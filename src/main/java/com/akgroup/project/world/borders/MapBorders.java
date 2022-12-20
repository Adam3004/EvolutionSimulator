package com.akgroup.project.world.borders;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;

public abstract class MapBorders {
    protected IWorldMap worldMap;

    public void setWorldMap(IWorldMap worldMap) {
        this.worldMap = worldMap;
    }

    /** Method takes position of object which is outside a map and returns new position depending on implementation*/
    public abstract Vector2D getPositionOutOfMap(Vector2D position);
}