package net.communityanalytics.spigot;

import net.communityanalytics.common.utils.ILogger;

public class SpigotLogger implements ILogger {

    private final AnalyticsPlugin plugin;

    public SpigotLogger(AnalyticsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void printDebug(String message) {
        if (this.plugin.getManager().getConfig().isDebug()) {
            this.plugin.getLogger().info(message);
        }
    }

    @Override
    public void printInfo(String message) {
        this.plugin.getLogger().info(message);
    }

    @Override
    public void printError(String message) {
        this.plugin.getLogger().severe(message);
    }
}
