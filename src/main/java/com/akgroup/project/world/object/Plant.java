package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;

public class Plant extends AbstractWorldElement {
    private int nutritionalValue;

    public Plant(Vector2D position) {
        super(position);
        this.nutritionalValue = 3;
    }

    public int getNutritionalValue() {
        return nutritionalValue;
    }

    @Override
    public TypeEnum getType() {
        return TypeEnum.PLANT;
    }
}
