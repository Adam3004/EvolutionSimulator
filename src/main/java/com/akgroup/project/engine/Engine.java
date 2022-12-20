package com.akgroup.project.engine;

import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;

import java.util.List;

public class Engine {
    private final WorldMap worldMap;

    public Engine(IWorldMap worldMap) {
        this.worldMap = (WorldMap) worldMap;
    }

    public void run() {
        while (true) {
            removeDeadAnimals();
            moveAnimals();
            eatPlants();
            multiplicationOfAnimals();
            summonNewPlants();
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

        // find fields with more than one animal
        // choose two of them with maximal energy
        // generate genome based on parents genomes
        // create new animal object with new genome
    }

    private void summonNewPlants() {
        // ??
    }
}
