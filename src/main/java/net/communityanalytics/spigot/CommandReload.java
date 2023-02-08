package net.communityanalytics.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandReload implements CommandExecutor {

    private final AnalyticsPlugin plugin;

    public CommandReload(AnalyticsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        this.plugin.reloadConfig();
        this.plugin.loadConfiguration();

        sender.sendMessage("§aConfig reload !");

        return true;
    }
}
