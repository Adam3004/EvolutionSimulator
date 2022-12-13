package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;

public abstract class AbstractWorldElement implements IWorldElement {
    protected Vector2D position;

    public AbstractWorldElement(Vector2D position) {
        this.position = position;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }
}
