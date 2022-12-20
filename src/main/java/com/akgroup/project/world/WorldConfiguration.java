package com.akgroup.project.world;

import com.akgroup.project.world.borders.EarthBorders;
import com.akgroup.project.world.borders.MapBorders;

public record WorldConfiguration(MapBorders mapBorders) {

    public static WorldConfiguration defaultConfiguration() {
        return new WorldConfiguration(new EarthBorders());
    }
}
