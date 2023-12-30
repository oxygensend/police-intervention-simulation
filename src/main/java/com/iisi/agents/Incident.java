package com.iisi.agents;

import com.iisi.City;
import com.iisi.SimulationConfig;
import com.iisi.utils.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Incident extends Agent implements Stepable {

    private final static Logger LOGGER = LoggerFactory.getLogger(Incident.class);
    public final int duration;
    private double probabilityOfFire;
    private boolean isFiring = false;
    private double strength;
    private int requiredPatrols;
    private List<PolicePatrol> patrolsSolving = new ArrayList<>();
    private List<PolicePatrol> patrolsReaching = new ArrayList<>();

    private boolean isActive;

    public Incident(Point position, District district) {
        super(position, district);
        isActive = true;
        duration = new Random().nextInt(SimulationConfig.MAX_INTERVENTION_DURATION - SimulationConfig.MIN_INTERVENTION_DURATION)
                + SimulationConfig.MIN_INTERVENTION_DURATION;
    }

    @Override
    public void step() {
        if (patrolsSolving.isEmpty()) {
            return;
        }

        if (isFiring) {

        } else {
            var patrol = patrolsSolving.stream().findFirst().orElseThrow();
            var finishTime = patrol.getAssignedTask().getIterationAtStart() + duration;
            if (City.instance().getSimulationDuration() >= finishTime) {
                LOGGER.info("Incident {} at {} is solved. Removing...", id, position);
                isActive = false;
                patrol.step();
            }

        }

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
}
