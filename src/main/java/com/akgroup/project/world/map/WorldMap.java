package com.akgroup.project.world.map;

import com.akgroup.project.engine.SimulationConfig;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.WorldConfiguration;
import com.akgroup.project.world.borders.MapBorders;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.IWorldElement;
import com.akgroup.project.world.object.Rotation;
import com.akgroup.project.world.object.TypeEnum;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements IWorldMap {
    private final Vector2D lowerLeft, upperRight;
    private final Map<Vector2D, List<IWorldElement>> mapObjects;
    private final MapBorders mapBorders;

    public WorldMap(int width, int height, WorldConfiguration configuration) {
        this.mapObjects = new HashMap<>();
        this.lowerLeft = new Vector2D(0, 0);
        this.upperRight = new Vector2D(width, height);
        this.mapBorders = configuration.mapBorders();
        this.mapBorders.setWorldMap(this);
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

    public void rotateAndMove(Animal animal) {
        rotateAnimal(animal);
        moveAnimal(animal);
        animal.loseEnergyOnMove();
    }

    private void rotateAnimal(Animal animal) {
        int genGap = 1;
        if (!SimulationConfig.getInstance().isFullPredestination()) {
            if (NumberGenerator.isTrue(20)) {
                genGap = NumberGenerator.generateNextInt(1, Animal.GENOME_LENGTH);
            }
        }
        animal.rotate(genGap);
    }

    private void removeObject(IWorldElement object) {
        mapObjects.get(object.getPosition()).remove(object);
        if (mapObjects.get(object.getPosition()).size() == 0) {
            mapObjects.remove(object.getPosition());
        }
    }

    private void moveAnimal(Animal animal) {
        Vector2D moveVector = Rotation.getVectorFromRotation(animal.getRotation());
        Vector2D newMapPosition = animal.getPosition().add(moveVector);
        removeObject(animal);
        newMapPosition = mapBorders.getPositionOutOfMap(newMapPosition);
        animal.move(newMapPosition);
        placeObject(animal);
    }

    private void animalsReproduction(Vector2D vector2D) {
        List<IWorldElement> mapElementsOnVector2D = mapObjects.get(vector2D);
        if (mapElementsOnVector2D.size() < 2) {
            return;
        }
        List<Animal> animalsOnVector2D = mapElementsOnVector2D.stream()
                .map(object -> (Animal) object)
//                .filter(Animal)
                .sorted(Comparator.comparing(Animal::getEnergy).reversed())
                .toList();
        Animal mum = animalsOnVector2D.get(0);
        Animal dad = animalsOnVector2D.get(1);


    }

    private void prepareEnergy(){

    }

    @Override
    public Vector2D getLowerLeft() {
        return lowerLeft;
    }

    @Override
    public Vector2D getUpperRight() {
        return upperRight;
    }
}
