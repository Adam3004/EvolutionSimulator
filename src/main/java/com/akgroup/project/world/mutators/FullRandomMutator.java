package com.akgroup.project.world.mutators;

import static com.akgroup.project.util.NumberGenerator.generateNextInt;

public class FullRandomMutator extends GenomeMutator {
    @Override
    protected void mutateAtIndex(int[] genome, int index) {
        genome[index] = generateNextInt(0, 7);
    }
}
