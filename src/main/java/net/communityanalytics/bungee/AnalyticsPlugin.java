package net.communityanalytics.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.communityanalytics.common.SessionManager;
import net.communityanalytics.common.utils.PlayerInfo;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AnalyticsPlugin extends Plugin implements Listener {

    private final Map<UUID, PlayerInfo> playerInfos = new HashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void onEnable() {
        ProxyServer server = getProxy();
        server.getPluginManager().registerListener(this, this);
        server.registerChannel(SessionManager.channelName);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        scheduledExecutorService.execute(() -> {
            ProxiedPlayer player = event.getPlayer();
            String hostName = player.getPendingConnection().getVirtualHost().getHostString();
            String playerIp = player.getAddress().getHostString();
            playerInfos.put(player.getUniqueId(), new PlayerInfo(hostName, playerIp));
        });
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        this.playerInfos.remove(player.getUniqueId());
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(SessionManager.channelName)) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        UUID uuid = UUID.fromString(input.readUTF());

        if (this.playerInfos.containsKey(uuid)) {
            PlayerInfo playerInfo = this.playerInfos.get(uuid);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(playerInfo.getHost());
            out.writeUTF(playerInfo.getIp());

            if (event.getReceiver() instanceof ProxiedPlayer) {
                ServerInfo serverInfo = ((ProxiedPlayer) event.getReceiver()).getServer().getInfo();
                serverInfo.sendData(SessionManager.channelName, out.toByteArray());
            }
        }
    }
}
