package com.akgroup.project;

import java.io.IOException;

import com.akgroup.project.config.Config;
import com.akgroup.project.engine.Engine;
import com.akgroup.project.gui.IFXObserver;
import com.akgroup.project.gui.SimulationConfigController;
import com.akgroup.project.util.MapVisualiser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFXMain extends Application implements IFXObserver {
    public void start(Stage stage) throws IOException
    {
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
        createThread(config);
//                try {
//            ConfigLoader loader = new ConfigLoader();
//            Config config = loader.loadConfig("default");
//            createThread(config);
//        }catch (InvalidConfigException exception){
//            System.out.println(exception.getMessage());
//        }
    }

    /** Function creates new thread based on given config options*/
    private static void createThread(Config config) {
        Engine engine = new Engine(config, new MapVisualiser());
        Thread thread = new Thread(engine);
        thread.start();
    }
}
