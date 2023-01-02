package com.akgroup.project.util;

import com.akgroup.project.world.object.Animal;

import java.util.Random;

public class NumberGenerator {
    private static final Random random = new Random();

    /**
     * Generates new random number between given min and max values
     */
    public static int generateNextInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Returns true with given percentage value
     */
    public static boolean isTrue(int truePercentage) {
        return generateNextInt(1, 100) <= truePercentage;
    }

    /**
     * Generates new animal genome
     */
    public static int[] generateNewGenome(int len) {
        int[] newGenome = new int[len];
        for (int i = 0; i < len; i++) {
            newGenome[i] = generateNextInt(0, 7);
        }
        return newGenome;
    }

    /**
     * Generates combined genome from given dad and mum Animal objects.
     */
    public static int[] createNewGenome(Animal dad, Animal mum, int lenOfGenome) {
        int numberOfGenomes = findHowManyGenomes(dad.getEnergy(), mum.getEnergy(), lenOfGenome);
        return fillNewGenome(numberOfGenomes, dad, mum, lenOfGenome);
    }

    /**
     * Fulfill new genome based on parent objects and numberOfGenomes.
     *
     * @param numberOfGenomes amount of genomes which will be given from dad animal
     * @param len length of animal genome
     */
    private static int[] fillNewGenome(int numberOfGenomes, Animal dad, Animal mum, int len) {
        int[] newGenome = new int[len];
        int numberOfGenomes2 = len - numberOfGenomes;
        try {
            if (isTrue(50)) {
                for (int i = 0; i < numberOfGenomes; i++) {
                    newGenome[i] = dad.getGenome()[i];
                }
                for (int i = numberOfGenomes; i < len; i++) {
                    newGenome[i] = mum.getGenome()[i];
                }
            } else {
                for (int i = 0; i < numberOfGenomes2; i++) {
                    newGenome[i] = mum.getGenome()[i];
                }
                for (int i = numberOfGenomes2; i < len; i++) {
                    newGenome[i] = dad.getGenome()[i];
                }
            }
        }catch (Exception e){
            System.out.println(numberOfGenomes + " " + numberOfGenomes2);
        }
        return newGenome;
    }

    /**
     * Function returns how many genomes will be given to child (from first animal) based on mum and dad energy values
     */
    private static int findHowManyGenomes(float energy1, float energy2, int genomeLen) {
        float sum = energy1 + energy2;
        float percentage1 = energy1 / sum;
        float percentage2 = energy2 / sum;
        int places1 = Math.round(percentage1 * genomeLen);
        int places2 = Math.round(percentage2 * genomeLen);
        if (places1 + places2 > genomeLen) {
            if (isTrue(50)) places1 -= 1;
        }
        return places1;
    }

    public static Vector2D generateNextVector2D(int width, int height) {
        return new Vector2D(generateNextInt(0, width - 1), generateNextInt(0, height - 1));
    }
}
