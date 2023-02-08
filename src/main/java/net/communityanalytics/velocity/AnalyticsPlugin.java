package net.communityanalytics.velocity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import net.communityanalytics.common.SessionManager;
import net.communityanalytics.common.utils.PlayerInfo;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Plugin(id = "communityanalytics", name = "CommunityAnalytics", version = "1.0.0")
public class AnalyticsPlugin {

    private final ChannelIdentifier channel = new LegacyChannelIdentifier(SessionManager.channelName);
    private final Map<UUID, PlayerInfo> playerInfos = new HashMap<>();
    private final ProxyServer server;

    @Inject
    public AnalyticsPlugin(ProxyServer server, Logger logger) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getChannelRegistrar().register(channel);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {

    }

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

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        this.playerInfos.remove(player.getUniqueId());
    }

    @Subscribe
    public void onMessage(PluginMessageEvent event) {

        if (event.getIdentifier() != channel) {
            return;
        }

        try {
            ByteArrayDataInput input = event.dataAsDataStream();
            UUID uuid = UUID.fromString(input.readUTF());

            if (this.playerInfos.containsKey(uuid)) {
                PlayerInfo playerInfo = this.playerInfos.get(uuid);

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(playerInfo.getHost());
                out.writeUTF(playerInfo.getIp());

                if (event.getSource() instanceof ServerConnection serverConnection) {
                    serverConnection.sendPluginMessage(channel, out.toByteArray());
                }
            }
        } catch (Exception ignored) {
        }
    }
}
