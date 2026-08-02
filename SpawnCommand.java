package com.example.spawngroup;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * /spawn            -> teleport ke spawn group dari world player saat ini
 * /spawn <group>     -> (butuh izin spawngroup.spawn.others) teleport ke spawn group tertentu
 */
public class SpawnCommand implements CommandExecutor, TabCompleter {

    private final GroupManager groupManager;

    public SpawnCommand(SpawnGroupPlugin plugin, GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Command ini hanya bisa dipakai oleh player.");
            return true;
        }

        if (args.length >= 1) {
            if (!player.hasPermission("spawngroup.spawn.others")) {
                player.sendMessage(ChatColor.RED + "Kamu tidak punya izin untuk pindah ke group lain.");
                return true;
            }
            WorldGroup target = groupManager.getGroup(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Group '" + args[0] + "' tidak ditemukan.");
                return true;
            }
            teleportToGroup(player, target);
            return true;
        }

        WorldGroup group = groupManager.getGroupByWorld(player.getWorld().getName());
        if (group == null) {
            player.sendMessage(ChatColor.RED + "World '" + player.getWorld().getName()
                    + "' belum terdaftar di group manapun. Hubungi admin.");
            return true;
        }

        teleportToGroup(player, group);
        return true;
    }

    private void teleportToGroup(Player player, WorldGroup group) {
        Location spawn = group.getSpawnLocation();
        if (spawn == null || spawn.getWorld() == null) {
            player.sendMessage(ChatColor.RED + "Spawn untuk group '" + group.getName()
                    + "' belum diatur. Hubungi admin (/spawngroup setspawn " + group.getName() + ").");
            return;
        }
        player.teleport(spawn);
        player.sendMessage(ChatColor.GREEN + "Teleport ke spawn '" + group.getName() + "'.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("spawngroup.spawn.others")) {
            String partial = args[0].toLowerCase();
            return groupManager.getGroups().keySet().stream()
                    .filter(name -> name.startsWith(partial))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
