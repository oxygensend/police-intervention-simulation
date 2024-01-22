package com.iisi;

import com.iisi.agents.Agent;
import com.iisi.agents.District;
import com.iisi.agents.Incident;
import com.iisi.agents.PolicePatrol;
import com.iisi.statistics.HistoricDistrictStatistics;
import com.iisi.statistics.HistoricSimulationStatistics;
import com.iisi.utils.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class City {

    private static final Logger LOGGER = LoggerFactory.getLogger(City.class);
    private static City instance;
    public final int[][] grid;
    public final List<Agent> agentList;
    public final List<District> districtList;
    public List<HistoricDistrictStatistics> historicDistrictStatisticsList;
    public List<HistoricSimulationStatistics> historicSimulationStatisticsList;
    private int neutralizedPatrolsTotal = 0;
    private int simulationDuration = 0;


    private City() {
        agentList = new ArrayList<>();
        districtList = new ArrayList<>();
        historicDistrictStatisticsList = new ArrayList<>();
        historicSimulationStatisticsList = new ArrayList<>();
        grid = new int[SimulationConfig.GRID_HEIGHT][SimulationConfig.GRID_WIDTH];
    }

    public static City instance() {
        if (instance == null) {
            instance = new City();
        }
        return instance;
    }


    public static boolean isValidPoint(int x, int y) {
        return x >= 0 && x < SimulationConfig.GRID_WIDTH && y >= 0 && y < SimulationConfig.GRID_HEIGHT;
    }

    public boolean checkIfPositionIsTaken(Point p) {
        return agentList.stream().findFirst().filter(a -> a.getPosition().equals(p)).isEmpty();
    }

    public int getNeutralizedPatrolsTotal() {
        return neutralizedPatrolsTotal;
    }

    public void setNeutralizedPatrolsTotal(int neutralizedPatrolsTotal) {
        this.neutralizedPatrolsTotal = neutralizedPatrolsTotal;
    }

    public void incrementNeutralizedPatrolsTotal() {
        this.neutralizedPatrolsTotal++;
    }


    public void addDistrict(District district) {
        districtList.add(district);
    }

    public void addAgent(Agent entity) {
        agentList.add(entity);
    }

    public void removeAgent(Agent agent) {
        agent.clear();
        agentList.remove(agent);
    }

    public Point getRandomPosition() {
        Random rand = new Random();
        int x = rand.nextInt(SimulationConfig.GRID_WIDTH);
        int y = rand.nextInt(SimulationConfig.GRID_HEIGHT);
        return new Point(x, y);
    }

    public PolicePatrol findNearestAvailablePolicePatrol(Incident incident) {
        return agentList.stream()
                        .filter(agent -> agent instanceof PolicePatrol)
                        .map(agent -> (PolicePatrol) agent)
                        .filter(patrol -> patrol.getState() == PolicePatrol.State.PATROLLING)
                        .min(Comparator.comparingDouble(patrol -> Point.calculateDistance(patrol.getPosition(), incident.getPosition())))
                        .orElse(null);
    }

    public int getSimulationDuration() {
        return simulationDuration;
    }

    public void setSimulationDuration(int simulationDuration) {
        this.simulationDuration = simulationDuration;
    }

    public void incrementSimulationDuration() {
        this.simulationDuration++;
    }

    public boolean isSimulationFinished() {
        return simulationDuration >= SimulationConfig.SIMULATION_DURATION;
    }


    public void addHistoricDistrictStatistics(HistoricDistrictStatistics historicDistrictStatistics) {
        historicDistrictStatisticsList.add(historicDistrictStatistics);
    }

    public void addHistoricSimulationStatistics(HistoricSimulationStatistics historicSimulationStatistics) {
        historicSimulationStatisticsList.add(historicSimulationStatistics);
    }

    public int getNumberOfPatrols() {
        return agentList.stream()
                        .filter(agent -> agent instanceof PolicePatrol)
                        .mapToInt(agent -> 1)
                        .sum();
    }

    public Map<PolicePatrol.State, Long> getNumberOfPatrolsByState() {
        return agentList.stream()
                        .filter(agent -> agent instanceof PolicePatrol)
                        .collect(Collectors.groupingBy(
                                agent -> ((PolicePatrol) agent).getState(),
                                Collectors.counting()
                        ));
    }


    public Map<District, Integer> calculateDangerLevelsAndPatrolNumberToDistrict() {
        int[] requiredPatrolsForDistrict = new int[districtList.size()];
        Arrays.fill(requiredPatrolsForDistrict, 1);
        double sumOfDangerCoefficients = districtList.stream().mapToDouble(District::calculateDangerCoefficient).sum();
        int totalPatrolsAssigned = 0;
        LOGGER.info("Sum of danger coefficients: {}", sumOfDangerCoefficients);
        var map = new HashMap<District, Integer>();

        for (var district :
                districtList) {

            double normalizedDanger = district.calculateDangerCoefficient() / sumOfDangerCoefficients;
            LOGGER.info("Normalized danger for district {}: {}", district.name, normalizedDanger);

            // Calculate the number of patrols for the district based on the normalized danger
            int numberOfPatrols = 1;
            numberOfPatrols += (int) Math.ceil(normalizedDanger * (SimulationConfig.NUMBER_OF_PATROLS - districtList.size()));


            // Ensure the total number of patrols doesn't exceed maximum value
            numberOfPatrols = Math.min(numberOfPatrols, SimulationConfig.NUMBER_OF_PATROLS - totalPatrolsAssigned);

            LOGGER.info("Number of patrols for district {}: {}", district.name, numberOfPatrols);
            district.setDangerCoefficient(normalizedDanger);
            district.setNumberOfPatrols(numberOfPatrols);
            City.instance().addHistoricDistrictStatistics(new HistoricDistrictStatistics(district));
//            district.statistics.reset();
            district.statistics.setNumberOfPatrols(numberOfPatrols);
            totalPatrolsAssigned += numberOfPatrols;

            map.put(district, numberOfPatrols);

        }

        for (var district : districtList) {
            if (district.getNumberOfPatrols() == 0) {
                District districtWithMostPatrols = districtList.stream()
                                                               .max(Comparator.comparingInt(District::getNumberOfPatrols))
                                                               .orElse(null);

                if (districtWithMostPatrols != null && districtWithMostPatrols.getNumberOfPatrols() > 1) {
                    int currentPatrols = districtWithMostPatrols.getNumberOfPatrols();
                    map.put(districtWithMostPatrols, currentPatrols - 1);
                    map.put(district, 1);
                    district.setNumberOfPatrols(1);
                    districtWithMostPatrols.setNumberOfPatrols(currentPatrols - 1);
                    City.instance().addHistoricDistrictStatistics(new HistoricDistrictStatistics(district));
                    City.instance().addHistoricDistrictStatistics(new HistoricDistrictStatistics(districtWithMostPatrols));

                    LOGGER.info("Adjusted patrols: 1 patrol assigned to district {} from district {}",
                                district.name, districtWithMostPatrols.name);
                } else {
                    LOGGER.info("Could not adjust patrols for district {}", district.name);
                }
            }
        }


        return map;

    }

}

