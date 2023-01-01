package com.akgroup.project.gui;

import com.akgroup.project.config.Config;

import java.io.File;

public interface IFXObserver {
    void startSimulation(Config config, boolean generateStatsCSV);
    File openConfigFile();
}
