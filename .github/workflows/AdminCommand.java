package com.example.spawngroup;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * /spawngroup reload
 * /spawngroup list
 * /spawngroup setspawn <group>
 */
public class AdminCommand implements CommandExecutor, TabCompleter {

    private final GroupManager groupManager;

    public AdminCommand(SpawnGroupPlugin plugin, GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("spawngroup.admin")) {
            sender.sendMessage(ChatColor.RED + "Kamu tidak punya izin untuk pakai command ini.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Gunakan: /spawngroup <reload|setspawn|list> [group]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                groupManager.reload();
                sender.sendMessage(ChatColor.GREEN + "Config SpawnGroup di-reload. "
                        + groupManager.getGroups().size() + " group dimuat.");
            }
            case "list" -> {
                if (groupManager.getGroups().isEmpty()) {
                    sender.sendMessage(ChatColor.YELLOW + "Belum ada group terdaftar.");
                    return true;
                }
                sender.sendMessage(ChatColor.GOLD + "=== Daftar Spawn Group ===");
                groupManager.getGroups().values().forEach(g -> {
                    String status = g.getSpawnLocation() != null
                            ? ChatColor.GREEN + "OK"
                            : ChatColor.RED + "belum diset";
                    sender.sendMessage(ChatColor.AQUA + "- " + g.getName() + ChatColor.GRAY
                            + " (worlds: " + String.join(", ", g.getWorlds()) + ") spawn: " + status);
                });
            }
            case "setspawn" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(ChatColor.RED + "Command ini hanya bisa dipakai player.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Gunakan: /spawngroup setspawn <namaGroup>");
                    return true;
                }
                WorldGroup group = groupManager.getGroup(args[1]);
                if (group == null) {
                    player.sendMessage(ChatColor.RED + "Group '" + args[1] + "' tidak ditemukan di config.yml.");
                    return true;
                }
                groupManager.setGroupSpawn(group.getName(), player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Spawn group '" + group.getName()
                        + "' diatur ke lokasi kamu sekarang.");
            }
            default -> sender.sendMessage(ChatColor.RED
                    + "Subcommand tidak dikenal. Gunakan: /spawngroup <reload|setspawn|list> [group]");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "setspawn", "list").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("setspawn")) {
            String partial = args[1].toLowerCase();
            return groupManager.getGroups().keySet().stream()
                    .filter(name -> name.startsWith(partial))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
