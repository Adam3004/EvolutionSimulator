package com.akgroup.project.world.borders;

import com.akgroup.project.engine.SimulationConfig;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;

public class HellPortalBorders extends MapBorders {
    @Override
    public Vector2D getPositionOutOfMap(Animal animal, Vector2D position) {
        Vector2D lowerLeft = worldMap.getLowerLeft();
        Vector2D upperRight = worldMap.getUpperRight();
        if (position.follows(lowerLeft) && position.precedes(upperRight)) return position;
        int newX = NumberGenerator.generateNextInt(lowerLeft.x, upperRight.x);
        int newY = NumberGenerator.generateNextInt(lowerLeft.y, upperRight.y);
        animal.loseEnergy(SimulationConfig.getInstance().getMultiplicationEnergyLose());
        return new Vector2D(newX, newY);
    }
}
