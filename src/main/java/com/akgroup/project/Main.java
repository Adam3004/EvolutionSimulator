package com.akgroup.project;

import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigLoader;
import com.akgroup.project.config.InvalidConfigException;
import com.akgroup.project.engine.Engine;
import com.akgroup.project.util.MapVisualiser;

public class Main {
    public static void main(String[] args) {
        try {
            ConfigLoader loader = new ConfigLoader();
            Config config = loader.loadConfig("default");
            createThread(config);
        } catch (InvalidConfigException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Function creates new thread based on given config options
     */
    private static void createThread(Config config) {
        Engine engine = new Engine(config, new MapVisualiser());
        Thread thread = new Thread(engine);
        thread.start();
    }

//    równik z trupami, albo kula z czymś
}