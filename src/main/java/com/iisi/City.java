package com.iisi;

import com.iisi.agents.Agent;
import com.iisi.agents.District;

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
}
