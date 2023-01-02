package com.akgroup.project;

import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;

import java.util.List;

/** Class responsible for managing Simulation output.*/
public interface IOutputObserver {
    /** Initialize output depending on IWorldMap instance.
     * In this method put all necessary things which must be done before running Simulation.*/
    void init(IWorldMap worldMap);
    /** Function is called every simulation cycle (output frame).*/
    void renderFrame(List<Animal> animals, List<Plant> plants);
}
