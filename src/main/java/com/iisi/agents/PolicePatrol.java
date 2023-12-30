package com.iisi.agents;

import com.iisi.City;
import com.iisi.SimulationConfig;
import com.iisi.utils.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolicePatrol extends Agent implements Stepable {

    public final Point initialPosition;
    private State state = State.PATROLLING;
    public final int baseSpeed = SimulationConfig.BASE_PATROL_SPEED;
    public final int interventionSpeed = SimulationConfig.INTERVENTION_PATROL_SPEED;
    private int durationOfShift = 0;

    private final static Logger LOGGER = LoggerFactory.getLogger(PolicePatrol.class);

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
                break;
            case TRANSFER_TO_INTERVENTION:
                moveToIncidentPlace();
                break;
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
                // if random is 4, then the agent stays in the same position
            }

            newPoint = new Point(x, y);
            if (City.instance().checkIfPositionIsTaken(newPoint) && district.allPointsInDistrict.contains(newPoint)) {
                position = newPoint;
                break;
            }

        }
    }

    public void moveToIncidentPlace() {
        Incident assignedIncident = City.instance().agentList.stream()
                .filter(agent -> agent instanceof Incident)
                .map(agent -> (Incident) agent)
                .filter(incident -> incident.getPatrolsReaching().stream()
                        .anyMatch(patrol -> patrol.id.equals(this.id)))
                .findFirst()
                .orElse(null);

        if (assignedIncident != null) {
            Point incidentPosition = assignedIncident.getPosition();

            int deltaX = Integer.compare(incidentPosition.x(), position.x());
            int deltaY = Integer.compare(incidentPosition.y(), position.y());

            Point newPosition = new Point(position.x() + deltaX * interventionSpeed,
                    position.y() + deltaY * interventionSpeed);

            if (Math.abs(incidentPosition.x() - position.x()) <= interventionSpeed &&
                    Math.abs(incidentPosition.y() - position.y()) <= interventionSpeed) {
                position = incidentPosition;
                state = State.INTERVENTION;
            } else if (City.isValidPoint(newPosition.x(), newPosition.y()) &&
                    City.instance().checkIfPositionIsTaken(newPosition)) {
                position = newPosition;
            }
        }
    }


    public State getState() {
        return state;
    }

    public void takeTask() {
        state = State.TRANSFER_TO_INTERVENTION;
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
