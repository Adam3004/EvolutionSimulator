package com.akgroup.project.engine;

public class SimulationConfig {
    public static SimulationConfig INSTANCE;
    private boolean isFullPredestination;
    private int mapWidth, mapHeight;
    private int energyNeededToMove;

    // docelowo taki *konstruktor*
    // public static void init(SimulationConfiguration)
    // lub
    // public static void init(SimulationSettings)

    public static void init(){
        INSTANCE = new SimulationConfig();
        INSTANCE.isFullPredestination = false;
        INSTANCE.mapWidth = 10;
        INSTANCE.mapHeight = 10;
        INSTANCE.energyNeededToMove = 1;
    }

    public static SimulationConfig getInstance(){
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
}
