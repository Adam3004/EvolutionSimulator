package com.akgroup.project.world.map;

import com.akgroup.project.IPositionChangedObserver;
import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.engine.statistics.StatSpectator;
import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.WorldConfig;
import com.akgroup.project.world.object.*;
import com.akgroup.project.world.planter.Planter;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements IWorldMap {
    private final Vector2D lowerLeft, upperRight;
    private final AnimalsContainer animalsContainer;
    private final Map<Vector2D, Plant> plants;
    private final WorldConfig worldConfig;
    private final Config simulationConfig;
    private final List<IPositionChangedObserver> positionChangedObservers;
    private final StatSpectator spectator;

    public WorldMap(Config simulationConfig, StatSpectator spectator) {
        this.positionChangedObservers = new ArrayList<>();
        this.animalsContainer = new AnimalsContainer();
        this.positionChangedObservers.add(this.animalsContainer);
        this.plants = new HashMap<>();
        this.lowerLeft = new Vector2D(0, 0);
        this.upperRight = new Vector2D(simulationConfig.getValue(ConfigOption.WIDTH) - 1, simulationConfig.getValue(ConfigOption.HEIGHT) - 1);
        this.worldConfig = WorldConfig.fromConfig(simulationConfig);
        this.simulationConfig = simulationConfig;
        this.spectator = spectator;
    }

    public List<Plant> getPlants() {
        return plants.values().stream().toList();
    }

    public Planter getPlanter() {
        return worldConfig.planter();
    }

    public int getPlantsCount() {
        return plants.keySet().size();
    }

    public void rotateAndMove(Animal animal) {
        Vector2D oldPosition = animal.getPosition();
        rotateAnimal(animal);
        moveAnimal(animal);
        animal.loseEnergyOnMove();
        notifyOnPositionChanged(animal, oldPosition, animal.getPosition());
    }

    private void rotateAnimal(Animal animal) {
        int genGap = worldConfig.behaviour().getAnimalRotation(simulationConfig.getValue(ConfigOption.GENOME_LENGTH));
        animal.rotate(genGap);
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
        newMapPosition = worldConfig.mapBorders().repairAnimalPosition(animal, newMapPosition);
        animal.moveAt(newMapPosition);
    }

    public List<Animal> filterAnimalsToReproduction(List<Animal> list) {
        return list.stream()
                .filter(Animal::haveEnoughEnergy)
                .sorted(Comparator.comparing(Animal::getEnergy).reversed())
                .collect(Collectors.toList());
    }

    private void multiply(List<Animal> list) {
        Animal bestAnimal = animalsContainer.findBestAnimalFrom(list);
        list.remove(bestAnimal);
        Animal secondBestAnimal = animalsContainer.findBestAnimalFrom(list);
        list.add(bestAnimal);
        createKid(bestAnimal, secondBestAnimal);
        bestAnimal.increaseNumberOfKids();
        secondBestAnimal.increaseNumberOfKids();
    }

    public void multiplyAnimals() {
        animalsContainer.getAnimalGroups()
                .map(this::filterAnimalsToReproduction)
                .filter(group -> group.size() > 1)
                .forEach(this::multiply);
    }

//    private void animalsReproduction(Vector2D vector2D) {
//        List<Animal> animalsOnVector2D = sortAnimalsByEnergyR(animals.getListOfPossibilities(vector2D));
//        if (animalsOnVector2D.size() < 2) {
//            return;
//        }
//        createKid(animalsOnVector2D, vector2D);
//    }

    private void createKid(Animal mum, Animal dad) {
        int energyPerParent = simulationConfig.getValue(ConfigOption.ANIMAL_ENERGY_FOR_CHILD);
        mum.loseEnergy(energyPerParent);
        dad.loseEnergy(energyPerParent);
        int genomeLen = simulationConfig.getValue(ConfigOption.GENOME_LENGTH);
        int[] newGenome = NumberGenerator.createNewGenome(dad, mum, genomeLen);
        int minMutations = simulationConfig.getValue(ConfigOption.MINIMAL_MUTATION);
        int maxMutations = simulationConfig.getValue(ConfigOption.MAXIMAL_MUTATION);
        worldConfig.mutator().mutate(newGenome, minMutations, maxMutations, genomeLen);
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


    public void eatPlants() {
        List<Vector2D> tmpList = new ArrayList<>();
        for (Vector2D plantPosition : plants.keySet()) {
            if (!animalsContainer.hasAnimalAt(plantPosition)) continue;
            Animal bestAnimal = animalsContainer.findBestAnimalAt(plantPosition);
            if (bestAnimal == null) continue;
            bestAnimal.addEnergy(simulationConfig.getValue(ConfigOption.PLANT_ENERGY));
            tmpList.add(plantPosition);
        }
        spectator.plantEaten(tmpList.size());
        for (Vector2D vector2D : tmpList) {
            plants.remove(vector2D);
            worldConfig.planter().eatPlantOnField(vector2D);
        }
    }

    private void notifyOnPositionChanged(Animal animal, Vector2D oldPosition, Vector2D newPosition) {
        positionChangedObservers.forEach(obs -> obs.onPositionChanged(animal, oldPosition, newPosition));
    }

    public void trySummonNewPlant() {
        Vector2D proposedVector = worldConfig.planter().findNewVectorToPlant();
        placeObject(new Plant(proposedVector));
        spectator.newPlantRespawned();
    }

//    private boolean isFieldFree(Vector2D vector2D) {
//        return animalsContainer.getAnimalsAt(vector2D).isEmpty() && !plants.containsKey(vector2D);
//    }

    public int countFreeFields() {
        Set<Vector2D> plantsFields = plants.keySet();
        int sameFields = plantsFields.stream()
                .filter(field -> !animalsContainer.getAnimalsAt(field).isEmpty())
                .toList()
                .size();
        return simulationConfig.getMapArea() - (plantsFields.size() + animalsContainer.countFilledFields() - sameFields);
    }

    @Override
    public Collection<Plant> getPlantsCollection() {
        return plants.values();
    }

    @Override
    public List<List<Animal>> getAnimalLists() {
        return animalsContainer.getAnimalLists();
    }

    @Override
    public void init() {
        worldConfig.planter().init();
    }

    @Override
    public boolean placeObject(IWorldElement element) {
        Vector2D position = element.getPosition();
        if (!position.follows(lowerLeft) || !position.precedes(upperRight)) return false;
        if (element.getType().equals(TypeEnum.ANIMAL)) {
            Animal newAnimal = (Animal) element;
            spectator.newAnimalRespawned(newAnimal);
            animalsContainer.addAnimal(position, (Animal) element);
        } else if (!plants.containsKey(position)) {
            plants.put(position, (Plant) element);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<IWorldElement> getObjectsAt(Vector2D vector2D) {
        List<IWorldElement> elements = animalsContainer.getWorldElementsAt(vector2D);
        if (plants.containsKey(vector2D)) elements.add(plants.get(vector2D));
        return elements;
    }

    @Override
    public List<Animal> getAllAnimals() {
        return animalsContainer.getAllAnimals();
    }

    @Override
    public void removeObject(IWorldElement object) {
        if (object.getType().equals(TypeEnum.ANIMAL)) {
            Animal dyingAnimal = (Animal) object;
            animalsContainer.removeAnimal(dyingAnimal);
            spectator.animalDying(dyingAnimal);
        } else {
            plants.remove(object.getPosition());
        }
    }

    @Override
    public Vector2D getLowerLeft() {
        return lowerLeft;
    }

    @Override
    public Vector2D getUpperRight() {
        return upperRight;
    }

    @Override
    public void addPositionChangedObserver(IPositionChangedObserver observer) {
        this.positionChangedObservers.add(observer);
    }
}
