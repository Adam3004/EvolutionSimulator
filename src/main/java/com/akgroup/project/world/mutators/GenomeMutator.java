package com.akgroup.project.world.mutators;

import com.akgroup.project.world.object.Animal;

import java.util.HashSet;
import java.util.Set;

import static com.akgroup.project.util.NumberGenerator.generateNextInt;

public abstract class GenomeMutator {
    private Set<Integer> getIndexesToMutate(int min, int max){
        Set<Integer> indexes = new HashSet<>();
        for (int i = 0; i < generateNextInt(min, max); i++) {
            indexes.add(generateNextInt(0, Animal.GENOME_LENGTH - 1));
        }
        return indexes;
    }

    public void mutate(int[] genome, int min, int max){
        Set<Integer> indexes = getIndexesToMutate(min, max);
        for (Integer index : indexes) {
            mutateAtIndex(genome, index);
        }
    }

    protected abstract void mutateAtIndex(int[] genome, int index);
}
