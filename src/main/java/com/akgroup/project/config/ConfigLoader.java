package com.akgroup.project.config;

import java.io.InputStream;
import java.util.Scanner;

public class ConfigLoader {
    private Scanner scanner;

    public Config loadConfig(String configName) {
        Config config = new Config();
        InputStream resourceAsStream = ConfigLoader.class.getResourceAsStream("/config/%s.csv".formatted(configName));
        if(resourceAsStream == null)
            return null;
        scanner = new Scanner(resourceAsStream);
        while (scanner.hasNextLine()){
            readNextConfigLine(config);
        }
        if(!config.isCorrect()){
            System.out.println("There are missing options in config file!");
            return null;
        }
        return config;
    }

    private void readNextConfigLine(Config config){
        String line = scanner.nextLine().strip();
        String name = line.split(";")[0];
        String data = line.split(";")[1];
        ConfigOption option;
        try {
            option = ConfigOption.valueOf(name);
        }catch (IllegalArgumentException e){
            System.out.println("Illegal config option: " + line);
            return;
        }
        if(config.hasValue(option)){
            System.out.println("Multiple definition of " + option + " in config file!");
        }
        config.addValue(option, Integer.parseInt(data));
    }
}