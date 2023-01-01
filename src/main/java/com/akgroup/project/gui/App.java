package com.akgroup.project.gui;

import com.akgroup.project.config.Config;
import com.akgroup.project.engine.Engine;
import com.akgroup.project.engine.statistics.StatSpectator;
import com.akgroup.project.util.MapVisualiser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application implements IFXObserver {


    private int simulationID = 0;

    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        String fxmlDocPath = "/fxml/main.fxml";

        VBox root = loader.load(this.getClass().getResourceAsStream(fxmlDocPath));
        SimulationConfigController controller = loader.getController();
        controller.setObserver(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Simulation Config");
        stage.show();
    }

    @Override
    public void startSimulation(Config config) {
        Engine engine = new Engine(config, new StatSpectator(config.getMapArea()));
        Thread thread = new Thread(engine);
        FXMLLoader loader = new FXMLLoader();
        String fxmlDocPath = "/fxml/simulation.fxml";

        Parent root;
        try {
            root = loader.load(this.getClass().getResourceAsStream(fxmlDocPath));
            SimulationController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Simulation " + simulationID);
            stage.setScene(new Scene(root, 800, 450));
            stage.show();
            engine.addOutputObserver(controller);
            engine.addOutputObserver(new MapVisualiser());
            thread.start();
            simulationID++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
