package net.communityanalytics.spigot;

import net.communityanalytics.common.Session;
import net.communityanalytics.common.SessionManager;
import net.communityanalytics.common.utils.ConfigLoader;
import net.communityanalytics.common.utils.ILogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AnalyticsPlugin extends JavaPlugin implements Listener {

    private final Map<UUID, String> hostPlayers = new HashMap<>();
    private final SessionManager manager = new SessionManager();

    @Override
    public void onEnable() {

        ILogger logger = new SpigotLogger(this);
        manager.setLogger(logger);

        logger.printInfo("Loading the plugin !");

        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();

        this.loadConfiguration();

        this.getCommand("communityanalyticsreload").setExecutor(new CommandReload(this));

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            if (!this.isEnabled()) {

            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    public void loadConfiguration() {

        YamlConfiguration yamlConfiguration = (YamlConfiguration) this.getConfig();
        ConfigLoader loader = new SpigotConfigLoader(yamlConfiguration);
        this.manager.setConfig(loader.loadConfig());

    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        this.hostPlayers.put(event.getPlayer().getUniqueId(), event.getHostname());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        String hostName = this.hostPlayers.getOrDefault(player.getUniqueId(), "");
        this.hostPlayers.remove(player.getUniqueId());

        String playerIp = player.getAddress().getHostString();
        Session session = new Session(player.getUniqueId(), player.getName(), hostName, playerIp);
        this.manager.add(session);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Optional<Session> optionalSession = this.manager.find(player.getUniqueId());
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            session.finish();
        }
    }

    public SessionManager getManager() {
        return manager;
    }
}
