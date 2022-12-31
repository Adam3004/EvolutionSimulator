package com.akgroup.project.util;

import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.world.object.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class NumberGeneratorTest {


    @Test
    void generateNextInt() {
        int min = 0;
        int max = 8;
        for (int i = 0; i < 100; i++) {
            int value = NumberGenerator.generateNextInt(min, max);
            assert value >= min;
            assert value <= max;
        }
    }

    @Test
    void generateNewGenome() {
        for (int i = 0; i < 10; i++) {
            int[] value = NumberGenerator.generateNewGenome(5);
            assertTrue(value.length == 5);
            System.out.println(Arrays.stream(value).mapToObj(Integer::toString).collect(Collectors.joining(",")));
            for (int j = 0; j < Config.defaultConfig().getValue(ConfigOption.GENOME_LENGTH); j++) {
                assertTrue(value[j] >= 0);
                assertTrue(value[j] <= 7);
            }
        }
    }

    @Test
    void createNewGenome() {
        int len = Config.defaultConfig().getValue(ConfigOption.GENOME_LENGTH);
        int[] newGenome = NumberGenerator.createNewGenome(new Animal(new Vector2D(2, 3), new int[]{2, 7, 0, 3, 4}), new Animal(new Vector2D(3, 2), new int[]{1, 2, 3, 7, 0}), len);
        assertTrue(newGenome.length == len);
        System.out.println(Arrays.stream(newGenome).mapToObj(Integer::toString).collect(Collectors.joining(",")));
        for (int j = 0; j < len; j++) {
            assertTrue(newGenome[j] >= 0);
            assertTrue(newGenome[j] <= 7);
        }
    }
}