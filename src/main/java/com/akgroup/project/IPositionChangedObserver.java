package com.akgroup.project;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;

public interface IPositionChangedObserver {
    void onPositionChanged(Animal animal, Vector2D oldPosition, Vector2D newPosition);
}
