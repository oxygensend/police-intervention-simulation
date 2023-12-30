package com.iisi.agents;

import com.iisi.utils.Point;

import java.util.UUID;

public abstract class Agent {
    public final UUID id = UUID.randomUUID();
    protected Point position;

    protected Agent(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }


}
