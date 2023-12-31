package com.iisi.tasks;

import com.iisi.agents.Incident;

public class SolvingIncidentTask extends Task {
    private final Incident target;

    public SolvingIncidentTask(Incident target) {
        this.target = target;
    }

    public SolvingIncidentTask(Incident target, int iterationAtStart) {
        this.target = target;
        this.iterationAtStart = iterationAtStart;
    }

    public Incident getTarget() {
        return target;
    }
}
