package net.communityanalytics.bungee;

import net.communityanalytics.common.SessionManager;
import net.communityanalytics.common.utils.ILogger;

public class BungeeLogger implements ILogger {

    private final AnalyticsPlugin plugin;

    public BungeeLogger(AnalyticsPlugin plugin) {
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

    @Override
    public boolean isPluginEnable() {
        return this.plugin.isEnable();
    }
}
