package com.akgroup.project.util;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;

import java.util.List;

public class MapVisualiser implements IOutputObserver {

    private IWorldMap worldMap;

    private char[][] charsMap;

    private StringBuilder builder;

    private int w, h;

    private final String[] animalArrows = new String[]{"/\\", "^|", "->", "_|", "\\/", "|_", "<-", "|^"};

    @Override
    public void renderFrame(List<Animal> animals, List<Plant> plants) {
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                charsMap[row][col] = 0;
            }
        }
        builder.setLength(0);
        buildHeader();
        fulfillCharMap();
        buildRows();
        System.out.println(builder);
    }

    private void buildRows() {
        buildEmptyRow(h);
        for (int i = 0; i < h; i++) {
            buildRow(i);
        }
        buildEmptyRow(-1);
    }

    private void buildRow(int y) {
        builder.append(String.format("\n%3d: ", h - y - 1));
        builder.append('|');
        char c;
        for (int i = 0; i < w; i++) {
            c = charsMap[h - y - 1][i];
            if (c == 0)
                builder.append("  |");
            else if (c < 9) {
                builder.append(String.format("%s|", animalArrows[c-1]));
            }
            else if(c < 20){
                builder.append(String.format("%2d|", c-9));
            }
            else {
                builder.append(String.format("%c |", c));
            }
        }
    }

    private void buildEmptyRow(int y) {
        builder.append(String.format("\n%3d: ", y));
        builder.append("-".repeat(3 * w + 1));
    }

    private void fulfillCharMap() {
        worldMap.getAllAnimals().forEach(animal -> {
            int x = animal.getPosition().x;
            int y = animal.getPosition().y;
            if(charsMap[y][x] != 0){
                if(charsMap[y][x] < 8)
                    charsMap[y][x] = 10;
                if(charsMap[y][x] > 8 && charsMap[y][x] < 20)
                    charsMap[y][x] += 1;
            }else{
                charsMap[y][x] = (char) (animal.getRotation() + 1);
            }
        });
        worldMap.getPlants().forEach(plant -> {
            int x = plant.getPosition().x;
            int y = plant.getPosition().y;
            charsMap[y][x] = 'X';
        });
    }

    private void buildHeader() {
        builder.append("y\\x  ");
        for (int i = 0; i < w; i++) {
            builder.append(String.format("%2d ", i));
        }
    }

    @Override
    public void init(IWorldMap worldMap) {
        this.worldMap = worldMap;
        this.w = worldMap.getUpperRight().x - worldMap.getLowerLeft().x + 1;
        this.h = worldMap.getUpperRight().y - worldMap.getLowerLeft().y + 1;
        this.charsMap = new char[this.h][this.w];
        this.builder = new StringBuilder();
    }

    @Override
    public void onAnimalSummoned(Vector2D position) {

    }

    @Override
    public void onPositionChanged(Vector2D oldPosition, Vector2D newPosition) {

    }

    @Override
    public void onPlantSummoned(Vector2D position) {

    }

    @Override
    public void onPlantWasEaten(Vector2D position) {

    }
}
