package com.iisi.agents;

import com.iisi.City;
import com.iisi.SimulationConfig;
import com.iisi.tasks.HeadingBackToDistrictTask;
import com.iisi.tasks.Task;
import com.iisi.utils.Point;
import com.iisi.tasks.SolvingIncidentTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolicePatrol extends Agent implements Stepable {

    public final Point initialPosition;
    private State state = State.PATROLLING;
    public final int baseSpeed = SimulationConfig.BASE_PATROL_SPEED;
    public final int interventionSpeed = SimulationConfig.INTERVENTION_PATROL_SPEED;
    private int durationOfShift = 0;
    private Task assignedTask;
    private final static Logger LOGGER = LoggerFactory.getLogger(PolicePatrol.class);

    public PolicePatrol(Point position, District district) {
        super(position, district);
        this.position = position;
        this.initialPosition = position;
    }

    public enum State {
        PATROLLING,
        TRANSFER_TO_INTERVENTION, TRANSFER_TO_FIRING,
        INTERVENTION,
        FIRING,
        NEUTRALIZED,
        FIRING_TO_PATROLLING,
    }


    public void step() {
        switch (state) {
            case PATROLLING:
                patrollingStep();
                break;
            case TRANSFER_TO_INTERVENTION:
                moveToIncidentPlace();
                break;
            case TRANSFER_TO_FIRING:
            case FIRING:
            case INTERVENTION:
                interventionStep();
                break;
            case NEUTRALIZED:
                break;
            case FIRING_TO_PATROLLING:
                firingToPatrollingStep();
                break;
        }
    }

    private void patrollingStep() {
        if (assignedTask == null) {
            randomStepInDistrict();
        } else {
            headingBackToDistrict();
        }
    }

    private void headingBackToDistrict() {
        if(state == State.FIRING_TO_PATROLLING) {
            state = State.PATROLLING;
        }
        var headingBackToDistrictTask = (HeadingBackToDistrictTask) assignedTask;
        var destination = headingBackToDistrictTask.getDestination();
        if (position.equals(destination)) {
            assignedTask = null;
            LOGGER.info("Patrol {} reached district at {}. Changing state to {}", id, position, state);
        } else {
            int deltaX = Integer.compare(destination.x(), position.x());
            int deltaY = Integer.compare(destination.y(), position.y());

            Point newPosition = new Point(position.x() + deltaX * baseSpeed,
                                          position.y() + deltaY * baseSpeed);

            if (City.isValidPoint(newPosition.x(), newPosition.y())) {
                position = newPosition;
            }
        }
    }

    private void firingToPatrollingStep() {
        var headingPoint = district.findTheNearestPointFromDifferentDistrict(position);
        assignedTask = new HeadingBackToDistrictTask(headingPoint, City.instance().getSimulationDuration());
        patrollingStep();
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

        if (assignedTask == null) {
            return;
        }

        var incident = ((SolvingIncidentTask) assignedTask).getTarget();
        Point incidentPosition = incident.getPosition();

        int deltaX = Integer.compare(incidentPosition.x(), position.x());
        int deltaY = Integer.compare(incidentPosition.y(), position.y());

        Point newPosition = new Point(position.x() + deltaX * interventionSpeed,
                                      position.y() + deltaY * interventionSpeed);

        if (Math.abs(incidentPosition.x() - position.x()) <= interventionSpeed &&
                Math.abs(incidentPosition.y() - position.y()) <= interventionSpeed) {
            position = incidentPosition;
            state = State.INTERVENTION;
            incident.assignSolvingPatrol(this);
            incident.removeReachingPatrol(this);
            assignedTask.setIterationAtStart(City.instance().getSimulationDuration());
            LOGGER.info("Patrol {} reached incident at {}. Starting solving intervention at {} iteration", id, position, assignedTask.getIterationAtStart());
        } else if (City.isValidPoint(newPosition.x(), newPosition.y()) &&
                City.instance().checkIfPositionIsTaken(newPosition)) {
            position = newPosition;
        }
    }

    private void interventionStep() {

        var incident = ((SolvingIncidentTask) assignedTask).getTarget();
        if (incident == null) {
            state = State.PATROLLING;
            assignedTask = null;
            LOGGER.info("Patrol {} finished intervention at {}. Changing state to {}", id, position, state);
        } else if (incident.isFiring()) {
            state = State.FIRING;
            assignedTask = new SolvingIncidentTask(incident, City.instance().getSimulationDuration());
            LOGGER.info("Patrol {} started firing at {}. Changing state to {}", id, position, state);
        } else if (!incident.isActive()) {
            state = State.PATROLLING;
            if (incident.district != district) {
                LOGGER.info("Patrol heading back to his district from {} to {}", incident.district.name, district.name);
                var headingPoint = district.findTheNearestPointFromDifferentDistrict(position);
                assignedTask = new HeadingBackToDistrictTask(headingPoint, City.instance().getSimulationDuration());
            } else {
                assignedTask = null;
            }
            LOGGER.info("Patrol {} finished intervention at {}. Changing state to {}", id, position, state);
        }
    }

    public State getState() {
        return state;
    }

    public void takeTask(Incident incident) {
        state = State.TRANSFER_TO_INTERVENTION;
        assignedTask = new SolvingIncidentTask(incident);
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

    public Task getAssignedTask() {
        return assignedTask;
    }
}
