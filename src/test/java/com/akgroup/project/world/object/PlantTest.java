package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {
    private Plant plant1;
    private IWorldElement plant2;

    @BeforeEach
    void setPlant1() {
        plant1 = new Plant(new Vector2D(1, 2));
        plant2 = new Plant(new Vector2D(5, -1));
    }

    @Test
    void getType() {
        assertEquals(plant1.getType(), TypeEnum.PLANT);
        assertEquals(plant2.getType(), TypeEnum.PLANT);
    }
}