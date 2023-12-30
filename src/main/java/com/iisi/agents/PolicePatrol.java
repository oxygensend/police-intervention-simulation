package com.iisi.agents;

import com.iisi.City;
import com.iisi.SimulationConfig;
import com.iisi.utils.Point;

public class PolicePatrol extends Agent implements Stepable {

    public final Point initialPosition;
    private State state = State.PATROLLING;
    public final int baseSpeed = SimulationConfig.BASE_PATROL_SPEED;
    public final int interventionSpeed = SimulationConfig.INTERVENTION_PATROL_SPEED;
    private int durationOfShift = 0;

    public PolicePatrol(Point position, District district) {
        super(position, district);
        this.position = position;
        this.initialPosition = position;
    }

    public enum State {
        PATROLLING,
        TRANSFER_TO_INTERVENTION,
        TRANSFER_TO_FIRING,
        INTERVENTION,
        FIRING,
        NEUTRALIZED,
    }


    public void step() {
        switch (state) {
            case PATROLLING:
                randomStepInDistrict();
            case TRANSFER_TO_INTERVENTION:
            case TRANSFER_TO_FIRING:
            case FIRING:
            case INTERVENTION:
            case NEUTRALIZED:
                break;

        }
    }

    private void randomStepInDistrict() {
        int x;
        int y;
        int random;
        var newPoint = position;
        while (true) {
            x = position.x();
            y = position.y();
            random = (int) (Math.random() * 5);
            switch (random) {
                case 0 -> x += baseSpeed;
                case 1 -> x -= baseSpeed;
                case 2 -> y += baseSpeed;
                case 3 -> y -= baseSpeed;
            }

            newPoint = new Point(x, y);
            if (City.instance().checkIfPositionIsTaken(newPoint) && district.allPointsInDistrict.contains(newPoint)) {
                position = newPoint;
                break;
            }

        }
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
