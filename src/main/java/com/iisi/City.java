package com.iisi;

import com.iisi.agents.Agent;
import com.iisi.agents.District;
import com.iisi.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class City {

    public static final int SHIFT_DURATION = 50;
    private static City instance;
    public final int[][] grid;
    public final List<Agent> agentList;
    public final List<District> districtList;

    private int neutralizedPatrolsTotal = 0;

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

    public void removeAgent(Agent entity) {
        agentList.remove(entity);
    }
}
