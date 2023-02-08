package net.communityanalytics.bungee;

import net.communityanalytics.common.Session;
import net.communityanalytics.common.SessionManager;
import net.communityanalytics.common.utils.ConfigLoader;
import net.communityanalytics.common.utils.ILogger;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AnalyticsPlugin extends Plugin implements Listener {

    private boolean isEnable = false;
    private final SessionManager manager = new SessionManager();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void onEnable() {
        this.isEnable = true;

        ILogger logger = new BungeeLogger(this);
        this.manager.setLogger(logger);

        logger.printInfo("Loading the plugin !");

        getProxy().getPluginManager().registerListener(this, this);

        try {

            if (!getDataFolder().exists() && getDataFolder().mkdir()) {
                logger.printInfo("Created config folder: " + getDataFolder().getAbsolutePath());
            }

            File file = new File(getDataFolder(), "config.yml");

            File configFile = new File(getDataFolder(), "config.yml");

            // Copy default config if it doesn't exist
            if (!configFile.exists()) {
                FileOutputStream outputStream = new FileOutputStream(configFile); // Throws IOException
                InputStream in = getResourceAsStream("config.yml"); // This file must exist in the jar resources folder
                in.transferTo(outputStream); // Throws IOException
            }

            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));

            ConfigLoader loader = new BungeeConfigLoader(configuration);
            manager.setConfig(loader.loadConfig());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.isEnable = true;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public SessionManager getManager() {
        return manager;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        scheduledExecutorService.execute(() -> {
            ProxiedPlayer player = event.getPlayer();
            String hostName = player.getPendingConnection().getVirtualHost().getHostString();
            String playerIp = player.getAddress().getHostName();
            Session session = new Session(player.getUniqueId(), player.getName(), hostName, playerIp);
            this.manager.add(session);
        });
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Optional<Session> optionalSession = this.manager.find(player.getUniqueId());
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            session.finish();
        }
    }

}
