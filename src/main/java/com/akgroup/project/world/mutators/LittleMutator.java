package com.akgroup.project.world.mutators;

import com.akgroup.project.util.NumberGenerator;

public class LittleMutator extends GenomeMutator {
    @Override
    protected void mutateAtIndex(int[] genome, int index) {
        if (NumberGenerator.isTrue(50)) {
            genome[index] = (genome[index] - 1) % 8;
            if (genome[index] < 0) {
                genome[index] = 7;
            }
        } else {
            genome[index] = (genome[index] + 1) % 8;
        }
    }
}
