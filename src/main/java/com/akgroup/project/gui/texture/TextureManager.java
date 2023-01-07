package com.akgroup.project.gui.texture;

import com.akgroup.project.Launcher;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

public class TextureManager {
    private static final HashMap<String, Image> imageMap = new HashMap<>();
    public static Image loadTexture(String resourcePath){
        Image image;
        if(imageMap.containsKey(resourcePath)){
            image = imageMap.get(resourcePath);
        }else{
            InputStream stream = Launcher.class.getResourceAsStream("/textures/%s.png".formatted(resourcePath));
            image = new Image(stream);
            imageMap.put(resourcePath, image);
        }
        return image;
    }

    public static Image getTexture(String name) {
        return imageMap.get(name);
    }
}
