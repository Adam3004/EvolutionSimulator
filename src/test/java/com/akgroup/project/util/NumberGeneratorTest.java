package com.akgroup.project.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberGeneratorTest {

    @BeforeEach
    void setUp() {
        NumberGenerator.init();
    }

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
}