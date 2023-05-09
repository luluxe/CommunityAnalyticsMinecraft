package net.communityanalytics.spigot.commands;

import net.communityanalytics.common.SentryManager;
import net.communityanalytics.spigot.SpigotAPI;
import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.api.ApiResponse;
import org.bukkit.command.CommandSender;

public class MainCommand extends Command {
    public MainCommand(String name, String[] args, CommandSender sender) {
        super(name, args, sender);
    }

    @Override
    protected void execute(SpigotPlugin plugin) {
        try {
            ApiResponse response = SpigotAPI.platformShow().sendRequest();
            if (response.getStatus() == 403) {
                sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cCan't auth to API:");
                sender.sendMessage("§e» §cCheck your token in config.yml");
                return;
            }
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §7Plugin information:");
            sender.sendMessage("§e» §7Status: §aConnected");
            sender.sendMessage("§e» §7Version §a" + plugin.getDescription().getVersion() + " §7(Dernière §e" + response.getStringArg("plugin_version") + "§7)");
            sender.sendMessage("§e» §7Community: §b" + response.getStringArg("community_name")); // Platform
            sender.sendMessage("§e» §7Platform: §b" + response.getStringArg("platform_name")); // Platform
            sender.sendMessage("§e» §a/community reload §7(Reload configuration)");
        } catch (Exception e) {
            SentryManager.capture(e);
            e.printStackTrace();
            sender.sendMessage("§cError: " + e.getMessage());
        }
    }
}
