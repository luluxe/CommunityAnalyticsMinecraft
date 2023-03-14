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
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

@Plugin(id = "communityanalytics", name = "CommunityAnalytics", version = "1.0.0")
public class VelocityPlugin {
    public static VelocityPlugin instance;
    private final ChannelIdentifier channel = new LegacyChannelIdentifier(CommunityAnalytics.CHANNEL_INFO);
    private ProxyServer server = null;
    private Logger logger = null;
    private final Metrics.Factory metricsFactory;

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger, Metrics.Factory metricsFactory) {
        instance = this;
        this.server = server;
        this.logger = logger;
        this.metricsFactory = metricsFactory;
    }

    public ChannelIdentifier getChannel() {
        return channel;
    }

    public ProxyServer getServer() {
        return server;
    }

    // Events

    public Logger getLogger() {
        return logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // listeners
        server.getEventManager().register(this, new PlayerInfoListener());

        // channels
        server.getChannelRegistrar().register(channel);

        int pluginId = 17953; // <-- Replace with the id of your plugin!
        Metrics metrics = metricsFactory.make(this, pluginId);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        // nothing to do
    }
}
