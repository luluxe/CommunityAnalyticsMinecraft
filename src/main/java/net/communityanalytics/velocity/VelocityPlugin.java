package net.communityanalytics.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.velocity.listeners.PlayerInfoListener;
import org.slf4j.Logger;

@Plugin(id = "communityanalytics", name = "CommunityAnalytics", version = "1.0.0")
public class VelocityPlugin {
    private final ChannelIdentifier channel = new LegacyChannelIdentifier(CommunityAnalytics.CHANNEL_INFO);
    private ProxyServer server = null;
    private Logger logger = null;

    public static VelocityPlugin instance;
    public VelocityPlugin() {
        instance = this;
    }

    public ChannelIdentifier getChannel() {
        return channel;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    // Events

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger) {
        // Register velocity things
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // listeners
        server.getEventManager().register(this, new PlayerInfoListener());

        // channels
        server.getChannelRegistrar().register(channel);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        // nothing to do
    }
}
