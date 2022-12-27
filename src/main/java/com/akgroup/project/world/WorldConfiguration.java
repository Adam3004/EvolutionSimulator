package com.akgroup.project.world;

import com.akgroup.project.world.borders.EarthBorders;
import com.akgroup.project.world.borders.MapBorders;
import com.akgroup.project.world.map.WorldMap;
import com.akgroup.project.world.mutators.FullRandomMutator;
import com.akgroup.project.world.mutators.GenomeMutator;

public record WorldConfiguration(MapBorders mapBorders, GenomeMutator mutator) {
    public static WorldConfiguration defaultConfiguration() {
        return new WorldConfiguration(new EarthBorders(), new FullRandomMutator());
    }

    public void setWorldMap(WorldMap worldMap) {
        this.mapBorders.setWorldMap(worldMap);
    }
}
