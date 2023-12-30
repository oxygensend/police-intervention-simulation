package com.iisi.utils;

import com.iisi.agents.Incident;

public class Task {
    private int iterationAtStart;
    private final Incident target;

    public Task(Incident target) {
        this.target = target;
    }

    public Task(Incident target, int iterationAtStart) {
        this.target = target;
        this.iterationAtStart = iterationAtStart;
    }


    public int getIterationAtStart() {
        return iterationAtStart;
    }

    public void setIterationAtStart(int iterationAtStart) {
        this.iterationAtStart = iterationAtStart;
    }

    public Incident getTarget() {
        return target;
    }
}
