package com.akgroup.project.util;

import com.akgroup.project.world.object.Animal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class NumberGenerator {
    private static final Random random = new Random();


    public static int generateNextInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean isTrue(int truePercentage) {
        return generateNextInt(1, 100) <= truePercentage;
    }

    public static int[] generateNewGenome() {
        int len = Animal.GENOME_LENGTH;
        int[] newGenome = new int[len];
        for (int i = 0; i < len; i++) {
            newGenome[i] = generateNextInt(0, 7);
        }
        return newGenome;
    }

    public static int[] createNewGenome(Animal dad, Animal mum) {
        int[] numberOfGenomes = findPercentage(dad.getEnergy(), mum.getEnergy());
        return fillNewGenome(numberOfGenomes, dad, mum);
    }

    private static int[] fillNewGenome(int[] numberOfGenomes, Animal dad, Animal mum) {
        int[] newGenome = new int[Animal.GENOME_LENGTH];
        if (isTrue(50)) {
            for (int i = 0; i < numberOfGenomes[0]; i++) {
                newGenome[i] = dad.getGenome()[i];
            }
            for (int i = numberOfGenomes[0]; i < Animal.GENOME_LENGTH; i++) {
                newGenome[i] = mum.getGenome()[i];
            }
        } else {
            for (int i = 0; i < numberOfGenomes[1]; i++) {
                newGenome[i] = mum.getGenome()[i];
            }
            for (int i = numberOfGenomes[1]; i < Animal.GENOME_LENGTH; i++) {
                newGenome[i] = dad.getGenome()[i];
            }
        }
        refactorNewGenome(newGenome);
        return newGenome;
    }

    private static void refactorNewGenome(int[] newGenome) {
        Set<Integer> newValues = new HashSet<>();
        for (int i = 0; i < generateNextInt(0, Animal.GENOME_LENGTH); i++) {
            newValues.add(generateNextInt(0, Animal.GENOME_LENGTH - 1));
        }
        for (int value : newValues) {
            newGenome[value] = generateNextInt(0, 7);
        }
    }

    private static int[] findPercentage(float value1, float value2) {
        float sum = value1 + value2;
        float percentage1 = value1 / sum;
        float percentage2 = value2 / sum;
        int places1 = Math.round(percentage1 * Animal.GENOME_LENGTH);
        int places2 = Math.round(percentage2 * Animal.GENOME_LENGTH);
        if (places1 + places2 > Animal.GENOME_LENGTH) {
            if (isTrue(50)) {
                places1 -= 1;
            } else {
                places2 -= 1;
            }
        }
        return new int[]{places1, places2};

    }
}
