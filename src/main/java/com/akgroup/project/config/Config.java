package com.akgroup.project.config;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private final Map<ConfigOption, Integer> config;

    private static final int[] defaultConfig = new int[]{10, 10, 0, 5, 5, 1, 0, 3, 8, 4, 2, 0, 4, 0, 5, 0};

    public Config() {
        this.config = new HashMap<>();
    }
    public boolean isCorrect(){
        for (ConfigOption option : ConfigOption.values()) {
            if(!config.containsKey(option)) return false;
        }
        return true;
    }

    public int getValue(ConfigOption option){
        return config.get(option);
    }

    public boolean hasValue(ConfigOption option){
        return config.containsKey(option);
    }

    public void addValue(ConfigOption option, int value){
        config.put(option, value);
    }

    public static Config defaultConfig(){
        Config config = new Config();
        for (int i = 0; i < ConfigOption.values().length; i++) {
            config.addValue(ConfigOption.values()[i], defaultConfig[i]);
        }
        return config;
    }

    public int getMapArea() {
        return getWidth() * getHeight();
    }

    private int getHeight() {
        return getValue(ConfigOption.HEIGHT);
    }

    private int getWidth() {
        return getValue(ConfigOption.WIDTH);
    }
}
