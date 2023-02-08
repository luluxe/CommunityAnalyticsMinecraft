package net.communityanalytics.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandExecutor {

    private final AnalyticsPlugin plugin;

    public CommandReload(AnalyticsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        this.plugin.reloadConfig();
        this.plugin.loadConfiguration();

        sender.sendMessage("§aConfig reload !");

        return true;
    }
}
