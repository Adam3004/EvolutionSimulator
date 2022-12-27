package com.akgroup.project.world.mutators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullRandomMutatorTest {

    @Test
    void mutateAtIndex() {
        GenomeMutator mutator = new LittleMutator();
        int[] genome = new int[]{0, 4, 2, 6, 7};
        mutator.mutateAtIndex(genome, 2);
        mutator.mutateAtIndex(genome, 4);
        assertTrue(genome[2] >= 0 && genome[2] <= 7);
        assertTrue(genome[4] >= 0 && genome[4] <= 7);
        assertEquals(0, genome[0]);
        assertEquals(4, genome[1]);
        assertEquals(6, genome[3]);
    }
}