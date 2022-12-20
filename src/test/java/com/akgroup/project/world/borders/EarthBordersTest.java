package com.akgroup.project.world.borders;

import com.akgroup.project.world.WorldConfiguration;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.map.WorldMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EarthBordersTest {

    IWorldMap worldMap;
    @BeforeEach
    void setUp(){
        worldMap = new WorldMap(10, 10, WorldConfiguration.defaultConfiguration());
    }

    @Test
    void getPositionOutOfMap() {
    }
}