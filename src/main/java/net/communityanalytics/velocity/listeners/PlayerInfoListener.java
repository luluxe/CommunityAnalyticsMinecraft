package net.communityanalytics.velocity.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.communityanalytics.common.PlayerInfo;
import net.communityanalytics.common.SentryManager;
import net.communityanalytics.velocity.VelocityPlugin;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerInfoListener {
    private final Map<UUID, PlayerInfo> playerInfos = new HashMap<>();

    /**
     * Get the playerInfo
     *
     * @param event PostLoginEvent
     */
    @Subscribe
    public void onLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        Optional<InetSocketAddress> optional = player.getVirtualHost();
        if (optional.isPresent()) {
            InetSocketAddress address = optional.get();
            String playerIp = player.getRemoteAddress().getHostName();
            this.playerInfos.put(player.getUniqueId(), new PlayerInfo(address.getHostName(), playerIp));
        }
    }

    /**
     * Remove the playerInfo
     *
     * @param event DisconnectEvent
     */
    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        this.playerInfos.remove(player.getUniqueId());
    }

    /**
     * Send the playerInfo to the server
     *
     * @param event PluginMessageEvent
     */
    @Subscribe
    public void onMessage(PluginMessageEvent event) {
        if (event.getIdentifier() != VelocityPlugin.instance.getChannel()) {
            return;
        }

        try {
            ByteArrayDataInput input = event.dataAsDataStream();
            UUID uuid = UUID.fromString(input.readUTF());

            if (this.playerInfos.containsKey(uuid)) {
                PlayerInfo playerInfo = this.playerInfos.get(uuid);

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(playerInfo.getIpConnect());
                out.writeUTF(playerInfo.getIpUser());

                if (event.getSource() instanceof ServerConnection) {
                    ServerConnection serverConnection = (ServerConnection) event.getSource();
                    serverConnection.sendPluginMessage(VelocityPlugin.instance.getChannel(), out.toByteArray());
                }
            }
        } catch (Exception e) {
            SentryManager.capture(e);
        }
    }
}
