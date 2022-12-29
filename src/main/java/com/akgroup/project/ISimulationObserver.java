package com.akgroup.project;

import com.akgroup.project.util.Vector2D;

public interface ISimulationObserver {
    void onPositionChanged(Vector2D oldPosition, Vector2D newPosition);
    void onAnimalDead(Vector2D position);
    void onAnimalFoundPlant(Vector2D position);

    void onAnimalFoundAnimals(Vector2D position);
}
