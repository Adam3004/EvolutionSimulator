package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;

public class Animal extends AbstractWorldElement {

    public static final int GENOME_LENGTH = 5;
    private int energy;
    private int activeGenIndex;
    private int[] genome;
    private int rotation = 0;

    public Animal(Vector2D vector2D, int[] genome){
        this(vector2D);
        this.genome = genome;
    }

    public Animal(Vector2D vector2D) {
        super(vector2D);
        this.energy = 0;
        this.activeGenIndex = 0;
    }

    public int[] getGenome() {
        return genome;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isDead(){
        return energy == 0;
    }

    public void addEnergy(int e){
        this.energy += e;
    }

    @Override
    public TypeEnum getType() {
        return TypeEnum.ANIMAL;
    }

    public void rotate(int nextGen) {
        activeGenIndex = (activeGenIndex + nextGen) % GENOME_LENGTH;
        rotation = (rotation + genome[activeGenIndex]) % 8;
    }
}