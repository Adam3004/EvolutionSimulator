package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;

public class Plant extends AbstractWorldElement{

    public Plant(Vector2D position) {
        super(position);
    }

    @Override
    public TypeEnum getType() {
        return TypeEnum.PLANT;
    }
}
