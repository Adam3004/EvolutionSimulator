package com.akgroup.project.world.map;

import com.akgroup.project.engine.SimulationConfig;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.WorldConfiguration;
import com.akgroup.project.world.borders.MapBorders;
import com.akgroup.project.world.object.*;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements IWorldMap {
    private final Vector2D lowerLeft, upperRight;
    private final Map<Vector2D, List<Animal>> animals;
    private final Map<Vector2D, Plant> plants;
    private final WorldConfiguration configuration;

    public WorldMap(int width, int height, WorldConfiguration configuration) {
        this.animals = new HashMap<>();
        this.plants = new HashMap<>();
        this.lowerLeft = new Vector2D(0, 0);
        this.upperRight = new Vector2D(width, height);
        this.configuration = configuration;
        this.configuration.setWorldMap(this);
    }

    @Override
    public boolean placeObject(IWorldElement element) {
        Vector2D position = element.getPosition();
        if (!position.follows(lowerLeft) || !position.precedes(upperRight)) return false;
        if (element.getType().equals(TypeEnum.ANIMAL)) {
            if (!animals.containsKey(position)) {
                animals.put(position, new ArrayList<>());
            }
            animals.get(position).add((Animal) element);
        } else if (!plants.containsKey(position)) {
            plants.put(position, (Plant) element);
        } else {
            return false;
        }

        return true;
    }

    @Override
    public List<IWorldElement> getObjectsAt(Vector2D vector2D) {
        if (plants.containsKey(vector2D)) return Collections.singletonList(plants.get(vector2D));
        if (!animals.containsKey(vector2D)) return new ArrayList<>();
        return new ArrayList<>(animals.get(vector2D));
    }

    @Override
    public List<Animal> getAllAnimals() {
        return animals.values().stream()
                .flatMap(List::stream)
                .filter(element -> element.getType() == TypeEnum.ANIMAL)
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
        if (object.getType().equals(TypeEnum.ANIMAL)) {
            animals.get(object.getPosition()).remove((Animal) object);
            if (animals.get(object.getPosition()).size() == 0) {
                animals.remove(object.getPosition());
            }
        } else {
            plants.remove(object.getPosition());
        }
    }

    /**
     * Finds unit vector of animal rotation.
     * Calculates position after moving by unit vector.
     * If position is outside the map then calculates position depending on MapBorder implementation.
     * Updates animal position and stored animals in HashMap.
     */
    private void moveAnimal(Animal animal) {
        Vector2D moveVector = Rotation.getVectorFromRotation(animal.getRotation());
        Vector2D newMapPosition = animal.getPosition().add(moveVector);
        newMapPosition = getMapBorders().repairAnimalPosition(animal, newMapPosition);
        removeObject(animal);
        animal.moveAt(newMapPosition);
        placeObject(animal);
    }

    public void animalsReproduction(Vector2D vector2D) {
        List<Animal> animalsOnVector2D = animals.get(vector2D).stream()
                .filter(Animal::haveEnoughEnergy)
                .sorted(Comparator.comparing(Animal::getEnergy).reversed())
                .toList();
        if (animalsOnVector2D.size() < 2) {
            return;
        }
        createKid(animalsOnVector2D, vector2D);
    }

    private void createKid(List<Animal> animalsOnVector2D, Vector2D vector2D) {
        Animal mum = animalsOnVector2D.get(0);
        Animal dad = animalsOnVector2D.get(1);
        int energyPerParent = SimulationConfig.getInstance().getMultiplicationEnergyLose();
        prepareEnergy(mum, dad, energyPerParent);
        Animal kid = new Animal(vector2D, 2 * energyPerParent, NumberGenerator.createNewGenome(dad, mum));
        placeObject(kid);
    }

    private void prepareEnergy(Animal mum, Animal dad, int energyPerParent) {
        mum.loseEnergy(energyPerParent);
        dad.loseEnergy(energyPerParent);
    }

    @Override
    public Vector2D getLowerLeft() {
        return lowerLeft;
    }

    @Override
    public Vector2D getUpperRight() {
        return upperRight;
    }

    private MapBorders getMapBorders() {
        return configuration.mapBorders();
    }
}
