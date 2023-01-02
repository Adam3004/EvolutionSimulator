package com.akgroup.project.world.borders;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;

public abstract class MapBorders {

    protected final Vector2D lowerLeft, upperRight;

    public MapBorders(int width, int height){
        this(new Vector2D(0, 0), new Vector2D(width-1, height-1));
    }

    public MapBorders(Vector2D lowerLeft, Vector2D upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    /** Method takes animal and its new position then returns new position depending on implementation*/
    public abstract Vector2D repairAnimalPosition(Animal animal, Vector2D position);
}