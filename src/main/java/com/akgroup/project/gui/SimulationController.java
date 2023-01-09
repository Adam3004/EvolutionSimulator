package com.akgroup.project.gui;

import com.akgroup.project.IOutputObserver;
import com.akgroup.project.engine.Engine;
import com.akgroup.project.engine.statistics.StatSpectator;
import com.akgroup.project.gui.render.Renderer;
import com.akgroup.project.gui.render.SimpleRenderer;
import com.akgroup.project.gui.render.TextureRenderer;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Arrays;
import java.util.List;

/**
 * Class is implementation of FXML Controller for Simulation Window
 */
public class SimulationController implements IOutputObserver {

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    public static final int RENDERING_TEXTURES_MAX = 17;
    @FXML
    private LineChart<Number, Number> graph;

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
    private IWorldMap worldMap;
    private Renderer renderer;
    private Graph graph2;


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
                .forEach(renderer::markAnimal);
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
        boolean renderTextures = cellSize < RENDERING_TEXTURES_MAX;
        if(cellSize < 8){
            cellSize = 400 / cellSize;
        }else{
            cellSize = 800 / cellSize;
        }
        cellSize = Math.round(cellSize * 100);
        cellSize = cellSize / 100;
        resetGridConstraints();
        grid.setOnMouseClicked(this::onGridMouseClicked);
        showAnimalsWithGenotype.setVisible(false);
        grid.setMaxWidth(cellSize * width);
        grid.gridLinesVisibleProperty().set(false);
        initGraph();
        if(renderTextures){
            renderer = new TextureRenderer(cellSize, grid, height);
        }else{
            renderer = new SimpleRenderer(cellSize, grid, height);
        }
        renderer.renderGridBackground();
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
        List<Animal> animals = lastRenderedAnimals.stream().filter(a -> a.getPosition().equals(clickedPosition)).toList();
        if(animals.isEmpty()) return;
        int index = animals.indexOf(chosenAnimal);
        if(chosenAnimal != null && index != -1){
            index++;
        }else{
            index = 0;
        }
        chosenAnimal = animals.get((index % animals.size()));
        renderAnimalDetails();
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
        renderer.renderJungle(worldMap);
        plants.forEach(renderer::renderPlant);
        animals.forEach(renderer::renderAnimal);
        lastRenderedAnimals = animals;
        renderAnimalDetails();
        graph2.updateGraph(animals.size(), plants.size());
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

    private void initGraph() {
        graph2 = new Graph(xAxis, yAxis, graph);
        graph2.prepareGraph();
    }


    @Override
    public void renderFrame(List<Animal> animals, List<Plant> plants) {
        Platform.runLater(() -> renderNextFrame(animals, plants));
    }
}