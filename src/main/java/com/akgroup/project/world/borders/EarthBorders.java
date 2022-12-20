package com.akgroup.project.world.borders;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;

public class EarthBorders extends MapBorders {

    @Override
    public Vector2D getPositionOutOfMap(Vector2D position) {
        int y = position.y;
        y = Math.min(Math.max(y, worldMap.getUpperRight().y), worldMap.getLowerLeft().y);
        int x = position.x;
        if (x < worldMap.getLowerLeft().x) {
            x = worldMap.getUpperRight().x;
        } else {
            x = worldMap.getLowerLeft().x;
        }
        return new Vector2D(x, y);
    }
}
