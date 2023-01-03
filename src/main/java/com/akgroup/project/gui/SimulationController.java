package com.akgroup.project.gui;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.engine.Engine;
import com.akgroup.project.engine.statistics.StatSpectator;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.*;
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
import javafx.scene.shape.Rectangle;

import java.util.*;

/**
 * Class is implementation of FXML Controller for Simulation Window
 */
public class SimulationController implements IOutputObserver {

    /**
     * Main simulation grid. Every cell of this grid represents one field on {@link IWorldMap}
     */
    @FXML
    private GridPane grid;

    /**
     * Global statistic Labels for simulation
     */
    @FXML
    private Label statAnimals, statAvgAge, statEnergy, statFields, statGenotype, statPlants;

    /**
     * Pause / Resume button. When clicked runs {@link #onSimulationAction(ActionEvent event)}
     */
    @FXML
    private Button simulationButton;

    /**
     * Detailed statistic Labels for chosen Animal
     */
    @FXML
    private Label statAnimalActiveGen, statAnimalDays, statAnimalEnergy, statAnimalGenome, statAnimalKids, statAnimalPlants;
    @FXML
    private Button showAnimalsWithGenotype;

    private int width, height;
    private float cellSize;
    private StatSpectator statSpectator;
    private boolean simulationStopped = false;
    private Engine engine;
    private Animal chosenAnimal;
    private List<Animal> lastRenderedAnimals;
    private final Color GRASS_COLOR = Color.rgb(55, 186, 32);
    private final Color ANIMAL_MARK_COLOR = Color.rgb(66, 135, 245);
    private final Color ANIMAL_HIGH_ENERGY_COLOR = Color.rgb(255, 59, 131);
    private final Color ANIMAL_LOW_ENERGY_COLOR = Color.rgb(255, 0, 0);
    private final Color ANIMAL_DYING_ENERGY_COLOR = Color.rgb(40, 40, 40);
    private final Color JUNGLE_COLOR = Color.rgb(44, 105, 12);
    private final Color GRID_BACKGROUND = Color.rgb(186, 114, 32);
    private IWorldMap worldMap;

    /**
     * Function runs when {@link #simulationButton} was clicked
     */
    @FXML
    void onSimulationAction(ActionEvent event) {
        if (simulationStopped) {
            startSimulation();
        } else {
            stopSimulation();
        }
        simulationStopped = !simulationStopped;
        showAnimalsWithGenotype.setVisible(simulationStopped);
    }

    /**
     * Function runs when button was clicked. Marks all animals with most popular genotype.
     */
    @FXML
    void onShowAnimalsWithGenotype(ActionEvent event) {
        int[] mostPopularGenotype = statSpectator.getMostPopularGenotype();
        lastRenderedAnimals.stream()
                .filter(animal -> Arrays.equals(animal.getGenome(), mostPopularGenotype))
                .forEach(this::markAnimal);
    }

    /**
     * Finds appropriate cell size and prepare {@link #grid} by adding columns and rows
     */
    @Override
    public void init(IWorldMap worldMap) {
        Platform.runLater(() -> init2(worldMap));
    }

    private void init2(IWorldMap worldMap) {
        this.worldMap = worldMap;
        width = worldMap.getUpperRight().y + 1;
        height = worldMap.getUpperRight().x + 1;
        cellSize = Math.max(width, height);
        cellSize = 400 / cellSize;
        while (cellSize < 20 && cellSize * width < 800) {
            cellSize *= 2;
        }
        cellSize = Math.round(cellSize * 100);
        cellSize = cellSize / 100;
        grid.setBackground(new Background(new BackgroundFill(GRID_BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY)));
        resetGridConstraints();
        grid.setOnMouseClicked(this::onGridMouseClicked);
        showAnimalsWithGenotype.setVisible(false);
        grid.setMaxWidth(cellSize * width);
        grid.gridLinesVisibleProperty().set(false);
    }

    /**
     * Runs when user clicked {@link #grid} with mouse.
     * Action is skipped when simulation is stopped or clicked {@link MouseButton} wasn't PRIMARY.
     * Calculates {@link Vector2D} (x, y) position of {@link MouseEvent}
     */
    private void onGridMouseClicked(MouseEvent mouseEvent) {
        if (!simulationStopped) return;
        if (!mouseEvent.getButton().equals(MouseButton.PRIMARY)) return;
        Node clickedNode = mouseEvent.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
        if(colIndex == null || rowIndex == null) return;
        Vector2D clickedPosition = new Vector2D( colIndex, height - rowIndex - 1);
        Optional<Animal> animal = findAnimalAt(clickedPosition);
        if (animal.isEmpty()) return;
        chosenAnimal = animal.get();
        renderAnimalDetails();
    }

    private Optional<Animal> findAnimalAt(Vector2D clickedPosition) {
        return lastRenderedAnimals.stream().filter(a -> a.getPosition().equals(clickedPosition)).findFirst();
    }

    private void markAnimal(Animal animal) {
        Vector2D position = animal.getPosition();
        Circle circle = createCircle(ANIMAL_MARK_COLOR);
        addCircleToGrid(circle, position.x, height - position.y - 1);
    }

    private void stopSimulation() {
        simulationButton.setText("Resume");
        engine.pause();
    }

    private void startSimulation() {
        simulationButton.setText("Pause");
        engine.resume();
    }

    private void renderNextFrame(List<Animal> animals, List<Plant> plants) {
        renderStatistics();
        grid.getChildren().clear();
        renderJungle();
        plants.forEach(plant -> {
            Vector2D position = plant.getPosition();
            Circle circle = createCircle(GRASS_COLOR);
            addCircleToGrid(circle, position.x, height - position.y - 1);
        });
        animals.forEach(animal -> {
            Vector2D position = animal.getPosition();
            Color color = getColorByEnergy(animal.getEnergy());
            Circle circle = createCircle(color);
            addCircleToGrid(circle, position.x, height - position.y - 1);
        });
        lastRenderedAnimals = animals;
        renderAnimalDetails();
    }

    private void renderJungle() {
        Vector2D lowerLeft = worldMap.getJungleLowerLeft();
        Vector2D upperRight = worldMap.getJungleUpperRight();
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(JUNGLE_COLOR);
        rectangle.setWidth(cellSize * (upperRight.x - lowerLeft.x + 1));
        rectangle.setHeight(cellSize * (upperRight.y - lowerLeft.y + 1));
        grid.add(rectangle, lowerLeft.x, lowerLeft.y, (upperRight.x - lowerLeft.x) + 1, (upperRight.y - lowerLeft.y) + 1);
    }

    private Color getColorByEnergy(int energy) {
        if (energy == 0) {
            return ANIMAL_DYING_ENERGY_COLOR;
        }
        if (energy <= 5) {
            return ANIMAL_LOW_ENERGY_COLOR;
        }
        return ANIMAL_HIGH_ENERGY_COLOR;
    }

    private void renderAnimalDetails() {
        if (chosenAnimal == null) return;
        statAnimalDays.setText(String.valueOf(chosenAnimal.getAge()));
        statAnimalActiveGen.setText(String.valueOf(chosenAnimal.getActiveGenIndex()));
        statAnimalEnergy.setText(String.valueOf(chosenAnimal.getEnergy()));
        statAnimalGenome.setText(Arrays.toString(chosenAnimal.getGenome()));
        statAnimalKids.setText(String.valueOf(chosenAnimal.getNumberOfKids()));
        statAnimalPlants.setText(String.valueOf(chosenAnimal.getEatenPlants()));
    }

    private void renderStatistics() {
        statAnimals.setText(statSpectator.getNumberOfAliveAnimals() + "");
        statPlants.setText(statSpectator.getNumberOfPlants() + "");
        statFields.setText(statSpectator.getFreeFields() + "");
        statGenotype.setText(Arrays.toString(statSpectator.getMostPopularGenotype()));
        statAvgAge.setText(statSpectator.getAverageAgeOfDiedAnimals() + "");
        statEnergy.setText(statSpectator.getAverageEnergy() + "");
    }

    private void addCircleToGrid(Circle circle, int x, int y) {
        grid.add(circle, x, y, 1, 1);
        GridPane.setHalignment(circle, HPos.CENTER);
    }

    private Circle createCircle(Color color) {
        Circle circle = new Circle();
        circle.setFill(color);
        circle.setRadius(1.4 * cellSize / 4);
        return circle;
    }

    private void resetGridConstraints() {
        ColumnConstraints columnConstraints = new ColumnConstraints(cellSize);
        RowConstraints rowConstraints = new RowConstraints(cellSize);
        for (int i = 0; i < width; i++) {
            grid.getColumnConstraints().add(columnConstraints);
        }
        for (int i = 0; i < height; i++) {
            grid.getRowConstraints().add(rowConstraints);
        }
    }

    public void setStatSpectator(StatSpectator spectator) {
        this.statSpectator = spectator;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void renderFrame(List<Animal> animals, List<Plant> plants) {
        Platform.runLater(() -> renderNextFrame(animals, plants));
    }
}