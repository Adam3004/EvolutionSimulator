package com.akgroup.project.world.object;

import com.akgroup.project.util.Vector2D;

public class Animal extends AbstractWorldElement {
    private int energy;
    private int activeGen;
    private int[] genome;

    public Animal(Vector2D vector2D, int[] genome){
        this(vector2D);
        this.genome = genome;
    }

    public Animal(Vector2D vector2D) {
        super(vector2D);
        this.energy = 0;
        this.activeGen = 0;
    }

    public int getActualGen() {
        int gen = genome[activeGen];
        activeGen = (activeGen + 1) % 8;
        energy -= 1;
        return gen;
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
}