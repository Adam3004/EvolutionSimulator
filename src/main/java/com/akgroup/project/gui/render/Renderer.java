package com.akgroup.project.gui.render;

import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Renderer {

    protected final float cellSize;
    protected final GridPane grid;

    protected final int height;
    private final Color JUNGLE_COLOR = Color.rgb(44, 105, 12);

    protected Renderer(float cellSize, GridPane grid, int height) {
        this.cellSize = cellSize;
        this.grid = grid;
        this.height = height;
    }

    public abstract void renderPlant(Plant plant);

    public abstract void renderAnimal(Animal animal);

    public void renderJungle(IWorldMap worldMap){
        Vector2D lowerLeft = worldMap.getJungleLowerLeft();
        Vector2D upperRight = worldMap.getJungleUpperRight();
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(JUNGLE_COLOR);
        int widthJ = upperRight.x - lowerLeft.x + 1;
        int heightJ = upperRight.y - lowerLeft.y + 1;
        rectangle.setWidth(cellSize * widthJ);
        rectangle.setHeight(cellSize * heightJ);
        grid.add(rectangle, lowerLeft.x, lowerLeft.y, widthJ, heightJ);
    }

    public abstract void markAnimal(Animal animal);
}
