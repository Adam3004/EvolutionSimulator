package com.akgroup.project.world.planter;

import com.akgroup.project.util.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PlanterTest {

    @Test
    void addValue() {
        EquatorPlanter equatorPlanter = new EquatorPlanter(14, 11);
//        equatorPlanter.init();
        equatorPlanter.addValue(new Vector2D(1, 2), 5);
        equatorPlanter.addValue(new Vector2D(2, 2), 1);
        equatorPlanter.addValue(new Vector2D(3, 2), 6);
        equatorPlanter.addValue(new Vector2D(4, 2), 8);
        equatorPlanter.addValue(new Vector2D(5, 2), 5);
        equatorPlanter.addValue(new Vector2D(4, 2), 0);
        assertEquals(equatorPlanter.getListOfPossibilities().size(), 6);

    }

    @Test
    void init() {
        EquatorPlanter equatorPlanter = new EquatorPlanter(14, 11);
        equatorPlanter.init();
        assertEquals(equatorPlanter.getEquatorHeight()[0], 4);
        assertEquals(equatorPlanter.getEquatorHeight()[1], 6);
        EquatorPlanter equatorPlanter2 = new EquatorPlanter(14, 1);
        equatorPlanter2.init();
        assertEquals(equatorPlanter2.getEquatorHeight()[0], 0);
        assertEquals(equatorPlanter2.getEquatorHeight()[1], 0);
        EquatorPlanter equatorPlanter3 = new EquatorPlanter(3, 2);
        equatorPlanter3.init();
        assertEquals(equatorPlanter3.getEquatorHeight()[0], 0);
        assertEquals(equatorPlanter3.getEquatorHeight()[1], 1);
        assertEquals(equatorPlanter3.getListOfPossibilities().size(), 6);
    }

    @Test
    void increasingPossibilitiesInList() {
        EquatorPlanter equatorPlanter = new EquatorPlanter(2, 12);
        equatorPlanter.init();
        assertEquals(equatorPlanter.getListOfPossibilities().stream().filter(data -> data.getPossibility() == 3).toList().size(), 3);
    }

    @Test
    void ToxicPlanterIncreasingValue() {
        ToxicPlanter toxicPlanter = new ToxicPlanter(10, 10);
        toxicPlanter.init();
        toxicPlanter.update(new Vector2D(1, 1), -1);
        toxicPlanter.update(new Vector2D(1, 1), -1);
        toxicPlanter.update(new Vector2D(1, 1), -1);
        assertEquals(toxicPlanter.getListOfPossibilities().stream().filter(data -> data.getVector2D().equals(new Vector2D(1, 1))).toList().get(0).getPossibility(), -2);
    }


    @Test
    void GeneratingTest() {
        EquatorPlanter equatorPlanter = new EquatorPlanter(4, 4);
        equatorPlanter.init();
        for (int i = 0; i < 20; i++) {
            equatorPlanter.findNewVectorToPlant();
        }
    }

}