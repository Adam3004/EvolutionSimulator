package com.akgroup.project.engine;

import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.util.MapVisualiser;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import com.akgroup.project.world.planter.Planter;

import java.util.List;

public class Engine implements Runnable {
    private WorldMap worldMap;
    private MapVisualiser visualiser;
    private final Config simulationConfig;

    private int startingSize;

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
        worldMap.getWorldConfig().planter().init();
        summonNewPlants(simulationConfig.getValue(ConfigOption.PLANTS_ON_START));
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
            summonNewPlants(simulationConfig.getValue(ConfigOption.PLANTS_EVERY_DAY));
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

    private void summonNewPlants(int increase) {
        startingSize = worldMap.getPlantedFields().size();
        int area = simulationConfig.getValue(ConfigOption.WIDTH) * simulationConfig.getValue(ConfigOption.HEIGHT);
        while (worldMap.getPlantedFields().size() < area && worldMap.getPlantedFields().size() < startingSize + increase) {
            worldMap.placeObject(new Plant(worldMap.getWorldConfig().planter().findNewVector()));
        }
    }

    private void increaseAge() {
        worldMap.increaseAgeOfAnimals();
    }
}