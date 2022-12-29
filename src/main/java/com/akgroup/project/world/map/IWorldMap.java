package com.akgroup.project.world.map;

import com.akgroup.project.IPositionChangedObserver;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.IWorldElement;
import com.akgroup.project.world.object.Plant;

import java.util.List;

public interface IWorldMap {

    /** Initializing method*/
    void init();
    /** Places instance of IWorldElement on the map. If everything went well then returns true, otherwise false. */
    boolean placeObject(IWorldElement element);

    /** Returns all objects on the map which have given position Vector2D*/
    List<IWorldElement> getObjectsAt(Vector2D vector2D);

    /** Returns all Animals on the map*/
    List<Animal> getAllAnimals();

    /** Returns bottom left point of the map as a Vector2D */
    Vector2D getLowerLeft();

    /** Returns top right point of the map as a Vector2D*/
    Vector2D getUpperRight();

    /** Removes IWorldElement from map*/
    void removeObject(IWorldElement worldElement);

    /** Adds Observer to map */
    void addPositionChangedObserver(IPositionChangedObserver observer);

    /** Returns all Plants on the map*/
    List<Plant> getPlants();
}
