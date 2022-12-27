package com.akgroup.project.world.planter;

import com.akgroup.project.util.SortedList;
import com.akgroup.project.util.Vector2D;

import java.util.*;

public abstract class Planter {
    protected SortedList<Vector2DWithPossibility> possibility;

    protected Planter() {
        this.possibility = new SortedList<>(Comparator.comparing(Vector2DWithPossibility::getPossibility));
    }

    public SortedList<Vector2DWithPossibility> getPossibility() {
        return possibility;
    }

    public void addValue(Vector2D vector2D, int val) {
        possibility.add(new Vector2DWithPossibility(val, vector2D));
    }

    @Override
    public String toString() {
        return "Planter{" +
                "possibility=" + possibility +
                '}';
    }
}
