package com.akgroup.project;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;

public interface IOutputObserver {
    void init(IWorldMap worldMap);
    void onAnimalSummoned(Vector2D position);
    void onPositionChanged(Vector2D oldPosition, Vector2D newPosition);
    void onPlantSummoned(Vector2D position);
    void onPlantWasEaten(Vector2D position);
    void renderFrame();
}
