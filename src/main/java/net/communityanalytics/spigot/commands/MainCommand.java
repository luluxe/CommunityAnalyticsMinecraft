package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private final SpigotPlugin plugin;

    public MainCommand(SpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String first_arg = args[0];
        if (first_arg.equals("reload")) {
            this.plugin.reloadConfig();
            this.plugin.loadConfiguration();
            sender.sendMessage("§aConfig reload!");
        }
        else if (first_arg.equals("setup")) {
            sender.sendMessage("§aThe plugin is already setup!");
        }
        else if (first_arg.equals("test")) {
            sender.sendMessage("§aThe plugin is well connected!");
        }
        else {
            sender.sendMessage("(CommunityAnalytics) &7Plugin information:");
            sender.sendMessage("- Version: §a" + this.plugin.getDescription().getVersion());
            sender.sendMessage("- Platform: §a"); // Platform
            sender.sendMessage("§a/community reload");
        }

        return true;
    }
}
