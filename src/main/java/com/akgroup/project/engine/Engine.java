package com.akgroup.project.engine;

import com.akgroup.project.config.Config;
import com.akgroup.project.util.MapVisualiser;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;

import java.util.List;

public class Engine implements Runnable {
    private WorldMap worldMap;
    private MapVisualiser visualiser;
    private final Config simulationConfig;

    public Engine(Config config) {
        this.simulationConfig = config;
    }

    @Override
    public void run() {
        worldMap = new WorldMap(simulationConfig);
        visualiser = new MapVisualiser(worldMap);
        summonStartPlants();
        summonStartAnimals();
        infinityLoop();
    }

    private void summonStartAnimals() {
        Animal animal = new Animal(new Vector2D(1, 1), new int[]{0, 5, 2, 4, 7});
        worldMap.placeObject(animal);
    }

    private void summonStartPlants() {
        worldMap.placeObject(new Plant(new Vector2D(9, 2)));
    }

    private void infinityLoop() {
        while (true) {
            visualiser.renderFrame();
            increaseAge();
            removeDeadAnimals();
            moveAnimals();
            eatPlants();
            multiplicationOfAnimals();
            summonNewPlants();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void removeDeadAnimals() {
        List<Animal> deadAnimals = worldMap.getAllAnimals().stream()
                .filter(Animal::isDead)
                .toList();
        for (Animal deadAnimal : deadAnimals) {
            worldMap.removeObject(deadAnimal);
        }
    }

    private void moveAnimals() {
        List<Animal> animals = worldMap.getAllAnimals();
        for (Animal animal : animals) {
            worldMap.rotateAndMove(animal);
        }
    }

    private void eatPlants() {
        worldMap.eatPlants();
    }

    private void multiplicationOfAnimals() {
        worldMap.multiplyAnimals();
    }

    private void summonNewPlants() {
        // ??
    }

    private void increaseAge() {
        worldMap.increaseAgeOfAnimals();
    }
}