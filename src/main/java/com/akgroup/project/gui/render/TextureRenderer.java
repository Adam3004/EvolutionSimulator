package com.akgroup.project.gui.render;

import com.akgroup.project.gui.texture.TextureManager;
import com.akgroup.project.util.Vector2D;
import com.akgroup.project.world.object.Animal;
import com.akgroup.project.world.object.Plant;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/** Renders plants and animals as textures (ImageViews). Used for small map.*/
public class TextureRenderer extends Renderer{

    private final static double SINGLE_ROTATION = (double)360 / 8;

    public TextureRenderer(float cellSize, GridPane grid, int height) {
        super(cellSize, grid, height);
        initTextures();
    }

    private void initTextures() {
        TextureManager.loadTexture("flower");
        TextureManager.loadTexture("bee");
        TextureManager.loadTexture("bee_crown");
    }

    private ImageView createImageView(String textureName){
        ImageView imageView = new ImageView(TextureManager.getTexture(textureName));
        imageView.setFitWidth(cellSize);
        imageView.setFitHeight(cellSize);
        return imageView;
    }

    @Override
    public void renderPlant(Plant plant) {
        ImageView imageView = createImageView("flower");
        Vector2D position = plant.getPosition();
        grid.add(imageView, position.x, height - position.y - 1);
    }

    @Override
    public void renderAnimal(Animal animal) {
        ImageView imageView = createImageView("bee");
        imageView.setRotate(animal.getRotation() * SINGLE_ROTATION);
        Vector2D position = animal.getPosition();
        grid.add(imageView, position.x, height - position.y - 1);
    }

    @Override
    public void markAnimal(Animal animal) {
        ImageView imageView = createImageView("bee_crown");
        imageView.setRotate(animal.getRotation() * SINGLE_ROTATION);
        Vector2D position = animal.getPosition();
        grid.add(imageView, position.x, height - position.y - 1);
    }
}
