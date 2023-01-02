package com.akgroup.project.engine;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.IPositionChangedObserver;
import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.engine.statistics.StatSpectator;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for main simulation loop
 */
public class Engine implements Runnable, IPositionChangedObserver {

    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
    private WorldMap worldMap;
    private final Config simulationConfig;
    private final List<IOutputObserver> outputObservers;
    private final StatSpectator spectator;
    private static final int INITIATION_TIME = 50;
    private static final int TIME_DELAY = 100;

    public Engine(Config config, StatSpectator spectator) {
        this.simulationConfig = config;
        this.outputObservers = new ArrayList<>();
        this.spectator = spectator;
    }

    @Override
    public void run() {
        worldMap = new WorldMap(simulationConfig, spectator);
        outputObservers.forEach(obs -> obs.init(worldMap));
        worldMap.addPositionChangedObserver(this);
        worldMap.init();
        sleepForInitiation();
        summonStartPlants();
        summonStartAnimals();
        mainLoop();
    }

    private void summonStartAnimals() {
        int width = simulationConfig.getWidth();
        int height = simulationConfig.getHeight();
        int genomeLen = simulationConfig.getValue(ConfigOption.GENOME_LENGTH);
        for (int i = 0; i < simulationConfig.getValue(ConfigOption.ANIMALS_ON_START); i++) {
            Vector2D newVector = NumberGenerator.generateNextVector2D(width, height);
            int[] newGenome = NumberGenerator.generateNewGenome(genomeLen);
            Animal newAnimal = new Animal(newVector, newGenome);
            worldMap.placeObject(newAnimal);
        }
    }

    private void summonStartPlants() {
        int numberOfStartingPlants = simulationConfig.getValue(ConfigOption.PLANTS_ON_START);
        summonNewPlants(numberOfStartingPlants);
    }

    private void mainLoop() {
        while (running) {
            synchronized (pauseLock) {
                if (!running) break;
                if (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) break;
                }
            }
            increaseAge();
            removeDeadAnimals();
            moveAnimals();
            eatPlants();
            multiplicationOfAnimals();
            summonNewPlants(simulationConfig.getValue(ConfigOption.PLANTS_EVERY_DAY));
            refreshFieldStatistics();
            notifyOutputObservers();
            waitForMoment(TIME_DELAY);
        }
    }

    private void notifyOutputObservers() {
        List<Animal> animals = new ArrayList<>(worldMap.getAllAnimals());
        List<Plant> plants = new ArrayList<>(worldMap.getPlants());
        outputObservers.forEach(obs -> obs.renderFrame(animals, plants));
        System.out.println(animals);
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
        animals.forEach(worldMap::rotateAndMove);
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

    public void addOutputObserver(IOutputObserver outputObserver) {
        this.outputObservers.add(outputObserver);
    }

    public void refreshFieldStatistics() {
        spectator.setFreeFields(worldMap.countFreeFields());
    }

    @Override
    public void onPositionChanged(Animal animal, Vector2D oldPosition, Vector2D newPosition) {

    }

    private void sleepForInitiation() {
        waitForMoment(INITIATION_TIME);
    }

    private void waitForMoment(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    // MultiThread functions

    public void stop() {
        running = false;
        resume();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }
}