package com.iisi.agents;

import com.iisi.utils.Point;

import java.util.UUID;

public abstract class Agent {
    public final UUID id = UUID.randomUUID();
    public final District district;
    protected Point position;

    protected Agent(Point position, District district) {
        this.position = position;
        this.district = district;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void clear() {
        district.removeTakenPosition(position);
    }


}
