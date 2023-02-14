package net.communityanalytics.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.data.Session;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SessionListener implements Listener, PluginMessageListener {
    private final Map<UUID, String> ip_connect_players = new HashMap<>();

    /**
     * Get ip_connect : if the proxy does not respond
     *
     * @param event PlayerLoginEvent
     */
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        this.ip_connect_players.put(event.getPlayer().getUniqueId(), event.getHostname());
    }

    /**
     * Start track session
     * + get the ip : if the proxy does not respond
     *
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String ip_connect = this.ip_connect_players.getOrDefault(player.getUniqueId(), "");
        this.ip_connect_players.remove(player.getUniqueId());

        String ip_user = player.getAddress().getHostString();
        Session session = new Session(player.getUniqueId(), player.getName(), ip_connect, ip_user);
        SpigotPlugin.manager().add(session);

        // Send a request to proxy to get the ip and host
        Bukkit.getScheduler().runTaskLater(SpigotPlugin.instance, () -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(player.getUniqueId().toString());
            player.sendPluginMessage(SpigotPlugin.instance, CommunityAnalytics.CHANNEL_INFO, out.toByteArray());
        }, 10);
    }

    /**
     * Stop track session
     * - set end date
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Optional<Session> optionalSession = SpigotPlugin.manager().find(player.getUniqueId());
        if (optionalSession.isPresent()) {
            Session session = optionalSession.get();
            session.finish();
        }
    }

    /**
     * Get ip_connect and ip_user from proxy
     *
     * @param channel String
     * @param player  Player
     * @param message byte[]
     */
    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals(CommunityAnalytics.CHANNEL_INFO)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String host = in.readUTF();
        String ip = in.readUTF();

        Optional<Session> optionalSession = SpigotPlugin.manager().find(player.getUniqueId());
        if (optionalSession.isPresent()) {
            // Update session
            Session session = optionalSession.get();
            session.setIp_connect(host);
            session.setIp_user(ip);
        }
    }
}
