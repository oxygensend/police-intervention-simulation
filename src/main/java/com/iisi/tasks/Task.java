package com.iisi.tasks;

public abstract class Task {
    protected int iterationAtStart;

    public int getIterationAtStart() {
        return iterationAtStart;
    }

    public void setIterationAtStart(int iterationAtStart) {
        this.iterationAtStart = iterationAtStart;
    }
}
