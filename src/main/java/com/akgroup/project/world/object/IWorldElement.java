package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;

public interface IWorldElement {
    TypeEnum getType();

    Vector2D getPosition();
}
