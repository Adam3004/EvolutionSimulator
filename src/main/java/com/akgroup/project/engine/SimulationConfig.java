package com.akgroup.project.engine;

public class SimulationConfig {
    public static SimulationConfig INSTANCE;
    private boolean isFullPredestination;
    private int mapWidth, mapHeight;
    private int energyNeededToMove;
    private int multiplicationEnergyLose;
    private int valueFromEating;

    // docelowo taki *konstruktor*
    // public static void init(SimulationConfiguration)
    // lub
    // public static void init(SimulationSettings)

    public static void init() {
        if (INSTANCE != null) return;
        INSTANCE = new SimulationConfig();
        INSTANCE.isFullPredestination = true;
        INSTANCE.mapWidth = 10;
        INSTANCE.mapHeight = 10;
        INSTANCE.energyNeededToMove = 1;
        INSTANCE.multiplicationEnergyLose = 4;
        INSTANCE.valueFromEating = 5;
    }

    private SimulationConfig() {
    }

    public static SimulationConfig getInstance() {
        return INSTANCE;
    }

    public boolean isFullPredestination() {
        return isFullPredestination;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getEnergyNeededToMove() {
        return energyNeededToMove;
    }

    public int getMultiplicationEnergyLose() {
        return multiplicationEnergyLose;
    }

    public int getValueFromEating() {
        return valueFromEating;
    }
}
