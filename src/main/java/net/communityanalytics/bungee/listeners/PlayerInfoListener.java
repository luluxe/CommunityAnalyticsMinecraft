package net.communityanalytics.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.common.PlayerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PlayerInfoListener implements Listener {
    private final Map<UUID, PlayerInfo> playerInfos = new HashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    /**
     * Get player info of the player when login
     *
     * @param event PostLoginEvent
     */
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        scheduledExecutorService.execute(() -> {
            ProxiedPlayer player = event.getPlayer();
            String ip_connect = player.getPendingConnection().getVirtualHost().getHostString();
            String ip_user = player.getAddress().getHostString();
            playerInfos.put(player.getUniqueId(), new PlayerInfo(ip_connect, ip_user));
        });
    }

    /**
     * Remove player login information when logout
     *
     * @param event PlayerDisconnectEvent
     */
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        this.playerInfos.remove(player.getUniqueId());
    }

    /**
     * Sent to the server the player info : connected_ip and user_ip
     *
     * @param event PluginMessageEvent
     */
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(CommunityAnalytics.CHANNEL_INFO)) {
            return;
        }

        // Get uuid asked
        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        UUID uuid = UUID.fromString(input.readUTF());

        if (this.playerInfos.containsKey(uuid)) {
            PlayerInfo playerInfo = this.playerInfos.get(uuid);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(playerInfo.getIpConnect());
            out.writeUTF(playerInfo.getIpUser());

            if (event.getReceiver() instanceof ProxiedPlayer) {
                ServerInfo serverInfo = ((ProxiedPlayer) event.getReceiver()).getServer().getInfo();
                serverInfo.sendData(CommunityAnalytics.CHANNEL_INFO, out.toByteArray());
            }
        }
    }
}
