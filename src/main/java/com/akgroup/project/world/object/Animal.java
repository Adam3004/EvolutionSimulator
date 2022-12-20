package com.akgroup.project.world.object;

import com.akgroup.project.engine.SimulationConfig;
import com.akgroup.project.util.Vector2D;

import java.util.Arrays;

public class Animal extends AbstractWorldElement {

    public static final int GENOME_LENGTH = 5;
    private int energy;
    private int activeGenIndex;
    private int[] genome;
    private int rotation = 0;

    public Animal(Vector2D vector2D, int[] genome) {
        this(vector2D);
        this.genome = genome;
    }

    public Animal(Vector2D vector2D) {
        super(vector2D);
        this.energy = 50;
        this.activeGenIndex = 0;
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

    public void loseEnergyOnMove(){
        this.loseEnergy(SimulationConfig.getInstance().getEnergyNeededToMove());
    }

    public void moveAt(Vector2D newPosition) {
        position = newPosition;
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