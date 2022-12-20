package com.akgroup.project;

import com.akgroup.project.engine.SimulationConfig;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.WorldConfiguration;
import com.akgroup.project.world.borders.HellPortalBorders;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.object.Animal;

public class Main {
    public static void main(String[] args) {
        WorldConfiguration configuration = new WorldConfiguration(new HellPortalBorders());
        IWorldMap worldMap = new WorldMap(100, 100, configuration);
        Animal animal = new Animal(new Vector2D(1, 1), new int[]{0, 5, 2, 4});
        worldMap.placeObject(animal);
        SimulationConfig.init();
    }

//    równik z trupami, albo kula z czymś
}