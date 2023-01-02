package com.akgroup.project.gui;

import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigLoader;
import com.akgroup.project.config.ConfigOption;
import com.akgroup.project.config.InvalidConfigException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class SimulationConfigController {
    @FXML
    private ToggleGroup behaviour_type;
    @FXML
    private ToggleGroup map_type;
    @FXML
    private ToggleGroup mutation_type;
    @FXML
    private ToggleGroup plant_type;
    @FXML
    private TextField heightInput, widthInput, startPlantInput, plantEveryDayInput, plantEnergyInput, animalsStartInput;
    @FXML
    private TextField animalEnergyInput, animalNeededEnergyInput, animalChildEnergyInput, minMutationInput;
    @FXML
    private TextField maxMutationInput, genomeLengthInput;
    @FXML
    private Menu openBuiltInConfigMenu;
    @FXML
    private CheckBox generateCSV;
    private TextField[] textFields;
    private IFXObserver observer;
    private final ConfigLoader configLoader;
    private boolean isConfigBuiltIn = false;

    private final Map<TextField, Boolean> isFieldValidMap;

    public SimulationConfigController() {
        this.configLoader = new ConfigLoader();
        isFieldValidMap = new HashMap<>();
    }

    @FXML
    public void initialize() {
        textFields = new TextField[]{widthInput, heightInput, startPlantInput, plantEnergyInput,
                plantEveryDayInput, animalsStartInput, animalEnergyInput, animalNeededEnergyInput, animalChildEnergyInput,
                minMutationInput, maxMutationInput, genomeLengthInput};
        String[] configFileNames = ConfigLoader.getConfigFileNames();
        for (String configFileName : configFileNames) {
            MenuItem item = new MenuItem();
            item.setText(configFileName);
            item.setOnAction(event -> loadBuiltInConfigByName(configFileName));
            openBuiltInConfigMenu.getItems().add(item);
        }
    }

    @FXML
    public void startSimulation(ActionEvent actionEvent) {
        if(!hasAllFieldsCorrect()) return;
        Config config = createConfigFromFields();
        boolean csv = generateCSV.isSelected();
        observer.startSimulation(config, csv);
    }


    @FXML
    void onNewConfigClicked(ActionEvent event) {
        for (int i = 0; i < 12; i++) {
            textFields[i].setText("");
        }
        setToggleGroupActiveValue(map_type, 0);
        setToggleGroupActiveValue(plant_type, 0);
        setToggleGroupActiveValue(behaviour_type, 0);
        setToggleGroupActiveValue(mutation_type, 0);
    }

    @FXML
    void onOpenConfigClicked(ActionEvent event) {
        File file = observer.openConfigFile();
        if(file == null) return;
        try {
            Config config = configLoader.loadConfigFromFile(file);
            loadConfigFields(config);
        } catch (FileNotFoundException | InvalidConfigException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occured");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void loadBuiltInConfigByName(String configFileName) {
        try {
            Config config = configLoader.loadConfig(configFileName);
            loadConfigFields(config);
            isConfigBuiltIn = true;
        } catch (InvalidConfigException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConfigFields(Config config){
        for (int i = 0; i < 12; i++) {
            ConfigOption option = ConfigOption.values()[i];
            textFields[i].setText(config.getValue(option) + "");
        }
        setToggleGroupActiveValue(map_type, config.getValue(ConfigOption.MAP_TYPE));
        setToggleGroupActiveValue(plant_type, config.getValue(ConfigOption.PLANTS_TYPE));
        setToggleGroupActiveValue(behaviour_type, config.getValue(ConfigOption.ANIMAL_BEHAVIOUR_TYPE));
        setToggleGroupActiveValue(mutation_type, config.getValue(ConfigOption.MUTATION_TYPE));
    }

    private int getFieldValue(TextField textField) {
        return Integer.parseInt(textField.getCharacters().toString());
    }

    private Config createConfigFromFields() {
        Config simulationConfig = new Config();
        for (int i = 0; i < 12; i++) {
            simulationConfig.addValue(ConfigOption.values()[i], getFieldValue(textFields[i]));
        }
        simulationConfig.addValue(ConfigOption.MAP_TYPE, getToggleGroupActiveIndex(map_type));
        simulationConfig.addValue(ConfigOption.PLANTS_TYPE, getToggleGroupActiveIndex(plant_type));
        simulationConfig.addValue(ConfigOption.MUTATION_TYPE, getToggleGroupActiveIndex(mutation_type));
        simulationConfig.addValue(ConfigOption.ANIMAL_BEHAVIOUR_TYPE, getToggleGroupActiveIndex(behaviour_type));
        return simulationConfig;
    }


    private boolean hasAllFieldsCorrect() {
        boolean valid = hasAllFieldsNumbers() && hasMinLessThanMax()
                && hasFieldsPositiveValues() && hasEnergyFieldsCorrect() && hasGenomeCorrect();
        markFieldsValidOrNot();
        return valid;
    }

    private boolean hasGenomeCorrect() {
        int genome = getFieldValue(genomeLengthInput);
        int maxM = getFieldValue(maxMutationInput);
        boolean valid = maxM <= genome;
        isFieldValidMap.put(genomeLengthInput, valid);
        isFieldValidMap.put(maxMutationInput, valid);
        return valid;
    }

    private void markFieldsValidOrNot() {
        for (TextField textField : textFields) {
            if(!isFieldValidMap.containsKey(textField)) continue;
            boolean valid = isFieldValidMap.get(textField);
            if(valid){
                setTextFieldValid(textField);
            }else{
                setTextFieldInvalid(textField);
            }
        }
    }

    private boolean hasEnergyFieldsCorrect() {
        int needed = getFieldValue(animalNeededEnergyInput);
        int childEnergy = getFieldValue(animalChildEnergyInput);
        boolean valid = childEnergy <= needed;
        isFieldValidMap.put(animalChildEnergyInput, valid);
        isFieldValidMap.put(animalNeededEnergyInput, valid);
        return valid;
    }

    private boolean hasFieldsPositiveValues() {
        int width = getFieldValue(widthInput);
        int height = getFieldValue(heightInput);
        int energy = getFieldValue(animalEnergyInput);
        int genome = getFieldValue(genomeLengthInput);
        isFieldValidMap.put(widthInput, width > 0);
        isFieldValidMap.put(heightInput, height > 0);
        isFieldValidMap.put(animalEnergyInput, energy > 0);
        isFieldValidMap.put(genomeLengthInput, genome > 0);
        return width > 0 && height > 0 && energy > 0 && genome > 0;
    }

    private boolean hasMinLessThanMax() {
        int min = getFieldValue(minMutationInput);
        int max = getFieldValue(maxMutationInput);
        boolean valid = min <= max;
        isFieldValidMap.put(minMutationInput, valid);
        isFieldValidMap.put(maxMutationInput, valid);
        return valid;
    }

    private boolean hasAllFieldsNumbers() {
        boolean correct = true;
        for (TextField textField : textFields) {
            boolean isNumber = checkIfNumber(textField);
            isFieldValidMap.put(textField, isNumber);
            if(!isNumber){
                correct = false;
            }
        }
        return correct;
    }

    private boolean checkIfNumber(TextField textField) {
        CharSequence characters = textField.getCharacters();
        if(characters.length() == 0) return false;
        return characters.codePoints().allMatch(this::isCharNumber);
    }

    private boolean isCharNumber(int c){
        return c >= '0' && c <= '9';
    }

    private int getToggleGroupActiveIndex(ToggleGroup group){
        return group.getToggles().indexOf(group.getSelectedToggle());
    }

    private void setToggleGroupActiveValue(ToggleGroup group, int value){
        group.selectToggle(group.getToggles().get(value));
    }

    private void setTextFieldInvalid(TextField textField){
        textField.setStyle("-fx-text-box-border: #ff0000; -fx-focus-color: #ff0000;");
    }

    private void setTextFieldValid(TextField textField){
        textField.setStyle("-fx-text-box-border: #009100; -fx-focus-color: #009100;");
    }

    public void setObserver(IFXObserver observer) {
        this.observer = observer;
    }
}