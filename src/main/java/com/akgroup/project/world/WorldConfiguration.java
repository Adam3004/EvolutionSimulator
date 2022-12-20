package com.akgroup.project.world;

import com.akgroup.project.world.borders.EarthBorders;
import com.akgroup.project.world.borders.MapBorders;
import com.akgroup.project.world.map.WorldMap;

public record WorldConfiguration(MapBorders mapBorders) {

    public static WorldConfiguration defaultConfiguration() {
        return new WorldConfiguration(new EarthBorders());
    }

    public MapBorders getMapBorders() {
        return mapBorders;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.mapBorders.setWorldMap(worldMap);
        // other setters
    }
}
