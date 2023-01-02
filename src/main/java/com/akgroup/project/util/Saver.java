package com.akgroup.project.util;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.engine.statistics.StatSpectator;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Saver implements IOutputObserver {

    private final String CSV_FILE_NAME;
    private final StatSpectator s;

    public Saver(String CSV_FILE_NAME, StatSpectator s) {
        this.CSV_FILE_NAME = CSV_FILE_NAME;
        this.s = s;
    }

    public String convertToCSV(List<String> data) {
        return String.join(",", data);
    }

    public void save(List<String> dataLines) throws IOException {
        File csvOutputFile = new File(CSV_FILE_NAME);
        if (!csvOutputFile.exists()) {
            csvOutputFile.createNewFile();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvOutputFile, true))) {
            bw.write((convertToCSV(dataLines)));
        }
    }

    public void saveDayToFile() {
        try {
            save(prepareLineToSave());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<String> prepareLineToSave() {
        List<String> tmpList = new ArrayList<>();
        tmpList.add(convertToString(s.getAverageEnergy()));
        tmpList.add(convertToString(s.getAverageAgeOfDiedAnimals()));
        tmpList.add(convertToString(s.getFreeFields()));
        tmpList.add(convertToString(s.getNumberOfAliveAnimals()));
        tmpList.add(convertToString(s.getNumberOfPlants()));
        tmpList.add(Arrays.toString(s.getMostPopularGenotype()) + "\n");
        return tmpList;
    }

    private String convertToString(int val) {
        return String.valueOf(val);
    }

    @Override
    public void init(IWorldMap worldMap) {
        File csvOutputFile = new File(CSV_FILE_NAME);
        if (csvOutputFile.exists()) {
            csvOutputFile.delete();
        }
        csvOutputFile = new File(CSV_FILE_NAME);
        if (!csvOutputFile.exists()) {
            try {
                csvOutputFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvOutputFile, true))) {
            bw.write(("Average energy, Day of death, Free fields, Animals, Plants, Most popular genome\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void renderFrame(List<Animal> animals, List<Plant> plants) {
        saveDayToFile();
    }
}
