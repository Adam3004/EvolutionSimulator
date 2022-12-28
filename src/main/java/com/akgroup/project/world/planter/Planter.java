package com.akgroup.project.world.planter;

import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.SortedList;
import com.akgroup.project.util.Vector2D;

import java.util.*;

public abstract class Planter {
    protected SortedList<Vector2DWithPossibility> listOfPossibilities;
    protected int width;
    protected int height;

    protected Vector2D jgBottomLeft;
    protected Vector2D jgTopRight;

    protected Planter(int width, int height) {
        this.listOfPossibilities = new SortedList<>(Comparator.comparing(Vector2DWithPossibility::getPossibility));
        this.width = width;
        this.height = height;
    }

    public SortedList<Vector2DWithPossibility> getListOfPossibilities() {
        return listOfPossibilities;
    }

    public void addValue(Vector2D vector2D, int val) {
        listOfPossibilities.add(new Vector2DWithPossibility(val, vector2D));
    }

    public void init() {
        for (int currH = 0; currH < height; currH++) {
            for (int currW = 0; currW < width; currW++) {
                listOfPossibilities.add(new Vector2DWithPossibility(1, new Vector2D(currW, currH)));
            }
        }
        createJungle();
        increasePossibility(jgBottomLeft, jgTopRight);
    }

    public abstract void update(Vector2D vector2D, int valueChange);

    public Vector2D findNewVector() {
        return listOfPossibilities.get(NumberGenerator.generateNextInt(0,listOfPossibilities.size()-1)).getVector2D();
    }

    private void createJungle() {
        int[] params = findJungle();
//        half of the width and height
        int jgWidth = Math.toIntExact(Math.round(params[0] / 2));
        int jgHeight = Math.toIntExact(Math.round(params[1] / 2));
        int middleWidth = Math.round(width / 2);
        int middleHeight = Math.round(height / 2);
        int leftW = middleWidth - jgWidth;
        int rightW = middleWidth + jgHeight;
        int topH = middleHeight + jgHeight;
        int bottomH = middleHeight - jgHeight;
        if (rightW - leftW != params[0]) {
            rightW += (params[0] - (rightW - leftW));
        }
        if (topH - bottomH != params[1]) {
            bottomH -= (params[1] - (topH - bottomH));
        }
        jgBottomLeft = new Vector2D(leftW, bottomH);
        jgTopRight = new Vector2D(rightW, topH);
    }

    private int[] findJungle() {
        int jgWidth = Math.toIntExact(Math.round(0.25 * width));
        int jgHeight = Math.toIntExact(Math.round(0.25 * height));
        if ((width > 1 && height >= 1) || (width >= 1 && height > 1)) {
            jgWidth = Math.max(jgWidth, 1);
            jgHeight = Math.max(jgHeight, 1);
        }
        return new int[]{jgWidth, jgHeight};
    }

    protected void increasePossibility(Vector2D topLeft, Vector2D bottomRight) {
        listOfPossibilities.stream()
                .filter(data -> data.getVector2D().follows(topLeft) && data.getVector2D().precedes(bottomRight))
                .forEach(data -> data.setPossibility(data.getPossibility() + 1));
        listOfPossibilities.sortList();
    }

    @Override
    public String toString() {
        return "Planter{" +
                "possibility=" + listOfPossibilities +
                '}';
    }
}
