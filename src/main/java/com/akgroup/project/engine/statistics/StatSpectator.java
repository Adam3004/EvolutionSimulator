package com.akgroup.project.engine.statistics;

import com.akgroup.project.world.object.Animal;

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

    public void newAnimalRespawned(Animal animal) {
        numberOfAliveAnimals++;
        addEnergy(animal);
        addNewGenomeToMap(animal);
    }

    private void addNewGenomeToMap(Animal animal) {
        if (!currGenotypes.containsKey(animal.getGenome())) {
            currGenotypes.put(animal.getGenome(), 1);
        }
        setNewAverageGenome();
    }

    private void removeGenomeFromMap(Animal animal) {
        currGenotypes.remove(animal.getGenome());
        setNewAverageGenome();
    }

    private void setNewAverageGenome() {
        var max = currGenotypes.entrySet().stream().max(Map.Entry.comparingByValue());
        if (max.isPresent())
            mostPopularGenotype = max.get().getKey();
        else
            mostPopularGenotype = new int[]{};
    }

    private void addEnergy(Animal animal) {
        sumOfEnergy += animal.getEnergy();
        setNewAverageEnergy();
    }

    private void removeEnergy(Animal animal) {
        sumOfEnergy -= animal.getEnergy();
        setNewAverageEnergy();
    }

    private void setNewAverageEnergy() {
        averageEnergy = 0;
        if (numberOfAliveAnimals > 0) {
            averageEnergy = Math.toIntExact(Math.round((float) sumOfEnergy / numberOfAliveAnimals));
        }
    }

    private void addNewAgeOfDiedAnimal(Animal animal) {
        sumOfAgesOfDiedAnimals += animal.getAge();
        setNewAverageAgeOfDiedAnimals();
    }

    private void setNewAverageAgeOfDiedAnimals() {
        averageAgeOfDiedAnimals = 0;
        if (diedAnimals > 0) {
            averageAgeOfDiedAnimals = Math.toIntExact(Math.round((float) sumOfAgesOfDiedAnimals / diedAnimals));
        }
    }

    public void animalDying(Animal animal) {
        numberOfAliveAnimals--;
        diedAnimals++;
        removeGenomeFromMap(animal);
        removeEnergy(animal);
        addNewAgeOfDiedAnimal(animal);
    }

    public void newPlantRespawned() {
        numberOfPlants++;
    }

    public void plantEaten(int number) {
        numberOfPlants -= number;
    }

    public void setFreeFields(int newFreeFields) {
        freeFields = newFreeFields;
    }


}
