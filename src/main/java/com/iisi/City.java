package com.iisi;

import com.iisi.agents.Agent;
import com.iisi.agents.District;
import com.iisi.agents.Incident;
import com.iisi.agents.PolicePatrol;
import com.iisi.utils.Point;

import java.util.*;

public class City {

    public static final int SHIFT_DURATION = 50;
    private static City instance;
    public final int[][] grid;
    public final List<Agent> agentList;
    public final List<District> districtList;
    private int neutralizedPatrolsTotal = 0;
    private int simulationDuration = 0;

    private City() {
        agentList = new ArrayList<>();
        districtList = new ArrayList<>();
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
                        .min(Comparator.comparingDouble(patrol -> calculateDistance(patrol.getPosition(), incident.getPosition())))
                        .orElse(null);
    }

    private double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x() - p2.x(), 2) + Math.pow(p1.y() - p2.y(), 2));
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
}

