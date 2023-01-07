package com.akgroup.project.gui.render;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** Renders Animals and Plants as simple circles on the grid.
 * Used for huge map on which we don't want to use normal textures*/
public class SimpleRenderer extends Renderer{

    private final Color GRASS_COLOR = Color.rgb(55, 186, 32);
    private final Color ANIMAL_MARK_COLOR = Color.rgb(66, 135, 245);
    private final Color ANIMAL_HIGH_ENERGY_COLOR = Color.rgb(255, 59, 131);
    private final Color ANIMAL_LOW_ENERGY_COLOR = Color.rgb(255, 0, 0);
    private final Color ANIMAL_DYING_ENERGY_COLOR = Color.rgb(40, 40, 40);


    public SimpleRenderer(float cellSize, GridPane grid, int height) {
        super(cellSize, grid, height);
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

    public void markAnimal(Animal animal) {
        Vector2D position = animal.getPosition();
        Circle circle = createCircle(ANIMAL_MARK_COLOR);
        addCircleToGrid(circle, position.x, height - position.y - 1);
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

    @Override
    public void renderPlant(Plant plant) {
        Vector2D position = plant.getPosition();
        Circle circle = createCircle(GRASS_COLOR);
        addCircleToGrid(circle, position.x, height - position.y - 1);
    }

    @Override
    public void renderAnimal(Animal animal) {
        Vector2D position = animal.getPosition();
        Color color = getColorByEnergy(animal.getEnergy());
        Circle circle = createCircle(color);
        addCircleToGrid(circle, position.x, height - position.y - 1);
    }
}
