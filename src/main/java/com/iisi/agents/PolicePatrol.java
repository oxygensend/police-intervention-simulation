package com.iisi.agents;

import com.iisi.SimulationConfig;
import com.iisi.utils.Point;

public class PolicePatrol extends Agent {

    public final Point initialPosition;
    public final District district;
    private State state = State.PATROLLING;
    public final int baseSpeed = SimulationConfig.BASE_PATROL_SPEED;
    public final int interventionSpeed = SimulationConfig.INTERVENTION_PATROL_SPEED;
    private int durationOfShift = 0;

    public PolicePatrol( Point position, District district) {
        super(position);
        this.position = position;
        this.initialPosition = position;
        this.district = district;
    }

    public enum State {
        PATROLLING,
        TRANSFER_TO_INTERVENTION,
        TRANSFER_TO_FIRING,
        INTERVENTION,
        FIRING,
        NEUTRALIZED,
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getDurationOfShift() {
        return durationOfShift;
    }

    public void setDurationOfShift(int durationOfShift) {
        this.durationOfShift = durationOfShift;
    }
}
