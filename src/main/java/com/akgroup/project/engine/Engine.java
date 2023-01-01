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
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Arrays;
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
            Animal newAnimal = new Animal(newVector, newGenome);
            worldMap.placeObject(newAnimal);
        }
    }

    private void summonStartPlants() {
        worldMap.init();
        int numberOfStartingPlants = simulationConfig.getValue(ConfigOption.PLANTS_ON_START);
        summonNewPlants(numberOfStartingPlants);
    }

    private void infinityLoop() {
        while (running) {
            synchronized (pauseLock) {
                if (!running) { // may have changed while waiting to
                    // synchronize on pauseLock
                    break;
                }
                if (paused) {
                    try {
                        pauseLock.wait(); // will cause this Thread to block until
                        // another thread calls pauseLock.notifyAll()
                        // Note that calling wait() will
                        // relinquish the synchronized lock that this
                        // thread holds on pauseLock so another thread
                        // can acquire the lock to call notifyAll()
                        // (link with explanation below this code)
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) { // running might have changed since we paused
                        break;
                    }
                }
            }
            outputObservers.forEach((obs) -> obs.renderFrame(new ArrayList<>(worldMap.getAllAnimals()), new ArrayList<>(worldMap.getPlants())));
            increaseAge();
            removeDeadAnimals();
            moveAnimals();
            eatPlants();
            multiplicationOfAnimals();
            summonNewPlants(simulationConfig.getValue(ConfigOption.PLANTS_EVERY_DAY));
            refreshFieldStatistics();
            showStats();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showStats() {
        System.out.println("all animals: " + spectator.getNumberOfAliveAnimals() + ", plants: " + spectator.getNumberOfPlants() + ", free fields: " + spectator.getFreeFields() + ", most popular genotype: " + Arrays.toString(spectator.getMostPopularGenotype()) + ", energy: " + spectator.getAverageEnergy() + ", ageOfDyingAnimals: " + spectator.getAverageAgeOfDiedAnimals());
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

    public void addOutputObserver(IOutputObserver outputObserver) {
        this.outputObservers.add(outputObserver);
    }

    public void refreshFieldStatistics() {
        spectator.setFreeFields(worldMap.countFreeFields());
    }

    @Override
    public void onPositionChanged(Animal animal, Vector2D oldPosition, Vector2D newPosition) {

    }

    public void stop() {
        running = false;
        // you might also want to interrupt() the Thread that is
        // running this Runnable, too, or perhaps call:
        resume();
        // to unblock
    }

    public void pause() {
        // you may want to throw an IllegalStateException if !running
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }
}