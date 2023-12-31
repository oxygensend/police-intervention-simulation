package com.iisi.tasks;

import com.iisi.utils.Point;

public class HeadingBackToDistrictTask extends Task {

    private final Point destination;

    public HeadingBackToDistrictTask(Point destination, int iterationAtStart) {
        this.destination = destination;
        this.iterationAtStart = iterationAtStart;
    }

    public Point getDestination() {
        return destination;
    }
}
