package com.akgroup.project.engine;

import com.akgroup.project.util.MapVisualiser;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;

import java.util.List;

public class Engine {
    private final WorldMap worldMap;

    public Engine(IWorldMap worldMap) {
        this.worldMap = (WorldMap) worldMap;
        this.visualiser = new MapVisualiser(this.worldMap);
    }

    private final MapVisualiser visualiser;

    public void run() {
        while (true) {
            visualiser.renderFrame();
            increaseAge();
            removeDeadAnimals();
            moveAnimals();
            eatPlants();
            multiplicationOfAnimals();
            summonNewPlants();
            try {
                Thread.sleep(1000);
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
