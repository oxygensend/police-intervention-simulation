package com.iisi.agents;

import com.iisi.City;
import com.iisi.SimulationConfig;
import com.iisi.agents.PolicePatrol.State;
import com.iisi.utils.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.iisi.agents.PolicePatrol.State.FIRING;
import static com.iisi.agents.PolicePatrol.State.FIRING_TO_PATROLLING;
import static com.iisi.agents.PolicePatrol.State.NEUTRALIZED;

public class Incident extends Agent implements Stepable {

    private final static Logger LOGGER = LoggerFactory.getLogger(Incident.class);
    public int duration;
    private double probabilityOfFire;
    private boolean isFiring = false;
    private double strength;
    private int requiredPatrols;
    private List<PolicePatrol> patrolsSolving = new ArrayList<>();
    private List<PolicePatrol> patrolsReaching = new ArrayList<>();
    private boolean isActive;
    private Priority priority;
    private final int createdAt;

    public Incident(Point position, District district, Priority priority) {
        super(position, district);
        this.isActive = true;
        this.priority = priority;
        this.duration = new Random().nextInt(SimulationConfig.MAX_INTERVENTION_DURATION - SimulationConfig.MIN_INTERVENTION_DURATION)
                + SimulationConfig.MIN_INTERVENTION_DURATION;
        this.createdAt = City.instance().getSimulationDuration();
        district.statistics.incrementNumberOfInterventions();
    }

    @Override
    public void step() {
        if (patrolsSolving.isEmpty()) {
            return;
        } else {
            if (!isFiring) {
                duration = duration + new Random().nextInt(SimulationConfig.MAX_INTERVENTION_DURATION - SimulationConfig.MIN_INTERVENTION_DURATION)
                        + SimulationConfig.MIN_INTERVENTION_DURATION;

                var patrol = patrolsSolving.stream().findFirst().orElseThrow();

                int currentSimulationIteration = City.instance().getSimulationDuration();
                int elapsedTime = currentSimulationIteration - patrol.getAssignedTask().getIterationAtStart();
                double firingProbability = calculateFiringProbability(elapsedTime) * 0.001;
                double random =new Random().nextDouble();
                this.isFiring = random < firingProbability;
                if (isFiring) {
                    district.statistics.incrementNumberOfFirings();
                    priority = Priority.PRIOR;
                    patrol.setState(FIRING);
                    patrol.step();
                }
            }
        }

        if (isFiring) {
            var policePatrol = patrolsSolving.stream().findFirst().orElseThrow();

            int currentSimulationIteration = City.instance().getSimulationDuration();
            int elapsedTime = currentSimulationIteration - policePatrol.getAssignedTask().getIterationAtStart();
            double probabilityOfNeutralizedPatrol = calculatePoliceNeutralizedProbability(elapsedTime) * 0.1; // 10%
            double random = new Random().nextDouble();
            if ( random < probabilityOfNeutralizedPatrol) {
                LOGGER.info("Police is nautralized at {} {}!", this.position.x(), this.position.y());
                policePatrol.setState(NEUTRALIZED);
                policePatrol.step();
                patrolsSolving.remove(policePatrol);
                district.statistics.incrementNumberOfNeutralizedPatrols();
            }
        }

        if (isFiring && patrolsSolving.size() != 1 && patrolsSolving.size() != 0) {
            boolean isEveryPatrolOnFire = patrolsSolving.stream().allMatch(patrol -> patrol.getState().equals(PolicePatrol.State.FIRING));

            if (isEveryPatrolOnFire) {
                LOGGER.info("Firing {} at {} is solved. Removing...", id, position);
                isActive = false;
                isFiring = false;
                for (PolicePatrol patrol : patrolsSolving) {
                    patrol.setState(FIRING_TO_PATROLLING);
                    patrol.step();
                }
                patrolsReaching = null;
                patrolsSolving = null;

                district.statistics.incrementNumberOfSolvedFirings();
            }

        } else if (patrolsSolving.size() != 0) {
            var patrol = patrolsSolving.stream().findFirst().orElseThrow();
            var finishTime = patrol.getAssignedTask().getIterationAtStart() + duration;
            if (City.instance().getSimulationDuration() >= finishTime) {
                LOGGER.info("Incident {} at {} is solved. Removing...", id, position);
                isActive = false;
                isFiring = false;
                patrol.step();
                patrolsReaching = null;
                patrolsSolving = null;

                district.statistics.incrementNumberOfSolvedInterventions();
            }
        }
    }

    public double calculatePoliceNeutralizedProbability(int elapsedTime) {
        double maxProbability = 1.0; 
        double incrementPerIteration = maxProbability / SimulationConfig.MAX_INTERVENTION_DURATION;
        return Math.min(elapsedTime * incrementPerIteration, maxProbability);
    }


    private double calculateFiringProbability(int elapsedTime) {
        LOGGER.info("XXX- {}", elapsedTime);
        double maxProbability = 1.0;
        double incrementPerIteration = maxProbability / SimulationConfig.MAX_INTERVENTION_DURATION;
        return Math.min(elapsedTime * incrementPerIteration, maxProbability);
    }

    public double getProbabilityOfFire() {
        return probabilityOfFire;
    }

    public void setProbabilityOfFire(double probabilityOfFire) {
        this.probabilityOfFire = probabilityOfFire;
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public int getRequiredPatrols() {
        return requiredPatrols;
    }

    public void setRequiredPatrols(int requiredPatrols) {
        this.requiredPatrols = requiredPatrols;
    }

    public List<PolicePatrol> getPatrolsSolving() {
        return patrolsSolving;
    }

    public void assignSolvingPatrol(List<PolicePatrol> patrolsSolving) {
        this.patrolsSolving = patrolsSolving;
    }

    public void assignSolvingPatrol(PolicePatrol patrol) {
        if (this.patrolsSolving == null) {
            this.patrolsSolving = new ArrayList<>();
        }
        this.patrolsSolving.add(patrol);
    }

    public void removeSolvingPatrol(PolicePatrol patrol) {
        if (this.patrolsSolving == null) {
            this.patrolsSolving = new ArrayList<>();
        }
        this.patrolsSolving.remove(patrol);
    }

    public void removeReachingPatrol(PolicePatrol patrol) {
        if (this.patrolsReaching != null) {
            patrolsReaching.remove(patrol);
        }
    }

    public List<PolicePatrol> getPatrolsReaching() {
        return patrolsReaching;
    }

    public void setPatrolsReaching(List<PolicePatrol> patrolsReaching) {
        this.patrolsReaching = patrolsReaching;
    }

    public void setPatrolsReaching(PolicePatrol patrol) {
        if (this.patrolsReaching == null) {
            this.patrolsReaching = new ArrayList<>();
        }
        this.patrolsReaching.add(patrol);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Priority getPriority() {
        return priority;
    }

    public int getPriorityValue() {
        return priority.value;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public static enum Priority {
        LOW(4),
        MEDIUM(3),
        HIGH(2),
        PRIOR(1);

        public final int value;

        Priority(int value) {
            this.value = value;
        }

        public static Priority getRandomPriority() {
            return values()[(int) (Math.random() * (values().length - 1))]; // without PRIOR
        }
    }
}
