package net.communityanalytics.spigot;

import net.communityanalytics.common.interfaces.ILogger;

public class SpigotLogger implements ILogger {
    private final SpigotPlugin plugin;

    public SpigotLogger(SpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void printDebug(String message) {
        if (SpigotPlugin.config().isDebug()) {
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
        return this.plugin.isEnabled();
    }
}
