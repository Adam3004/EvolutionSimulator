package com.akgroup.project.world.planter;

import com.akgroup.project.util.NumberGenerator;
import com.akgroup.project.util.SortedList;
import com.akgroup.project.util.Vector2D;

import java.time.Instant;
import java.util.*;

public abstract class Planter {
    protected SortedList<Vector2DWithPossibility> listOfPossibilities;
    protected int width;
    protected int height;

    protected Vector2D jgBottomLeft;
    protected Vector2D jgTopRight;
    private final int size;
    private Instant timeStart;
    private Instant timeEnd;

    protected Planter(int width, int height) {
        this.listOfPossibilities = new SortedList<>(Comparator.comparing(Vector2DWithPossibility::getPossibility));
        this.width = width;
        this.height = height;
        this.size = width * height;
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

    public Vector2D findNewVectorToPlant() {
        if (listOfPossibilities.get(0).getPossibility() <= -9997) {
            return new Vector2D(-1, -1);
        }
        if (size >= 1000 && NumberGenerator.isTrue(33)) {
            return randForBigSizes();
        }
        if (size < 1000 && NumberGenerator.isTrue(80)) {
            return chooseVector(findInterestingList());
        }
        return chooseVector(filterOnlyPossiblePlaces(listOfPossibilities));
    }


    private Vector2D chooseVector(List<Vector2DWithPossibility> interestingList) {
        timeStart = Instant.now();
        Vector2DWithPossibility chosenField = interestingList.get(NumberGenerator.generateNextInt(0, interestingList.size() - 1));
        plantOnField(chosenField);
        timeEnd = Instant.now();
        //System.out.println("Choosing vector: " + Duration.between(timeStart, timeEnd));
        return chosenField.getVector2D();
    }

    private void plantOnField(Vector2DWithPossibility field) {
        field.setPossibility(field.getPossibility() - 10000);
        listOfPossibilities.sortList();
    }

    public void eatPlantOnField(Vector2D vector2D) {
        Vector2DWithPossibility currVector = listOfPossibilities.stream()
                .filter(data -> data.getVector2D().equals(vector2D))
                .toList().get(0);
        currVector.setPossibility(currVector.getPossibility() + 10000);
        listOfPossibilities.sortList();
    }

    private List<Vector2DWithPossibility> filterOnlyPossiblePlaces(List<Vector2DWithPossibility> interestingList) {
        return interestingList.stream()
                .filter(vector2DWithPossibility -> vector2DWithPossibility.getPossibility() > -9997)
                .toList();
    }


    private Vector2D randForBigSizes() {
        List<Vector2DWithPossibility> newList = filterOnlyPossiblePlaces(listOfPossibilities);
        Vector2DWithPossibility chosenField = newList.get(NumberGenerator.generateNextInt(0, Math.toIntExact(Math.round(newList.size() * 0.2))));
        plantOnField(chosenField);
        return chosenField.getVector2D();
    }


    private List<Vector2DWithPossibility> findInterestingList() {

        int interestingLen = Math.toIntExact(Math.round(size * 0.2));
        if (listOfPossibilities.get(interestingLen).getPossibility() <= -9997) {
            return filterOnlyPossiblePlaces(listOfPossibilities);
        }
        //timeStart = Instant.now();
        int possibilities = 0;
        int start = interestingLen;
        int i = interestingLen;
        int interestingValue = listOfPossibilities.get(interestingLen).getPossibility();
        while (i > 0 && interestingValue == listOfPossibilities.get(i).getPossibility()) {
            possibilities += 1;
            start -= 1;
            i--;
        }
        //timeEnd = Instant.now();
        //System.out.println("First while in findInterestingList: " + Duration.between(timeStart, timeEnd));
        //timeStart = Instant.now();
        List<Vector2DWithPossibility> tmpList = new ArrayList<>(listOfPossibilities.subList(0, start).stream().toList());
        i = interestingLen + 1;
        //timeEnd = Instant.now();
        //System.out.println("Creating list in findInterestingList: " + Duration.between(timeStart, timeEnd));

        //timeStart = Instant.now();
        while (i < size - 1 && interestingValue == listOfPossibilities.get(i).getPossibility()) {
            possibilities += 1;
            i++;
        }
        //timeEnd = Instant.now();
        //System.out.println("Second while in findInterestingList: " + Duration.between(timeStart, timeEnd));
        //timeStart = Instant.now();
        Set<Integer> chosenNumbers = new HashSet<>();
        while (chosenNumbers.size() < possibilities) {
            chosenNumbers.add(NumberGenerator.generateNextInt(start, start + possibilities - 1));
        }
        //timeEnd = Instant.now();
        //System.out.println("Creating set and third while in findInterestingList: " + Duration.between(timeStart, timeEnd));
        //timeStart = Instant.now();
        for (int number : chosenNumbers) {
            tmpList.add(listOfPossibilities.get(number));
        }
        //timeEnd = Instant.now();
        //System.out.println("Last for in findInterestingList: " + Duration.between(timeStart, timeEnd));
        return tmpList;
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

    public Vector2D getJgBottomLeft() {
        return jgBottomLeft;
    }

    public Vector2D getJgTopRight() {
        return jgTopRight;
    }

}
