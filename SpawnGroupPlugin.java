package com.example.spawngroup;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnGroupPlugin extends JavaPlugin {

    private GroupManager groupManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.groupManager = new GroupManager(this);
        groupManager.loadGroups();

        SpawnCommand spawnCommand = new SpawnCommand(this, groupManager);
        PluginCommand spawnCmd = getCommand("spawn");
        if (spawnCmd != null) {
            spawnCmd.setExecutor(spawnCommand);
            spawnCmd.setTabCompleter(spawnCommand);
        }

        AdminCommand adminCommand = new AdminCommand(this, groupManager);
        PluginCommand adminCmd = getCommand("spawngroup");
        if (adminCmd != null) {
            adminCmd.setExecutor(adminCommand);
            adminCmd.setTabCompleter(adminCommand);
        }

        getLogger().info("SpawnGroup aktif! " + groupManager.getGroups().size() + " group dimuat.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SpawnGroup dimatikan.");
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}
