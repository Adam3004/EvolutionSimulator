package com.akgroup.project.gui;

import com.akgroup.project.config.Config;
import com.akgroup.project.engine.Engine;
import com.akgroup.project.engine.statistics.StatSpectator;
import com.akgroup.project.util.Saver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/** Class responsible for GUI side of Application */
public class App extends Application implements IFXObserver {
    private int simulationID = 0;

    private Stage mainStage;

    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader loader = new FXMLLoader();
        String fxmlDocPath = "/fxml/main.fxml";

        VBox root = loader.load(this.getClass().getResourceAsStream(fxmlDocPath));
        SimulationConfigController controller = loader.getController();
        controller.setObserver(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Simulation Config");
        stage.show();
        stage.setOnCloseRequest(event -> System.exit(0));
    }

    @Override
    public void startSimulation(Config config, boolean generateCSV) {
        StatSpectator spectator = new StatSpectator(config.getMapArea());
        Engine engine = new Engine(config, spectator);
        Thread thread = new Thread(engine);
        FXMLLoader loader = new FXMLLoader();
        String fxmlDocPath = "/fxml/simulation.fxml";

        Parent root;
        try {
            root = loader.load(this.getClass().getResourceAsStream(fxmlDocPath));
            SimulationController controller = loader.getController();
            controller.setStatSpectator(spectator);
            controller.setEngine(engine);
            Stage stage = new Stage();
            stage.setOnCloseRequest(event -> engine.stop());
            stage.setTitle("Simulation " + simulationID);
            stage.setScene(new Scene(root, 1000, 550));
            stage.show();
            engine.addOutputObserver(controller);
            if (generateCSV) {
                engine.addOutputObserver(new Saver("simulation" + simulationID + "Stats.csv", spectator));
            }
//            engine.addOutputObserver(new MapVisualiser());
            thread.start();
            simulationID++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File openConfigFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Config file");
        return fileChooser.showOpenDialog(mainStage);
    }
}
