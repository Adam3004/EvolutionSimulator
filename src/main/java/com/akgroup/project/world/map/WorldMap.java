package com.akgroup.project.world.map;

import com.akgroup.project.ISimulationObserver;
import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.WorldConfig;
import com.akgroup.project.world.borders.MapBorders;
import com.akgroup.project.world.mutators.GenomeMutator;
import com.akgroup.project.world.object.*;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements IWorldMap {
    private final Vector2D lowerLeft, upperRight;
    private final Map<Vector2D, List<Animal>> animals;
    private final Map<Vector2D, Plant> plants;
    private final WorldConfig worldConfig;
    private final Config simulationConfig;
    private final List<ISimulationObserver> simulationObservers;

    public WorldMap(Config simulationConfig) {
        this.simulationObservers = new ArrayList<>();
        this.animals = new HashMap<>();
        this.plants = new HashMap<>();
        this.lowerLeft = new Vector2D(0, 0);
        this.upperRight = new Vector2D(simulationConfig.getValue(ConfigOption.WIDTH) - 1, simulationConfig.getValue(ConfigOption.HEIGHT) - 1);
        this.worldConfig = WorldConfig.fromConfig(simulationConfig);
        this.simulationConfig = simulationConfig;
    }

    public WorldConfig getWorldConfig() {
        return worldConfig;
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
                .collect(Collectors.toList());
    }

    public List<Plant> getPlants() {
        return plants.values().stream().toList();
    }

    public int getPlantsCount() {
        return plants.keySet().size();
    }

    public void rotateAndMove(Animal animal) {
        rotateAnimal(animal);
        moveAnimal(animal);
        animal.loseEnergyOnMove();
    }

    private void rotateAnimal(Animal animal) {
        int genGap = worldConfig.behaviour().getAnimalRotation();
        animal.rotate(genGap);
    }

    public void removeObject(IWorldElement object) {
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

    public void multiplyAnimals() {
        List<List<Animal>> animalsToMultiply = animals.values().stream()
                .filter(animalList -> animalList.size() > 1)
                .toList();
        for (List<Animal> animalsToReproduction : animalsToMultiply) {
            List<Animal> goodAnimals = animalsToReproduction.stream().sorted(Comparator.comparing(Animal::getEnergy).reversed()).filter(Animal::haveEnoughEnergy).collect(Collectors.toList());
            if (goodAnimals.size() < 2) {
                continue;
            }
            Animal bestAnimal = findBestAnimal(goodAnimals);
            goodAnimals.remove(bestAnimal);
            Animal secondBestAnimal = findBestAnimal(goodAnimals);
            goodAnimals.add(bestAnimal);
            createKid(bestAnimal, secondBestAnimal);
            bestAnimal.increaseNumberOfKids();
            secondBestAnimal.increaseNumberOfKids();
        }
    }

//    private void animalsReproduction(Vector2D vector2D) {
//        List<Animal> animalsOnVector2D = sortAnimalsByEnergyR(animals.getListOfPossibilities(vector2D));
//        if (animalsOnVector2D.size() < 2) {
//            return;
//        }
//        createKid(animalsOnVector2D, vector2D);
//    }

    private List<Animal> sortAnimalsByEnergyR(List<Animal> inputList) {
        return inputList.stream()
                .sorted(Comparator.comparing(Animal::getEnergy).reversed())
                .toList();
    }

    private void createKid(Animal mum, Animal dad) {
        int energyPerParent = simulationConfig.getValue(ConfigOption.ANIMAL_ENERGY_FOR_CHILD);
        mum.loseEnergy(energyPerParent);
        dad.loseEnergy(energyPerParent);
        int[] newGenome = NumberGenerator.createNewGenome(dad, mum);
        int minMutations = simulationConfig.getValue(ConfigOption.MINIMAL_MUTATION);
        int maxMutations = simulationConfig.getValue(ConfigOption.MAXIMAL_MUTATION);
        getGenomeMutator().mutate(newGenome, minMutations, maxMutations);
        Animal kid = new Animal(mum.getPosition(), 2 * energyPerParent, newGenome);
        placeObject(kid);
    }

//    private void createKid(List<Animal> animalsOnVector2D, Vector2D vector2D) {
//        Animal mum = animalsOnVector2D.getListOfPossibilities(0);
//        Animal dad = animalsOnVector2D.getListOfPossibilities(1);
//        int energyPerParent = SimulationConfig.getInstance().getMultiplicationEnergyLose();
//        prepareEnergy(mum, dad, energyPerParent);
//        Animal kid = new Animal(vector2D, 2 * energyPerParent, NumberGenerator.createNewGenome(dad, mum));
//        placeObject(kid);
//    }

    @Override
    public Vector2D getLowerLeft() {
        return lowerLeft;
    }
    @Override
    public Vector2D getUpperRight() {
        return upperRight;
    }

    private MapBorders getMapBorders() {
        return worldConfig.mapBorders();
    }

    private GenomeMutator getGenomeMutator() {
        return worldConfig.mutator();
    }

    public void eatPlants() {
        List<Vector2D> tmpList = new ArrayList<>();
        for (Vector2D plantPosition : plants.keySet()) {
            if (animals.get(plantPosition) == null) {
                continue;
            }
            Animal bestAnimal = findBestAnimal(animals.get(plantPosition));
            bestAnimal.addEnergy(simulationConfig.getValue(ConfigOption.PLANT_ENERGY));
            tmpList.add(plantPosition);
        }
        for (Vector2D vector2D : tmpList) {
            plants.remove(vector2D);
        }
    }

    private Animal findBestAnimal(List<Animal> chosenAnimals) {
        List<Animal> chosenSortedAnimals = sortAnimalsByEnergyR(chosenAnimals);
        int maxEnergy = chosenSortedAnimals.get(0).getEnergy();
        List<Animal> biggestEnergyAnimals = chosenSortedAnimals.stream()
                .filter(animal -> animal.getEnergy() == maxEnergy)
                .toList();
        if (biggestEnergyAnimals.size() == 1) {
            return biggestEnergyAnimals.get(0);
        }
        biggestEnergyAnimals = biggestEnergyAnimals.stream()
                .sorted(Comparator.comparing(Animal::getAge).reversed())
                .toList();
        int maxAge = biggestEnergyAnimals.get(0).getAge();
        List<Animal> oldestAnimals = biggestEnergyAnimals.stream()
                .filter(animal -> animal.getAge() == maxAge)
                .toList();
        if (oldestAnimals.size() == 1) {
            return oldestAnimals.get(0);
        }
        oldestAnimals = oldestAnimals.stream()
                .sorted(Comparator.comparing(Animal::getNumberOfKids).reversed())
                .toList();
        return oldestAnimals.get(0);
    }

    public void addSimulationObserver(ISimulationObserver output){
        this.simulationObservers.add(output);
    }

    public void trySummonNewPlant() {
        Vector2D proposedVector = worldConfig.planter().findNewVector();
        placeObject(new Plant(proposedVector));
    }
}
