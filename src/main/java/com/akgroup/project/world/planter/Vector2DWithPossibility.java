package com.akgroup.project.world.planter;

import com.akgroup.project.util.Vector2D;

import java.util.Objects;

public class Vector2DWithPossibility {
    private int possibility;
    private final Vector2D vector2D;

    public Vector2DWithPossibility(int possibility, Vector2D vector2D) {
        this.possibility = possibility;
        this.vector2D = vector2D;
    }

    public int getPossibility() {
        return possibility;
    }

    public void setPossibility(int possibility) {
        this.possibility = possibility;
    }

    public Vector2D getVector2D() {
        return vector2D;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2DWithPossibility that = (Vector2DWithPossibility) o;
        return possibility == that.possibility && vector2D.equals(that.vector2D);
    }

    @Override
    public int hashCode() {
        return Objects.hash(possibility, vector2D);
    }

    @Override
    public String toString() {
        return "Vector2DWithPossibility{" +
                "possibility=" + possibility +
                ", vector2D=" + vector2D +
                '}';
    }
}
