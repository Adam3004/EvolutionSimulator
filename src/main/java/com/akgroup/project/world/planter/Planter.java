package com.akgroup.project.world.planter;

import com.akgroup.project.util.SortedList;
import com.akgroup.project.util.Vector2D;

import java.util.*;

public abstract class Planter {
    protected SortedList<Vector2DWithPossibility> listOfPossibilities;
    protected int width;
    protected int height;

    protected Planter(int width, int height) {
        this.listOfPossibilities = new SortedList<>(Comparator.comparing(Vector2DWithPossibility::getPossibility));
        this.width = width;
        this.height = height;
    }

    public SortedList<Vector2DWithPossibility> getListOfPossibilities() {
        return listOfPossibilities;
    }

    public void addValue(Vector2D vector2D, int val) {
        listOfPossibilities.add(new Vector2DWithPossibility(val, vector2D));
    }

    public void init() {
        for (int currW = 0; currW < width; currW++) {
            for (int currH = 0; currH < height; currH++) {
                listOfPossibilities.add(new Vector2DWithPossibility(1, new Vector2D(currW, currH)));
            }
        }
    }

    public abstract void update(Vector2D vector2D, int valueChange);


    @Override
    public String toString() {
        return "Planter{" +
                "possibility=" + listOfPossibilities +
                '}';
    }
}
