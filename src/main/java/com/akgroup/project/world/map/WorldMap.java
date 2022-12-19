package com.akgroup.project.world.map;

import com.akgroup.project.engine.SimulationConfig;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.IWorldElement;
import com.akgroup.project.world.object.TypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldMap implements IWorldMap {
    private final Vector2D lowerLeft, upperRight;
    private final Map<Vector2D, List<IWorldElement>> mapObjects;

    public WorldMap(int width, int height) {
        this.mapObjects = new HashMap<>();
        this.lowerLeft = new Vector2D(0, 0);
        this.upperRight = new Vector2D(width, height);
    }

    @Override
    public boolean placeObject(IWorldElement element) {
        Vector2D position = element.getPosition();
        if (!position.follows(lowerLeft) || !position.precedes(upperRight)) return false;
        if (!mapObjects.containsKey(position)) {
            mapObjects.put(position, new ArrayList<>());
        }
        mapObjects.get(position).add(element);
        return true;
    }

    @Override
    public List<IWorldElement> getObjectsAt(Vector2D vector2D) {
        if (!mapObjects.containsKey(vector2D)) return new ArrayList<>();
        return mapObjects.get(vector2D);
    }

    @Override
    public List<Animal> getAllAnimals() {
        return mapObjects.values().stream()
                .flatMap(List::stream)
                .filter(element -> element.getType() == TypeEnum.ANIMAL)
                .map(element -> (Animal) element)
                .collect(Collectors.toList());
    }

    public void rotateAnimal(Animal animal) {
        int genGap = 1;
        if (!SimulationConfig.getInstance().isFullPredestination()) {
            if (NumberGenerator.isTrue(20)) {
                genGap = NumberGenerator.generateNextInt(1, Animal.GENOME_LENGTH);
            }
        }
        animal.rotate(genGap);
    }

    public void moveAnimal(Animal animal) {
        //TODO implement
    }
}
