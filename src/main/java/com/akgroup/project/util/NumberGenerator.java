package com.akgroup.project.util;

import com.akgroup.project.world.object.Animal;

import java.util.Random;

public class NumberGenerator {
    private static final Random random = new Random();

    /** Generates new random number between given min and max values*/
    public static int generateNextInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /** Returns true with given percentage value*/
    public static boolean isTrue(int truePercentage) {
        return generateNextInt(1, 100) <= truePercentage;
    }

    /** Generates new animal genome*/
    public static int[] generateNewGenome() {
        int len = Animal.GENOME_LENGTH;
        int[] newGenome = new int[len];
        for (int i = 0; i < len; i++) {
            newGenome[i] = generateNextInt(0, 7);
        }
        return newGenome;
    }

    /** Generates combined genome from given dad and mum Animal objects.*/
    public static int[] createNewGenome(Animal dad, Animal mum) {
        int numberOfGenomes = findHowManyGenomes(dad.getEnergy(), mum.getEnergy());
        return fillNewGenome(numberOfGenomes, dad, mum);
    }

    /**Fulfill new genome based on parent objects and numberOfGenomes.
     * @param numberOfGenomes - amount of genomes which will be given from dad animal*/
    private static int[] fillNewGenome(int numberOfGenomes, Animal dad, Animal mum) {
        int[] newGenome = new int[Animal.GENOME_LENGTH];
        int numberOfGenomes2 = Animal.GENOME_LENGTH - numberOfGenomes;
        if (isTrue(50)) {
            for (int i = 0; i < numberOfGenomes; i++) {
                newGenome[i] = dad.getGenome()[i];
            }
            for (int i = numberOfGenomes; i < Animal.GENOME_LENGTH; i++) {
                newGenome[i] = mum.getGenome()[i];
            }
        } else {
            for (int i = 0; i < numberOfGenomes2; i++) {
                newGenome[i] = mum.getGenome()[i];
            }
            for (int i = numberOfGenomes2; i < Animal.GENOME_LENGTH; i++) {
                newGenome[i] = dad.getGenome()[i];
            }
        }
        return newGenome;
    }

    /** Function returns how many genomes will be given to child (from first animal) based on mum and dad energy values*/
    private static int findHowManyGenomes(float value1, float value2) {
        float sum = value1 + value2;
        float percentage1 = value1 / sum;
        float percentage2 = value2 / sum;
        int places1 = Math.round(percentage1 * Animal.GENOME_LENGTH);
        int places2 = Math.round(percentage2 * Animal.GENOME_LENGTH);
        if (places1 + places2 > Animal.GENOME_LENGTH) {
            if (isTrue(50)) places1 -= 1;
        }
        return places1;
    }
}
