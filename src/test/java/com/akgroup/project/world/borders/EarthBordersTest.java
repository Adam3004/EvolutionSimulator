package com.akgroup.project.world.borders;

import com.akgroup.project.config.Config;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EarthBordersTest {

    WorldMap worldMap;
    @BeforeEach
    void setUp(){
        worldMap = new WorldMap(Config.defaultConfig());
    }

    @Test
    void repairAnimalPosition() {
        Animal animal = new Animal(new Vector2D(0, 0), new int[]{4, 0, 2, 0, 0});
        worldMap.placeObject(animal);
        for (int i = 0; i < 4; i++) {
            worldMap.rotateAndMove(animal);
        }
    }
}