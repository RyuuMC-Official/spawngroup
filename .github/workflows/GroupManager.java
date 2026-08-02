package com.example.spawngroup;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Membaca config.yml, membangun daftar WorldGroup, dan menyediakan
 * pencarian cepat "world ini masuk group mana".
 */
public class GroupManager {

    private final SpawnGroupPlugin plugin;
    private final Map<String, WorldGroup> groups = new HashMap<>();
    // key: nama world (lowercase) -> key group (lowercase)
    private final Map<String, String> worldToGroup = new HashMap<>();

    public GroupManager(SpawnGroupPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadGroups() {
        groups.clear();
        worldToGroup.clear();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection groupsSection = config.getConfigurationSection("groups");
        if (groupsSection == null) {
            plugin.getLogger().warning("Tidak ada section 'groups' di config.yml!");
            return;
        }

        for (String groupName : groupsSection.getKeys(false)) {
            ConfigurationSection groupSection = groupsSection.getConfigurationSection(groupName);
            if (groupSection == null) continue;

            List<String> worlds = groupSection.getStringList("worlds");

            Location spawnLocation = null;
            ConfigurationSection spawnSection = groupSection.getConfigurationSection("spawn");
            if (spawnSection != null) {
                String worldName = spawnSection.getString("world");
                World world = worldName != null ? Bukkit.getWorld(worldName) : null;

                if (world != null) {
                    double x = spawnSection.getDouble("x");
                    double y = spawnSection.getDouble("y");
                    double z = spawnSection.getDouble("z");
                    float yaw = (float) spawnSection.getDouble("yaw");
                    float pitch = (float) spawnSection.getDouble("pitch");
                    spawnLocation = new Location(world, x, y, z, yaw, pitch);
                } else if (worldName != null) {
                    plugin.getLogger().warning("World spawn '" + worldName + "' untuk group '"
                            + groupName + "' belum ter-load saat plugin start. "
                            + "Spawn akan dianggap belum diset sampai kamu jalankan /spawngroup setspawn.");
                }
            }

            WorldGroup group = new WorldGroup(groupName, worlds, spawnLocation);
            groups.put(groupName.toLowerCase(), group);

            for (String w : worlds) {
                worldToGroup.put(w.toLowerCase(), groupName.toLowerCase());
            }
        }
    }

    public void reload() {
        plugin.reloadConfig();
        loadGroups();
    }

    /** Cari group berdasarkan nama world tempat player berdiri sekarang. */
    public WorldGroup getGroupByWorld(String worldName) {
        String groupKey = worldToGroup.get(worldName.toLowerCase());
        if (groupKey == null) return null;
        return groups.get(groupKey);
    }

    public WorldGroup getGroup(String groupName) {
        return groups.get(groupName.toLowerCase());
    }

    public Map<String, WorldGroup> getGroups() {
        return groups;
    }

    /** Set + simpan lokasi spawn sebuah group ke config.yml. */
    public void setGroupSpawn(String groupName, Location location) {
        WorldGroup group = groups.get(groupName.toLowerCase());
        if (group == null) return;
        group.setSpawnLocation(location);

        FileConfiguration config = plugin.getConfig();
        String path = "groups." + group.getName() + ".spawn";
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
        plugin.saveConfig();
    }
}
