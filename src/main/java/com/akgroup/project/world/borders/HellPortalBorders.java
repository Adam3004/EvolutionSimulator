package com.akgroup.project.world.borders;

import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;

public class HellPortalBorders extends MapBorders {
    public HellPortalBorders(int width, int height) {
        super(width, height);
    }

    @Override
    public Vector2D repairAnimalPosition(Animal animal, Vector2D position) {
        if (position.follows(lowerLeft) && position.precedes(upperRight)) return position;
        int newX = NumberGenerator.generateNextInt(lowerLeft.x, upperRight.x);
        int newY = NumberGenerator.generateNextInt(lowerLeft.y, upperRight.y);
        animal.loseEnergy(4);
        return new Vector2D(newX, newY);
    }
}
