package com.akgroup.project.world.planter;

import com.akgroup.project.util.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanterTest {

    @Test
    void addValue() {
        Planter equatorPlanter = new EquatorPlanter(10,10);
        equatorPlanter.addValue(new Vector2D(1,2), 5);
        equatorPlanter.addValue(new Vector2D(2,2), 1);
        equatorPlanter.addValue(new Vector2D(3,2), 6);
        equatorPlanter.addValue(new Vector2D(4,2), 8);
        equatorPlanter.addValue(new Vector2D(5,2), 5);
        equatorPlanter.addValue(new Vector2D(4,2), 0);

    }
}