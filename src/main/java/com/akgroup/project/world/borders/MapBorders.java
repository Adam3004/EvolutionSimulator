package com.akgroup.project.world.borders;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;

public abstract class MapBorders {
    protected IWorldMap worldMap;

    public void setWorldMap(IWorldMap worldMap) {
        this.worldMap = worldMap;
    }

    /** Method takes animal and its new position then returns new position depending on implementation*/
    public abstract Vector2D getPositionOutOfMap(Animal animal, Vector2D position);
}