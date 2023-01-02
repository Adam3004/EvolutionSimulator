package com.akgroup.project.world.object;

import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;

import java.util.Arrays;

public class Animal extends AbstractWorldElement {
    private int energy;
    private int activeGenIndex;
    private final int[] genome;
    private int rotation;
    private int age;
    private int numberOfKids;
    private int eatenPlants;
    private int lastDay;

    public Animal(Vector2D vector2D, int[] genome) {
        this(vector2D, 10, genome);
    }

    public Animal(Vector2D vector2D, int energy, int[] genome) {
        super(vector2D);
        this.energy = energy;
        this.genome = genome;
        this.age = 0;
        this.numberOfKids = 0;
        this.eatenPlants = 0;
        this.activeGenIndex = generateFirstActiveGenomeIndex();
        this.rotation = generateStartingRotation();
        this.lastDay = -1;
    }

    public int[] getGenome() {
        return genome;
    }

    public int getEnergy() {
        return energy;
    }

    public int getRotation() {
        return rotation;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public int getEatenPlants() {
        return eatenPlants;
    }

    public int getLastDay() {
        return lastDay;
    }

    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }

    public void addEnergy(int e) {
        this.energy += e;
        eatenPlants += 1;
    }

    public int getActiveGenIndex() {
        return activeGenIndex;
    }

    @Override
    public TypeEnum getType() {
        return TypeEnum.ANIMAL;
    }

    public void rotate(int nextGen) {
        rotation = (rotation + genome[activeGenIndex]) % 8;
        activeGenIndex = (activeGenIndex + nextGen) % genome.length;
    }

    public void loseEnergy(int energyToLose) {
        this.energy -= energyToLose;
    }

    public void loseEnergyOnMove() {
        this.loseEnergy(1);
    }

    public void moveAt(Vector2D newPosition) {
        position = newPosition;
    }

    public void increaseAge() {
        age += 1;
    }

    public void increaseNumberOfKids() {
        numberOfKids += 1;
    }

    public int getAge() {
        return age;
    }

    public int getNumberOfKids() {
        return numberOfKids;
    }

    private int generateStartingRotation() {
        return NumberGenerator.generateNextInt(0, 7);
    }

    private int generateFirstActiveGenomeIndex() {
        return NumberGenerator.generateNextInt(0, genome.length - 1);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "energy=" + energy +
                ", activeGenIndex=" + activeGenIndex +
                ", genome=" + Arrays.toString(genome) +
                ", rotation=" + rotation +
                ", position=" + position +
                '}';
    }
}