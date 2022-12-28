package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;

import java.util.Arrays;

public class Animal extends AbstractWorldElement {
    public static final int GENOME_LENGTH = 5;
    private int energy;
    private int activeGenIndex;
    private final int[] genome;
    private int rotation;
    private int age;
    private int numberOfKids;

    public Animal(Vector2D vector2D, int[] genome) {
        this(vector2D, 10, genome);

    }

    public Animal(Vector2D vector2D, int energy, int[] genome) {
        super(vector2D);
        this.energy = energy;
        this.activeGenIndex = 0;
        this.genome = genome;
        this.age = 0;
        this.numberOfKids = 0;
        this.rotation = 0;
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

    public void addEnergy(int e) {
        this.energy += e;
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
        activeGenIndex = (activeGenIndex + nextGen) % GENOME_LENGTH;
    }

    public void loseEnergy(int energyToLose) {
        this.energy -= energyToLose;
    }

    //TODO zmienić statyczną wartość na przekazywaną zwierzęciu w konstruktorze albo zrobić to w mapie
    public boolean haveEnoughEnergy() {
        return this.energy >= 4;
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