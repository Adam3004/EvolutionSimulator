package com.akgroup.project.world.borders;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;

public class EarthBorders extends MapBorders {

    @Override
    public Vector2D repairAnimalPosition(Animal animal, Vector2D position) {
        int y = position.y;
        y = Math.max(Math.min(y, worldMap.getUpperRight().y), worldMap.getLowerLeft().y);
        int x = position.x;
        if (x < worldMap.getLowerLeft().x) {
            x = worldMap.getUpperRight().x;
        } else if (x > worldMap.getUpperRight().x) {
            x = worldMap.getLowerLeft().x;
        }
        return new Vector2D(x, y);
    }
}
