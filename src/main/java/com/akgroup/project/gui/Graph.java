package com.akgroup.project.gui;

import com.akgroup.project.util.SortedList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Comparator;

public class Graph {

    private NumberAxis xAxis;

    private NumberAxis yAxis;

    private LineChart<Number, Number> graph;

    private int startX;

    private int endX;

    private int startY;

    private int endY;

    private final SortedList<Integer> yValues = new SortedList<>(Comparator.comparing(Integer::intValue));

    private final int SIZE_OF_VIEW = 10;

    public Graph(NumberAxis xAxis, NumberAxis yAxis, LineChart<Number, Number> graph) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.graph = graph;
        this.startX = 0;
        this.endX = 0;
        this.startY = 0;
        this.endY = 0;
    }

    public void updateGraph(int numberOfAnimals, int numberOfPlants) {
        endX += 1;
        yValues.add(numberOfAnimals);
        yValues.add(numberOfPlants);
        graph.getData().get(0).getData().add(new XYChart.Data<Number, Number>((Number) endX, numberOfAnimals));
        graph.getData().get(1).getData().add(new XYChart.Data<Number, Number>((Number) endX, numberOfPlants));
        if (graph.getData().get(0).getData().size() > SIZE_OF_VIEW) {
            updateYAxis();
            startX += 1;
            graph.getData().get(0).getData().remove(0);
            graph.getData().get(1).getData().remove(0);
        }
        setY(numberOfAnimals);
        setY(numberOfPlants);
        updateAxes();
    }

    private void updateYAxis() {
        yValues.removeObject(graph.getData().get(0).getData().get(0).getYValue().intValue());
        yValues.removeObject(graph.getData().get(1).getData().get(0).getYValue().intValue());
        startY = Math.max(yValues.getLast() - findMarginVal(yValues.getLast()), 0);
        endY = yValues.getFirst() + findMarginVal(yValues.getFirst());

    }

    private int findMarginVal(int val) {
        return Math.max(1, Math.toIntExact(Math.round((float) val / 5)));
    }

    private void setY(int newVal) {
        if (newVal > endY) {
            endY = newVal + findMarginVal(newVal);
        } else if (newVal < startY) {
            startY = newVal - findMarginVal(newVal);
        }
    }

    private void updateAxes() {
        xAxis.setUpperBound(endX);
        xAxis.setLowerBound(startX);
        yAxis.setLowerBound(startY);
        yAxis.setUpperBound(endY);
    }

    public void prepareGraph() {
        XYChart.Series<Number, Number> animals = new XYChart.Series<Number, Number>();
        XYChart.Series<Number, Number> plants = new XYChart.Series<Number, Number>();

        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);

        animals.getData().add(new XYChart.Data<Number, Number>(0, 0));
        plants.getData().add(new XYChart.Data<Number, Number>(0, 0));

        animals.setName("Number of animals");
        plants.setName("Number of plants");

        graph.getData().add(animals);
        graph.getData().add(plants);
    }

}
