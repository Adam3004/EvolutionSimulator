package com.akgroup.project.gui;

import com.akgroup.project.config.Config;
import com.akgroup.project.config.ConfigOption;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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

    private TextField[] textFields;
    private IFXObserver observer;

    public SimulationConfigController() {}

    @FXML
    public void startSimulation(ActionEvent actionEvent) {
        textFields = new TextField[]{widthInput, heightInput, startPlantInput, plantEnergyInput,
                plantEveryDayInput, animalsStartInput, animalEnergyInput, animalNeededEnergyInput, animalChildEnergyInput,
                minMutationInput, maxMutationInput, genomeLengthInput};
        System.out.println("1");
        if(!hasAllFieldsCorrect()) return; //TODO display error message on window
        System.out.println("2");
        Config config = createConfigFromFields();
        System.out.println("3");
        observer.startSimulation(config);
    }

    private int getFieldValue(TextField textField) {
        return Integer.parseInt(textField.getCharacters().toString());
    }

    private Config createConfigFromFields() {
        Config simulationConfig = new Config();
        for (int i = 0; i < 12; i++) {
            simulationConfig.addValue(ConfigOption.values()[i], getFieldValue(textFields[i]));
        }
        simulationConfig.addValue(ConfigOption.MAP_TYPE, getMapTypeValue());
        simulationConfig.addValue(ConfigOption.PLANTS_TYPE, getPlantTypeValue());
        simulationConfig.addValue(ConfigOption.MUTATION_TYPE, getMutationTypeValue());
        simulationConfig.addValue(ConfigOption.ANIMAL_BEHAVIOUR_TYPE, getBehaviourTypeValue());
        return simulationConfig;
    }

    private boolean hasAllFieldsCorrect() {
        for (TextField textField : textFields) {
            boolean isNumber = checkIfNumber(textField);
            if(!isNumber) return false;
        }
        return true;
    }

    private boolean checkIfNumber(TextField textField) {
        CharSequence characters = textField.getCharacters();
        if(characters.length() == 0) return false;
        return characters.codePoints().allMatch(this::isCharNumber);
    }

    private boolean isCharNumber(int c){
        return c >= '0' && c <= '9';
    }

    private int getMapTypeValue(){
        String value = getToggleGroupValue(map_type);
        return value.equals("Kula ziemska") ? 1 : 0;
    }

    private int getPlantTypeValue(){
        String value = getToggleGroupValue(plant_type);
        return value.equals("Zalesione równiki") ? 1 : 0;
    }

    private int getMutationTypeValue(){
        String value = getToggleGroupValue(mutation_type);
        return value.equals("Pełna losowość") ? 1 : 0;
    }

    private int getBehaviourTypeValue(){
        String value = getToggleGroupValue(behaviour_type);
        return value.equals("Pełna predestynacja") ? 1 : 0;
    }

    private String getToggleGroupValue(ToggleGroup group) {
        return ((RadioButton) group.getSelectedToggle()).getText();
    }

    public void setObserver(IFXObserver observer) {
        this.observer = observer;
    }
}