package com.akgroup.project.world.borders;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;

public class EarthBorders extends MapBorders {

    public EarthBorders(int width, int height) {
        super(width, height);
    }

    @Override
    public Vector2D repairAnimalPosition(Animal animal, Vector2D position) {
        int y = position.y;
        y = Math.max(Math.min(y, upperRight.y), lowerLeft.y);
        int x = position.x;
        if (x < lowerLeft.x) {
            x = upperRight.x;
        } else if (x > upperRight.x) {
            x = lowerLeft.x;
        }
        return new Vector2D(x, y);
    }
}
