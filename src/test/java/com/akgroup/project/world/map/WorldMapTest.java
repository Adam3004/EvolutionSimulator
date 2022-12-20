package com.akgroup.project.world.map;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.WorldConfiguration;
import com.akgroup.project.world.borders.EarthBorders;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {

    WorldMap worldMap;

    @BeforeEach
    void setUp(){
        worldMap = new WorldMap(100, 100, WorldConfiguration.defaultConfiguration());
    }

    @Test
    void placeObject() {
        Animal animal1 = new Animal(new Vector2D(1, 10));
        Animal animal2 = new Animal(new Vector2D(100, 101));
        Animal animal3 = new Animal(new Vector2D(-2, 5));
        Animal animal4 = new Animal(new Vector2D(0, 0));
        assertTrue(worldMap.placeObject(animal1));
        assertFalse(worldMap.placeObject(animal2));
        assertFalse(worldMap.placeObject(animal3));
        assertTrue(worldMap.placeObject(animal4));
    }

    @Test
    void getObjectsAt() {
        Animal animal1 = new Animal(new Vector2D(1, 10));
        Animal animal2 = new Animal(new Vector2D(100, 0));
        Animal animal3 = new Animal(new Vector2D(1, 10));
        Animal animal4 = new Animal(new Vector2D(0, 0));
        worldMap.placeObject(animal1);
        worldMap.placeObject(animal2);
        worldMap.placeObject(animal3);
        worldMap.placeObject(animal4);
        var objectsAt = worldMap.getObjectsAt(new Vector2D(1, 10));
        assertEquals(objectsAt.size(), 2);
        assertTrue(objectsAt.contains(animal1));
        assertFalse(objectsAt.contains(animal2));
        assertTrue(objectsAt.contains(animal3));
        var objectsAt2 = worldMap.getObjectsAt(new Vector2D(0, 0));
        assertEquals(objectsAt2.size(), 1);
        assertTrue(objectsAt2.contains(animal4));
        assertFalse(objectsAt2.contains(animal1));
    }

    @Test
    void getAllAnimals() {
        Animal animal1 = new Animal(new Vector2D(1, 10));
        Animal animal2 = new Animal(new Vector2D(100, 0));
        Animal animal3 = new Animal(new Vector2D(1, 10));
        worldMap.placeObject(animal1);
        worldMap.placeObject(animal2);
        worldMap.placeObject(animal3);
        Plant plant1 = new Plant(new Vector2D(2, 2));
        Plant plant2 = new Plant(new Vector2D(1, 10));
        worldMap.placeObject(plant1);
        worldMap.placeObject(plant2);
        List<Animal> allAnimals = worldMap.getAllAnimals();
        assertEquals(allAnimals.size(), 3);
        assertTrue(allAnimals.contains(animal1));
        assertTrue(allAnimals.contains(animal2));
        assertTrue(allAnimals.contains(animal3));
    }
}