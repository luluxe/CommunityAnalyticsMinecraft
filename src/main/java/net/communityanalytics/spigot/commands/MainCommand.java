package net.communityanalytics.spigot.commands;

import net.communityanalytics.common.SentryManager;
import net.communityanalytics.spigot.SpigotAPI;
import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.api.ApiResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final SpigotPlugin plugin;

    public MainCommand(SpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // TODO class for each command

        // Main command
        if (args.length == 0) {
            try {
                ApiResponse response = SpigotAPI.platformShow().sendRequest();
                if (response.getStatus() == 403) {
                    sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cCan't auth to API:");
                    sender.sendMessage("§e» §cCheck your token in config.yml");
                    return true;
                }

                sender.sendMessage("§f(§b§lCommunityAnalytics§f) §7Plugin information:");
                sender.sendMessage("§e» §7Status: §aConnected");
                sender.sendMessage("§e» §7Version §a" + this.plugin.getDescription().getVersion() + " §7(Dernière §e" + response.getStringArg("plugin_version") + "§7)");
                sender.sendMessage("§e» §7Community: §b" + response.getStringArg("community_name")); // Platform
                sender.sendMessage("§e» §7Platform: §b" + response.getStringArg("platform_name")); // Platform
                sender.sendMessage("§e» §a/community reload §7(Reload configuration)");
            } catch (Exception e) {
                SentryManager.capture(e);
                e.printStackTrace();
                sender.sendMessage("§cError: " + e.getMessage());
            }
            return true;
        }

        String first_arg = args[0];

        // Reload command
        if (first_arg.equals("reload")) {
            this.plugin.reload();
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aConfig reload!");
        }

        return true;
    }
}
