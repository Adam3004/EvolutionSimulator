package com.akgroup.project.engine;

import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;

import java.util.List;

public class Engine {
    private final IWorldMap worldMap;

    public Engine(IWorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void run(){
        List<Animal> allAnimals = worldMap.getAllAnimals();
    }
}
