package com.akgroup.project.engine;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.IPositionChangedObserver;
import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;

import java.util.List;

/**
 * Class responsible for main simulation loop
 */
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
        int width = simulationConfig.getValue(ConfigOption.WIDTH);
        int height = simulationConfig.getValue(ConfigOption.HEIGHT);
        for (int i = 0; i < simulationConfig.getValue(ConfigOption.ANIMALS_ON_START); i++) {
            Vector2D newVector = NumberGenerator.generateNextVector2D(width, height);
            int[] newGenome = NumberGenerator.generateNewGenome(simulationConfig.getValue(ConfigOption.GENOME_LENGTH));
            worldMap.placeObject(new Animal(newVector, newGenome));
        }
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
        deadAnimals
                .forEach(animal -> animal.setLastDay(animal.getAge()));
        for (Animal deadAnimal : deadAnimals) {
            worldMap.getPlanter().update(deadAnimal.getPosition(), -1);
            worldMap.removeObject(deadAnimal);
        }
    }

    private void moveAnimals() {
        List<Animal> animals = worldMap.getAllAnimals();
        System.out.println(animals);
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
        while (worldMap.getPlantsCount() < simulationConfig.getMapArea() && worldMap.getPlantsCount() < startingSize + increase) {
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