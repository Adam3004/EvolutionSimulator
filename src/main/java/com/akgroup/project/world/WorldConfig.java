package com.akgroup.project.world;

import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.world.behaviour.IAnimalBehaviour;
import com.akgroup.project.world.behaviour.FullPredestinationBehaviour;
import com.akgroup.project.world.behaviour.LittleCrazyBehaviour;
import com.akgroup.project.world.borders.EarthBorders;
import com.akgroup.project.world.borders.HellPortalBorders;
import com.akgroup.project.world.borders.MapBorders;
import com.akgroup.project.world.mutators.FullRandomMutator;
import com.akgroup.project.world.mutators.GenomeMutator;
import com.akgroup.project.world.mutators.LittleMutator;
import com.akgroup.project.world.planter.EquatorPlanter;
import com.akgroup.project.world.planter.Planter;
import com.akgroup.project.world.planter.ToxicPlanter;

public record WorldConfig(MapBorders mapBorders, GenomeMutator mutator, Planter planter, IAnimalBehaviour behaviour) {

    public static WorldConfig fromConfig(Config config) {
        int width = config.getValue(ConfigOption.WIDTH);
        int height = config.getValue(ConfigOption.HEIGHT);
        int bordersType = config.getValue(ConfigOption.MAP_TYPE);
        int planterType = config.getValue(ConfigOption.PLANTS_TYPE);
        int mutatorType = config.getValue(ConfigOption.MUTATION_TYPE);
        int behaviourType = config.getValue(ConfigOption.ANIMAL_BEHAVIOUR_TYPE);
        MapBorders borders = bordersFromConfig(bordersType, width, height);
        Planter planter = planterFromConfig(planterType, width, height);
        GenomeMutator mutator = genomeMutatorFromConfig(mutatorType);
        IAnimalBehaviour behaviour = animalBehaviourFromConfig(behaviourType);
        return new WorldConfig(borders, mutator, planter, behaviour);
    }

    private static IAnimalBehaviour animalBehaviourFromConfig(int behaviourType) {
        switch (behaviourType) {
            case 0 -> {
                return new FullPredestinationBehaviour();
            }
            default -> {
                return new LittleCrazyBehaviour();
            }
        }
    }

    private static MapBorders bordersFromConfig(int bordersType, int width, int height) {
        switch (bordersType) {
            case 0 -> {
                return new EarthBorders(width, height);
            }
            default -> {
                return new HellPortalBorders(width, height);
            }
        }
    }

    private static Planter planterFromConfig(int bordersType, int width, int height) {
        switch (bordersType) {
            case 0 -> {
                return new EquatorPlanter(width, height);
            }
            default -> {
                return new ToxicPlanter(width, height);
            }
        }
    }

    private static GenomeMutator genomeMutatorFromConfig(int bordersType) {
        switch (bordersType) {
            case 0 -> {
                return new FullRandomMutator();
            }
            default -> {
                return new LittleMutator();
            }
        }
    }
}
