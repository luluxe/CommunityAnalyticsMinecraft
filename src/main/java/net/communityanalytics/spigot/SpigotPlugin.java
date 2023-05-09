package net.communityanalytics.spigot;

import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.common.SentryManager;
import net.communityanalytics.common.interfaces.ConfigLoader;
import net.communityanalytics.common.interfaces.ILogger;
import net.communityanalytics.spigot.commands.DispatchCommand;
import net.communityanalytics.spigot.configs.SpigotConfig;
import net.communityanalytics.spigot.configs.SpigotConfigLoader;
import net.communityanalytics.spigot.listeners.SessionListener;
import net.communityanalytics.spigot.listeners.UpdateListener;
import net.communityanalytics.spigot.managers.PlatformManager;
import net.communityanalytics.spigot.managers.SessionManager;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotPlugin extends JavaPlugin {
    public static SpigotPlugin instance;
    private SessionManager manager = null;
    private ILogger logger = null;
    private SpigotConfig config = null;
    private PlatformManager platformManager = null;

    public SpigotPlugin() {
        SentryManager.init();

        instance = this;
    }

    public static SessionManager manager() {
        return instance.manager;
    }

    public static ILogger logger() {
        return instance.logger;
    }

    public static SpigotConfig config() {
        return instance.config;
    }

    public static PlatformManager platformManager() {
        return instance.platformManager;
    }

    @Override
    public void onEnable() {
        logger = new SpigotLogger(this);

        // listeners
        this.getServer().getPluginManager().registerEvents(new SessionListener(), this);
        this.getServer().getPluginManager().registerEvents(new UpdateListener(), this);

        // configs
        this.saveDefaultConfig();
        this.loadConfiguration();

        // commands
        this.getCommand("communityanalytics").setExecutor(new DispatchCommand(this));

        // channels
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, CommunityAnalytics.CHANNEL_INFO);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, CommunityAnalytics.CHANNEL_INFO, new SessionListener());

        // managers
        manager = new SessionManager();
        platformManager = new PlatformManager();
        platformManager.getPlatformInfo();

        // All you have to do is adding the following two lines in your onEnable method.
        // You can find the plugin ids of your plugins on the page https://bstats.org/what-is-my-plugin-id
        int pluginId = 17845; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("connected_to_api", ()
                -> platformManager.isSuccess() ? "Yes" : "No"));
    }

    @Override
    public void onDisable() {
        manager.sendAPI();

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    // Utils

    public void reload() {
        this.reloadConfig();
        this.loadConfiguration();
        platformManager.getPlatformInfo();
    }

    public void loadConfiguration() {

        YamlConfiguration yamlConfiguration = (YamlConfiguration) this.getConfig();
        ConfigLoader loader = new SpigotConfigLoader(yamlConfiguration);
        config = loader.loadConfig();

    }
}
