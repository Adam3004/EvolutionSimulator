package com.akgroup.project.world.object;

import com.akgroup.project.IPositionChangedObserver;
import com.akgroup.project.util.Vector2D;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to store animals on map. Every Vector2D has List of Animals which has the same position
 */
public class AnimalsContainer implements IPositionChangedObserver {
    private final Map<Vector2D, List<Animal>> animals;

    public AnimalsContainer() {
        this.animals = new HashMap<>();
    }

    public void addAnimal(Vector2D position, Animal element) {
        if (!animals.containsKey(position)) {
            animals.put(position, new ArrayList<>());
        }
        animals.get(position).add(element);
    }

    public int countFilledFields() {
        return animals.size();
    }

    public List<IWorldElement> getWorldElementsAt(Vector2D vector2D) {
        return getAnimalsAt(vector2D)
                .stream()
                .map(animal -> (IWorldElement) animal)
                .collect(Collectors.toList());
    }

    public List<Animal> getAnimalsAt(Vector2D vector2D) {
        return animals.getOrDefault(vector2D, new ArrayList<>());
    }

    public List<Animal> getAllAnimals() {
        return animals.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public void removeAnimalAt(Animal animal, Vector2D position) {
        animals.get(position).remove(animal);
        if (animals.get(position).size() == 0) {
            animals.remove(position);
        }
    }

    public void removeAnimal(Animal animal) {
        removeAnimalAt(animal, animal.getPosition());
    }

    private List<Animal> sortAnimalsByEnergyR(List<Animal> inputList) {
        return inputList.stream()
                .sorted(Comparator.comparing(Animal::getEnergy).reversed())
                .toList();
    }

    public Animal findBestAnimalAt(Vector2D vector2D) {
        return findBestAnimalFrom(getAnimalsAt(vector2D));
    }

    public Animal findBestAnimalFrom(List<Animal> chosenAnimals) {
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

    public Stream<List<Animal>> getAnimalGroups() {
        return animals.values().stream().filter(list -> list.size() > 1);
    }

    @Override
    public void onPositionChanged(Animal animal, Vector2D oldPosition, Vector2D newPosition) {
        removeAnimalAt(animal, oldPosition);
        addAnimal(newPosition, animal);
    }

    public boolean hasAnimalAt(Vector2D plantPosition) {
        return animals.containsKey(plantPosition);
    }
}
