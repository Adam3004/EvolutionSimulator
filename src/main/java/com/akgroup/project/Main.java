package com.akgroup.project;

import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigLoader;
import com.akgroup.project.engine.Engine;

public class Main {
    public static void main(String[] args) {
        ConfigLoader loader = new ConfigLoader();
        Config config = loader.loadConfig("default");
        createThread(config);
    }

    /** Function creates new thread based on given config options*/
    private static void createThread(Config config) {
        Engine engine = new Engine(config);
        Thread thread = new Thread(engine);
        thread.start();
    }

//    równik z trupami, albo kula z czymś
}