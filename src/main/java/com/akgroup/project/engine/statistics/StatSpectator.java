package com.akgroup.project.engine.statistics;

import com.akgroup.project.world.object.Animal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class StatSpectator {
    private int numberOfAliveAnimals;
    private int numberOfPlants;
    private int freeFields;
    private int[] mostPopularGenotype;
    private int averageEnergy;
    private int sumOfEnergy;
    private int averageAgeOfDiedAnimals;
    private int sumOfAgesOfDiedAnimals;
    private int diedAnimals;
    private final int numberOfAllFields;
    private final Map<int[], Integer> currGenotypes;

    public StatSpectator(int area) {
        this.numberOfAliveAnimals = 0;
        this.numberOfPlants = 0;
        this.freeFields = area;
        this.mostPopularGenotype = new int[]{};
        this.averageEnergy = 0;
        this.sumOfEnergy = 0;
        this.averageAgeOfDiedAnimals = 0;
        this.sumOfAgesOfDiedAnimals = 0;
        this.diedAnimals = 0;
        this.numberOfAllFields = area;
        this.currGenotypes = new HashMap<>();
    }

    public int getNumberOfAliveAnimals() {
        return numberOfAliveAnimals;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public int getFreeFields() {
        return freeFields;
    }

    public int[] getMostPopularGenotype() {
        return mostPopularGenotype;
    }

    public int getAverageEnergy() {
        return averageEnergy;
    }

    public int getAverageAgeOfDiedAnimals() {
        return averageAgeOfDiedAnimals;
    }

    public void newAnimalRespawned(Animal animal, boolean isFieldFree) {
        numberOfAliveAnimals++;
        if (isFieldFree) {
            freeFields -= 1;
        }
        setNewAverageEnergy(animal);
        addNewGenomeToMap(animal);
        setNewAverageGenome();
    }

    private void addNewGenomeToMap(Animal animal) {
        if (!currGenotypes.containsKey(animal.getGenome())) {
            currGenotypes.put(animal.getGenome(), 1);
        }
    }

    private void setNewAverageGenome() {
        mostPopularGenotype = currGenotypes.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    private void setNewAverageEnergy(Animal animal) {
        sumOfEnergy += animal.getEnergy();
        averageEnergy = Math.toIntExact(Math.round(sumOfEnergy / numberOfAliveAnimals));
    }


    public void animalDying() {

    }


}
