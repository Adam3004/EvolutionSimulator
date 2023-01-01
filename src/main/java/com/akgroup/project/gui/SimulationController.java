package com.akgroup.project.gui;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.*;

public class SimulationController implements IOutputObserver {

    @FXML
    private GridPane grid;
    private int width, height;
    private float cellSize;

    private IWorldMap worldMap;

    @Override
    public void init(IWorldMap worldMap) {
        this.worldMap = worldMap;
        width = worldMap.getUpperRight().y + 1;
        height = worldMap.getUpperRight().x + 1;
        cellSize = Math.max(width, height);
        cellSize = 400 / cellSize;
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.setBackground(new Background(new BackgroundFill(Color.rgb(186, 114, 32), CornerRadii.EMPTY, Insets.EMPTY)));
        grid.setMaxWidth(cellSize * width);
        grid.setMaxHeight(cellSize * height);
        addGridConstraints();
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

    @Override
    public void renderFrame(List<Animal> animals, List<Plant> plants) {
        Platform.runLater(() -> {
            renderNextFrame(animals, plants);
        });
    }

    private final Color GRASS_COLOR = Color.rgb(55, 186, 32);
    private final Color ANIMAL_COLOR = Color.rgb(40, 40, 40);

    private void renderNextFrame(List<Animal> animals, List<Plant> plants) {
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        Node node = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0, node);
        grid.getRowConstraints().clear();
        addGridConstraints();
        plants.forEach(plant -> {
            Vector2D position = plant.getPosition();
            Circle circle = createCircle(GRASS_COLOR);
            addCircleToGrid(circle, position.x, position.y);
        });
        animals.forEach(animal -> {
            Vector2D position = animal.getPosition();
            Circle circle = createCircle(ANIMAL_COLOR);
            addCircleToGrid(circle, position.x, position.y);
        });
    }

    private void addCircleToGrid(Circle circle, int x, int y) {
        grid.add(circle, x, y, 1, 1);
        GridPane.setHalignment(circle, HPos.CENTER);
    }

    private Circle createCircle(Color color) {
        Circle circle = new Circle();
        circle.setFill(color);
        circle.setRadius(cellSize / 4);
        return circle;
    }

    private void addGridConstraints() {
        for (int i = 0; i < width; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }
        for (int i = 0; i < height; i++) {
            grid.getRowConstraints().add(new RowConstraints(cellSize));
        }
    }
}