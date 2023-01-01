package com.akgroup.project.gui;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.engine.Engine;
import com.akgroup.project.engine.statistics.StatSpectator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.IWorldElement;
import com.akgroup.project.world.object.Plant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.*;

public class SimulationController implements IOutputObserver {

    @FXML
    private GridPane grid;

    @FXML
    private Label statAnimals, statAvgAge, statEnergy, statFields, statGenotype, statPlants;

    @FXML
    private Button simulationButton;
    private int width, height;
    private float cellSize;

    private IWorldMap worldMap;
    private StatSpectator statSpectator;
    private boolean simulationStopped = false;
    private Engine engine;

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
        grid.setOnMouseClicked(this::onGridMouseClicked);
    }

    private void onGridMouseClicked(MouseEvent mouseEvent) {
        if(!simulationStopped) return;
        if(!mouseEvent.getButton().equals(MouseButton.PRIMARY)) return;
        long x = (long) Math.floor(mouseEvent.getX() / cellSize);
        long y = (long) Math.floor(mouseEvent.getY() / cellSize);
        showDetailsForXY(x, height - y - 1);
    }

    private void showDetailsForXY(long x, long y) {
        List<IWorldElement> objectsAt = worldMap.getObjectsAt(new Vector2D((int) x, (int) y));
        System.out.println(objectsAt);
    }

    @FXML
    void onSimulationAction(ActionEvent event) {
        if(simulationStopped){
            startSimulation();
        }else{
            stopSimulation();
        }
        simulationStopped = !simulationStopped;
    }

    private void stopSimulation() {
        simulationButton.setText("Resume");
        engine.pause();
    }

    private void startSimulation() {
        simulationButton.setText("Pause");
        engine.resume();
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
        statAnimals.setText(statSpectator.getNumberOfAliveAnimals() + "");
        statPlants.setText(statSpectator.getNumberOfPlants() + "");
        statFields.setText(statSpectator.getFreeFields() + "");
        statGenotype.setText(Arrays.toString(statSpectator.getMostPopularGenotype()));
        statAvgAge.setText(statSpectator.getAverageAgeOfDiedAnimals() + "");
        statEnergy.setText(statSpectator.getAverageEnergy() + "");
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
            addCircleToGrid(circle, position.x, height - position.y - 1);
        });
        animals.forEach(animal -> {
            Vector2D position = animal.getPosition();
            Circle circle = createCircle(ANIMAL_COLOR);
            addCircleToGrid(circle, position.x, height - position.y - 1);
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

    public void setStatSpectator(StatSpectator spectator) {
        this.statSpectator = spectator;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}