package com.akgroup.project.config;

import java.io.InputStream;
import java.util.Scanner;

public class ConfigLoader {
    private Scanner scanner;

    public Config loadConfig(String configName) throws InvalidConfigException {
        Config config = new Config();
        InputStream resourceAsStream = ConfigLoader.class.getResourceAsStream("/config/%s.csv".formatted(configName));
        if(resourceAsStream == null)
            throw new InvalidConfigException("Config file with name '%s' doesn't exists!!".formatted(configName));
        scanner = new Scanner(resourceAsStream);
        while (scanner.hasNextLine()){
            readNextConfigLine(config);
        }
        if(!config.isCorrect())
            throw new InvalidConfigException("There are missing options in config file!");
        return config;
    }

    private void readNextConfigLine(Config config) throws InvalidConfigException {
        String line = scanner.nextLine().strip();
        if(line.indexOf(';') == -1 || line.split(";").length < 2)
            throw new InvalidConfigException("Invalid line format in config file: '%s'".formatted(line));
        String name = line.split(";")[0];
        String data = line.split(";")[1];
        ConfigOption option;
        try {
            option = ConfigOption.valueOf(name);
        }catch (IllegalArgumentException e){
            throw new InvalidConfigException("Illegal config option: " + line);
        }
        if(config.hasValue(option))
            throw new InvalidConfigException("Multiple definition of " + option + " in config file!");
        config.addValue(option, Integer.parseInt(data));
    }
}