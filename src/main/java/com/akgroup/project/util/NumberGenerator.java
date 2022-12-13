package com.akgroup.project.util;

import java.util.Random;

public class NumberGenerator {
    private static Random random;

    public static void init(){
        random = new Random();
    }

    public static int generateNextInt(int min, int max){
        return random.nextInt(max-min+1)+min;
    }
}
