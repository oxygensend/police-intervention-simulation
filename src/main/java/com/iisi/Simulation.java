package com.iisi;

import com.iisi.agents.*;
import com.iisi.statistics.HistoricDistrictStatistics;
import com.iisi.statistics.StatisticsCsvWriter;
import com.iisi.utils.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class Simulation {
    private final static City CITY = City.instance();
    private final static Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

    public void run() throws InterruptedException {
        setUpAgents();

        while (CITY.getSimulationDuration() < SimulationConfig.SIMULATION_DURATION) {
            assignTasks();
            updatePatrolsState();
            updateIncidentsState();
            generateNewIncidents();
            removeInActiveAgents();

            CITY.incrementSimulationDuration();

            if (CITY.getSimulationDuration() != 0 && CITY.getSimulationDuration() % SimulationConfig.SHIFT_DURATION == 0) {
                LOGGER.info("Shift is over!------------------------------------------------------------------");
                changeShifts();
                LOGGER.info("Shift is changed!------------------------------------------------------------------");
            }

            LOGGER.info("Sleeping for 5 seconds------------------------------------------------------------------");
            Thread.sleep(100);
        }

        LOGGER.info("Simulation is over, saving data to csv files");
        StatisticsCsvWriter.saveDistrictStatistics();
    }

    private void removeInActiveAgents() {
        CITY.agentList.removeIf(agent -> !agent.isActive());
    }

    private void assignTasks() {
        for (var headquarters : CITY.agentList) {
            if (headquarters instanceof Headquarters) {
                ((Headquarters) headquarters).assignTasks();
            }
        }
    }

    private void changeShifts() {
        var headquarters = CITY.agentList.stream().filter(x -> x instanceof Headquarters).toList();
        for (var headquarter : headquarters) {
            if (headquarter instanceof Headquarters) {
                ((Headquarters) headquarter).newShift();
            }
        }
    }

    private void updatePatrolsState() {
        for (var police : CITY.agentList) {
            if (police instanceof PolicePatrol agent) {
                var previousPosition = police.getPosition();
                agent.step();
                if (!previousPosition.equals(police.getPosition())) {
                    LOGGER.info("Patrol {} moved from {} to {} | assigned task {}", police.id, previousPosition, police.getPosition(), ((PolicePatrol) police).getState());
                } else if (((PolicePatrol) police).getState() == PolicePatrol.State.INTERVENTION) {
                    LOGGER.info("Patrol {} is in intervention", police.id);
                } else if (((PolicePatrol) police).getState() == PolicePatrol.State.TRANSFER_TO_INTERVENTION) {
                    LOGGER.info("Patrol {} is in transfer to intervention", police.id);
                } else if (((PolicePatrol) police).getState() == PolicePatrol.State.TRANSFER_TO_FIRING) {
                    LOGGER.info("Patrol {} is in transfer to firing", police.id);
                } else if (((PolicePatrol) police).getState() == PolicePatrol.State.FIRING) {
                    LOGGER.info("Patrol {} is in firing", police.id);
                } else {
                    LOGGER.info("Patrol {} is in patrolling at {}", police.id, police.getPosition());
                }

            }
        }
    }

    private void updateIncidentsState() {
        for (var incident : CITY.agentList) {
            if (incident instanceof Incident) {
                ((Incident) incident).step();
                if (incident.isActive()) {
                    var beingSolved = ((Incident) incident).getPatrolsSolving().isEmpty() ? "not being solved" : "solving";
                    var patrolReached = ((Incident) incident).getPatrolsReaching().isEmpty() ? "not assigned" : "reaching";
                    var incidentName = ((Incident) incident).isFiring() ? "Firing" : "Intervention";
                    LOGGER.info("Incident({}) {} at {} is {} patrol {}",  incidentName, incident.id, incident.getPosition(), beingSolved, patrolReached);
                }
            }
        }
    }

    private void generateNewIncidents() {
        for (var district : CITY.districtList) {
            var probability = SimulationConfig.PROBABILITY_OF_INCIDENT_BY_THREAT_LEVEL.get(district.getThreatLevel());
            if (new Random().nextDouble() < probability) {
                var priority = Incident.Priority.getRandomPriority();
                var incident = new Incident(district.getRandomPositionInDistrict(), district, priority);
                CITY.addAgent(incident);
                LOGGER.info("Incident {} created at the position {} in district {}", incident.id, incident.getPosition(), district.name);
            }
        }
    }

    private void setUpAgents() {
        int index = 1;
        int[] patrolPerDistrict = calculateInitialPatrolPerDistrict();

        for (var districtSet : SimulationConfig.DISTRICT_BOUNDARIES_CONFIG.entrySet()) {
            var district = new District(index, districtSet.getKey(), districtSet.getValue(),
                                        SimulationConfig.DISTRICTS_THREAT_LEVEL_CONFIG.get(districtSet.getKey()),
                                        patrolPerDistrict[index - 1]);

            LOGGER.info("District {} created with threatLevel {}", district.name, district.getThreatLevel());
            City.instance().addDistrict(district);
            City.instance().addHistoricDistrictStatistics(new HistoricDistrictStatistics(district));

            for (int i = 0; i < district.initialNumberOfPatrols; i++) {
                var patrolPosition = district.getRandomPositionInDistrict();
                var patrol = new PolicePatrol(patrolPosition, district);
                City.instance().addAgent(patrol);
                LOGGER.info("Patrol {} created at the position {} in district {}", patrol.id, patrol.getPosition(), district.name);
            }

            index++;
        }

        var position = City.instance().getRandomPosition();
        District district = City.instance().districtList.get(0);
        Headquarters headquarters = new Headquarters(position, district);
        CITY.addAgent(headquarters);
        LOGGER.info("Headquarters created at the position [{},{}]", position.x(), position.y());
    }


    private int[] calculateInitialPatrolPerDistrict() {

        int[] patrolPerDistrict = new int[SimulationConfig.DISTRICT_BOUNDARIES_CONFIG.size()];
        int sum;
        int j = 0;

        while (true) {
            sum = ArrayUtils.sumArray(patrolPerDistrict);
            if (sum == SimulationConfig.NUMBER_OF_PATROLS) {
                break;
            }
            patrolPerDistrict[j]++;
            j++;

            if (j == patrolPerDistrict.length) {
                j = 0;
            }
        }

        return patrolPerDistrict;
    }


//    private static List<District> generateRandomDistricts() {
//        List<District> districts = new ArrayList<>();
//        List<District.Districts> districtNames = Arrays.asList(District.Districts.values());
//        Collections.shuffle(districtNames);
//
//        int index = 0;
//        int[] zones = {10, 20, 30, 40};
//        for (var districtSet : SimulationConfig.DISTRICTS_CONFIG.entrySet()) {
//            var district = new District(index, districtSet.getKey(), districtSet.getValue(),
//                                        District.ThreatLevel.from(SimulationConfig.BASE_THREAT_LEVEL),
//                                        4);
//            districts.add(district);
//
//            var GRID_SIZE = SimulationConfig.GRID_HEIGHT;
//            var random = new Random();
//            int startX = random.nextInt(GRID_SIZE);
//            int startY = random.nextInt(GRID_SIZE);
//
//            while (!isValid(startX, startY)) {
//                startX = random.nextInt(GRID_SIZE);
//                startY = random.nextInt(GRID_SIZE);
//            }
//
//            List<Point> districtCells = new ArrayList<>();
//            districtCells.add(new Point(startX, startY));
//            CITY.board[startX][startY] = district.id;
//
//            while (districtCells.size() < GRID_SIZE * GRID_SIZE) {
//                List<Point> newCells = new ArrayList<>();
//
//                for (Point cell : districtCells) {
//                    int x = cell.x();
//                    int y = cell.y();
//
//                    int[] dx = {-1, 1, 0, 0};
//                    int[] dy = {0, 0, -1, 1};
//
//                    for (int i = 0; i < 4; i++) {
//                        int newX = x + dx[i];
//                        int newY = y + dy[i];
//
//                        if (isValid(newX, newY)) {
//                            CITY.board[newX][newY] = district.id;
//                            newCells.add(new Point(newX, newY));
//                        }
//                    }
//                }
//
//                if (newCells.isEmpty()) {
//                    break;
//                }
//
//                districtCells.addAll(newCells);
//            }
//            index++;
//        }
//
//        int districtId = 1;
////        for (int x = 0; x < SimulationConfig.GRID_WIDTH; x++) {
////            for (int y = 0; y < SimulationConfig.GRID_HEIGHT; y++) {
////                if (CITY.board[x][y] == 0) {
////                    floodFill(x, y, districtId);
////                    districts.get(districtId - 1).boundaries.add(new Point(x, y));
////                    districtId++;
////                    if (districtId > districts.size()) {
////                        break;
////                    }
////                }
////            }
////        }
//
//        return districts;
//    }

//    private static boolean isValid(int x, int y) {
////        return x >= 0 && x < SimulationConfig.GRID_WIDTH && y >= 0 && y < SimulationConfig.GRID_HEIGHT && CITY.board[x][y] == 0;
////    }
////
////    // Function to perform flood fill algorithm for creating districts
////    private static void floodFill(int x, int y, int districtId) {
////        Stack<Point> stack = new Stack<>();
////        stack.push(new Point(x, y));
////
////        while (!stack.isEmpty()) {
////            Point point = stack.pop();
////            x = point.x();
////            y = point.y();
////
////            if (isValid(x, y)) {
////                CITY.board[x][y] = districtId;
////                stack.push(new Point(x + 1, y));
////                stack.push(new Point(x - 1, y));
////                stack.push(new Point(x, y + 1));
////                stack.push(new Point(x, y - 1));
////            }
////        }
////    }


}
