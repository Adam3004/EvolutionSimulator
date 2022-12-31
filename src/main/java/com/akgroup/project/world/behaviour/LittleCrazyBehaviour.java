package com.akgroup.project.world.behaviour;

import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.world.object.Animal;

public class LittleCrazyBehaviour implements AnimalBehaviour{
    @Override
    public int getAnimalRotation(int genomeLen) {
        if (NumberGenerator.isTrue(20)) {
            return NumberGenerator.generateNextInt(1, genomeLen);
        }
        return 1;
    }
}
