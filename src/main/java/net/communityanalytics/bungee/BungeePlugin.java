package net.communityanalytics.bungee;

import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.bungee.listeners.PlayerInfoListener;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin {
    public static BungeePlugin instance;

    public BungeePlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // listeners
        getProxy().getPluginManager().registerListener(this, new PlayerInfoListener());

        // channels
        getProxy().registerChannel(CommunityAnalytics.CHANNEL_INFO);
    }
}
