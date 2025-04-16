package net.communityanalytics.bungee;

import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.bungee.listeners.PlayerInfoListener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

public class BungeePlugin extends Plugin {
    public static BungeePlugin instance;

    public BungeePlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("Make your minecraft server grow, with data!");
        getLogger().info("=> https://communityanalytics.net/");

        // listeners
        getProxy().getPluginManager().registerListener(this, new PlayerInfoListener());

        // channels
        getProxy().registerChannel(CommunityAnalytics.CHANNEL_INFO);

        int pluginId = 17952; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
    }
}
