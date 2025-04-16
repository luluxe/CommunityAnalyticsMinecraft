package net.communityanalytics.spigot.listeners;

import net.communityanalytics.spigot.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(SpigotPlugin.instance, () -> {
            Player player = event.getPlayer();
            if (SpigotPlugin.platformManager().isSuccess() && SpigotPlugin.platformManager().isOutdated() && player.hasPermission("communityanalytics.admin")) {
                player.sendMessage("§f(§b§lCommunityAnalytics§f) §cPlugin is outdated! Download the latest version at §7https://communityanalytics.net/download");
            }
        }, 20L);
    }
}
