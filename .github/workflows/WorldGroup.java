package com.example.spawngroup;

import org.bukkit.Location;

import java.util.List;

/**
 * Mewakili satu group world (contoh: "survival" berisi world, world_nether,
 * world_the_end) beserta satu titik spawn tujuan (biasanya di lobby).
 */
public class WorldGroup {

    private final String name;
    private final List<String> worlds;
    private Location spawnLocation;

    public WorldGroup(String name, List<String> worlds, Location spawnLocation) {
        this.name = name;
        this.worlds = worlds;
        this.spawnLocation = spawnLocation;
    }

    public String getName() {
        return name;
    }

    public List<String> getWorlds() {
        return worlds;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
