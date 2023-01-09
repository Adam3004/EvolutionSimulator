package com.akgroup.project.gui.render;

import com.akgroup.project.gui.texture.TextureManager;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.map.IWorldMap;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public abstract class Renderer {

    protected final float cellSize;
    protected final GridPane grid;

    protected final int height;

    protected Renderer(float cellSize, GridPane grid, int height) {
        this.cellSize = cellSize;
        this.grid = grid;
        this.height = height;
        TextureManager.loadTexture("jungle");
        TextureManager.loadTexture("sand");
    }

    public abstract void renderPlant(Plant plant);

    public abstract void renderAnimal(Animal animal);

    public void renderJungle(IWorldMap worldMap){
        Vector2D lowerLeft = worldMap.getJungleLowerLeft();
        Vector2D upperRight = worldMap.getJungleUpperRight();
        int widthJ = upperRight.x - lowerLeft.x + 1;
        int heightJ = upperRight.y - lowerLeft.y + 1;
        ImageView imageView = new ImageView(TextureManager.getTexture("jungle"));
        imageView.setFitHeight(cellSize*heightJ);
        imageView.setFitWidth(cellSize*widthJ);
        grid.add(imageView, lowerLeft.x, lowerLeft.y, widthJ, heightJ);
    }

    public abstract void markAnimal(Animal animal);

    public void renderGridBackground() {
        BackgroundSize size = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage image = new BackgroundImage(TextureManager.getTexture("sand"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,  BackgroundPosition.DEFAULT, size);
        grid.setBackground(new Background(image));
    }
}
