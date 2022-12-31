package com.akgroup.project.world.planter;

import com.akgroup.project.util.Vector2D;

public class ToxicPlanter extends Planter {
    public ToxicPlanter(int width, int height) {
        super(width, height);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update(Vector2D vector2D, int valueChange) {
        Vector2DWithPossibility currVector = listOfPossibilities.stream()
                .filter(data -> data.getVector2D().equals(vector2D))
                .toList().get(0);
        currVector.setPossibility(currVector.getPossibility() + valueChange);
        listOfPossibilities.sortList();
    }
}
