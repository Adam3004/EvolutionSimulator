package com.akgroup.project;

import com.akgroup.project.engine.Engine;
import com.akgroup.project.engine.SimulationConfig;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.WorldConfiguration;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;

public class Main {
    public static void main(String[] args) {
        WorldConfiguration configuration = WorldConfiguration.defaultConfiguration();
        WorldMap worldMap = new WorldMap(10, 10, configuration);
        Animal animal = new Animal(new Vector2D(1, 1), new int[]{0, 5, 2, 4, 7});
        worldMap.placeObject(animal);
        worldMap.placeObject(new Plant(new Vector2D(9, 2)));
        SimulationConfig.init();
        Engine engine = new Engine(worldMap);
        engine.run();
    }

//    równik z trupami, albo kula z czymś
}