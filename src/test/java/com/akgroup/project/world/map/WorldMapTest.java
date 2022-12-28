package com.akgroup.project.world.map;

import com.akgroup.project.config.Config;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {

    WorldMap worldMap;
    Animal animal1, animal2, animal3, animal4;

    @BeforeEach
    void setUp(){
        worldMap = new WorldMap(Config.defaultConfig());
        animal1 = new Animal(new Vector2D(1, 9), NumberGenerator.generateNewGenome());
        animal2 = new Animal(new Vector2D(100, 101), NumberGenerator.generateNewGenome());
        animal3 = new Animal(new Vector2D(-2, 5), NumberGenerator.generateNewGenome());
        animal4 = new Animal(new Vector2D(0, 0), NumberGenerator.generateNewGenome());
    }

    @Test
    void placeObject() {
        assertTrue(worldMap.placeObject(animal1));
        assertFalse(worldMap.placeObject(animal2));
        assertFalse(worldMap.placeObject(animal3));
        assertTrue(worldMap.placeObject(animal4));
    }

    @Test
    void getObjectsAt() {
        worldMap.placeObject(animal1);
        worldMap.placeObject(animal2);
        worldMap.placeObject(animal3);
        worldMap.placeObject(animal4);
        var objectsAt = worldMap.getObjectsAt(new Vector2D(1, 9));
        assertEquals(objectsAt.size(), 1);
        assertTrue(objectsAt.contains(animal1));
        assertFalse(objectsAt.contains(animal2));
        assertFalse(objectsAt.contains(animal3));
        var objectsAt2 = worldMap.getObjectsAt(new Vector2D(0, 0));
        assertEquals(objectsAt2.size(), 1);
        assertTrue(objectsAt2.contains(animal4));
        assertFalse(objectsAt2.contains(animal1));
    }

    @Test
    void getAllAnimals() {
        worldMap.placeObject(animal1);
        worldMap.placeObject(animal2);
        worldMap.placeObject(animal3);
        Plant plant1 = new Plant(new Vector2D(2, 2));
        Plant plant2 = new Plant(new Vector2D(1, 10));
        worldMap.placeObject(plant1);
        worldMap.placeObject(plant2);
        List<Animal> allAnimals = worldMap.getAllAnimals();
        assertEquals(allAnimals.size(), 1);
        assertTrue(allAnimals.contains(animal1));
        assertFalse(allAnimals.contains(animal2));
        assertFalse(allAnimals.contains(animal3));
    }


    @Test
    void rotateAndMove() {
        Animal animal5 = new Animal(new Vector2D(2, 2), new int[]{2, 3, 5, 2, 0});
        worldMap.placeObject(animal5);
        worldMap.rotateAndMove(animal5);
        assertEquals(animal5.getPosition(), new Vector2D(3, 2));
        worldMap.rotateAndMove(animal5);
        System.out.println(animal5.getPosition());
        assertEquals(animal5.getPosition(), new Vector2D(2, 1));
    }
}