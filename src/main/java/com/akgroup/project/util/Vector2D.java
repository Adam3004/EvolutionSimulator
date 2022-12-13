package com.akgroup.project.util;

import java.util.Objects;

public class Vector2D {
    private final int x, y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns true when both fields have a value less than or equal to the fields of the other object
     * */
    public boolean precedes(Vector2D other){
        return this.x <= other.x && this.y <= other.y;
    }

    /**
     * Returns true when both fields have a value greater than or equal to the fields of the other object
     * */
    public boolean follows(Vector2D other){
        return this.x >= other.x && this.y >= other.y;
    }

    /**
     * Returns new instance of Vector2D with fields are sum of this and other object
     * */
    public Vector2D add(Vector2D other){
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    /**
     * Returns new instance of Vector2D with fields are the difference of this and other object
     * */
    Vector2D subtract(Vector2D other){
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    /**
     * Returns new instance of Vector2D with each field is maximum of this and other fields
     * */
    Vector2D upperRight(Vector2D other){
        return new Vector2D(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }


    /**
     * Returns new instance of Vector2D with each field is minimum of this and other fields
     * */
    Vector2D lowerLeft(Vector2D other){
        return new Vector2D(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    /**
     * Returns opposite vector2d
     * */
    Vector2D opposite(){
        return new Vector2D(-this.x, -this.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2D vector2D)) return false;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}