package com.akgroup.project.util;

import com.akgroup.project.world.object.Animal;

import java.util.Random;

public class NumberGenerator {
    private static Random random;

    public static void init() {
        random = new Random();
    }

    public static int generateNextInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean isTrue(int truePercentage) {
        return generateNextInt(1, 100) <= truePercentage;
    }

    public static int[] generateNewGenome() {
        int len = Animal.GENOME_LENGTH;
        int[] newTable = new int[len];
        for (int i = 0; i < len; i++) {
            newTable[i] = generateNextInt(0, 7);
        }
        return newTable;
    }

//    public static int[] createNewGenome(Animal dad, Animal mother){
//
//    }
}
