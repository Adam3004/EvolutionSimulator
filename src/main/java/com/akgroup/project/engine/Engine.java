package com.akgroup.project.engine;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.IPositionChangedObserver;
import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;

import java.util.List;

/** Class responsible for main simulation loop*/
public class Engine implements Runnable, IPositionChangedObserver {
    private WorldMap worldMap;
    private final Config simulationConfig;
    private final IOutputObserver outputObserver;
    public Engine(Config config, IOutputObserver outputObserver) {
        this.simulationConfig = config;
        this.outputObserver = outputObserver;
    }

    @Override
    public void run() {
        worldMap = new WorldMap(simulationConfig);
        outputObserver.init(worldMap);
        worldMap.addPositionChangedObserver(this);
        summonStartPlants();
        summonStartAnimals();
        infinityLoop();
    }

    private void summonStartAnimals() {
        worldMap.placeObject(new Animal(new Vector2D(1, 1), new int[]{0, 4, 0, 0, 7}));
        //worldMap.placeObject(new Animal(new Vector2D(1, 2), new int[]{4, 4, 0, 0, 3}));
        //worldMap.placeObject(new Animal(new Vector2D(2, 2), new int[]{5, 4, 3, 1, 5}));
        //worldMap.placeObject(new Animal(new Vector2D(2, 1), new int[]{3, 7, 1, 5, 4}));
    }

    private void summonStartPlants() {
        worldMap.init();
        summonNewPlants(simulationConfig.getValue(ConfigOption.PLANTS_ON_START));
    }

    private void infinityLoop() {
        while (true) {
            outputObserver.renderFrame();
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
        int startingSize = worldMap.getPlantsCount();
        int area = simulationConfig.getMapArea();
        while (worldMap.getPlantsCount() < area && worldMap.getPlantsCount() < startingSize + increase) {
            worldMap.trySummonNewPlant();
        }
    }

    private void increaseAge() {
        worldMap.getAllAnimals().forEach(Animal::increaseAge);
    }

    @Override
    public void onPositionChanged(Animal animal, Vector2D oldPosition, Vector2D newPosition) {

    }
}