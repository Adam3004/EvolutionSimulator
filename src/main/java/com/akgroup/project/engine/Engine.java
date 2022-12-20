package com.akgroup.project.engine;

import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;

import java.util.List;

public class Engine {
    private final IWorldMap worldMap;

    public Engine(IWorldMap worldMap) {
        this.worldMap = worldMap;
    }
    public void run() {
        while (true){
            removeDeadAnimals();
            moveAnimals();
            eatPlants();
            multiplicationOfAnimals();
            summonNewPlants();
        }
    }

    private void removeDeadAnimals() {
        // find dead animals where energy is lower than  SimulationConfig.getInstance().getEnergyNeededToMove()
        // remove them from map
    }

    private void moveAnimals() {
        // move all animals on the map
    }

    private void eatPlants() {
        // remove plants where on their field is any animal
    }

    private void multiplicationOfAnimals() {
        // find fields with more than one animal
        // choose two of them with maximal energy
        // generate genome based on parents genomes
        // create new animal object with new genome
    }

    private void summonNewPlants() {
        // ??
    }
}
