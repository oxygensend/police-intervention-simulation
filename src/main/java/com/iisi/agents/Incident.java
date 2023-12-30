package com.iisi.agents;

import com.iisi.utils.Point;

import java.util.List;

public class Incident extends Agent implements Stepable {

    public final int duration = 0;
    private double probabilityOfFire;
    private boolean isFiring;
    private double strength;
    private int requiredPatrols;
    private List<PolicePatrol> patrolsSolving;
    private List<PolicePatrol> patrolsReaching;

    public Incident(Point position, District district) {
        super(position, district);
    }

    @Override
    public void step() {
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

    public void setPatrolsSolving(List<PolicePatrol> patrolsSolving) {
        this.patrolsSolving = patrolsSolving;
    }

    public List<PolicePatrol> getPatrolsReaching() {
        return patrolsReaching;
    }

    public void setPatrolsReaching(List<PolicePatrol> patrolsReaching) {
        this.patrolsReaching = patrolsReaching;
    }
}