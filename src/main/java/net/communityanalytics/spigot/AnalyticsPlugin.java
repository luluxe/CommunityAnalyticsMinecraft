package net.communityanalytics.spigot;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.communityanalytics.common.Session;
import net.communityanalytics.common.SessionManager;
import net.communityanalytics.common.utils.ConfigLoader;
import net.communityanalytics.common.utils.ILogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AnalyticsPlugin extends JavaPlugin implements Listener, PluginMessageListener {

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

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, SessionManager.channelName);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, SessionManager.channelName, this);
    }

    public void loadConfiguration() {

        YamlConfiguration yamlConfiguration = (YamlConfiguration) this.getConfig();
        ConfigLoader loader = new SpigotConfigLoader(yamlConfiguration);
        this.manager.setConfig(loader.loadConfig());

    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
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

        // Send a request to bungeecord to get the ip and host
        Bukkit.getScheduler().runTaskLater(this, () -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(player.getUniqueId().toString());
            player.sendPluginMessage(this, SessionManager.channelName, out.toByteArray());
        }, 10);
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

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (!channel.equals(SessionManager.channelName)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String host = in.readUTF();
        String ip = in.readUTF();

        Optional<Session> optionalSession = this.manager.find(player.getUniqueId());
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            session.setIpConnect(host);
            session.setIpPlayer(ip);
        }
    }
}
