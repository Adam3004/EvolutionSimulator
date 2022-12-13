package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;

public enum Rotation {
    NORTH(0, 1),
    NORTH_WEST(1, 1),
    WEST(1, 0),
    SOUTH_WEST(1, -1),
    SOUTH(0, -1),
    SOUTH_EAST(-1, -1),
    EAST(-1, 0),
    NORTH_EAST(-1, 1);

    private final Vector2D movement;

    Rotation(int x, int y) {
        this.movement = new Vector2D(x, y);
    }

    public static Vector2D getVectorFromRotation(int rotation){
        return values()[rotation].movement;
    }
}
