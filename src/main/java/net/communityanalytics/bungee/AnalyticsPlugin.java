package net.communityanalytics.bungee;

import net.communityanalytics.common.Session;
import net.communityanalytics.common.SessionManager;
import net.communityanalytics.common.utils.ILogger;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

public class AnalyticsPlugin extends Plugin implements Listener {

    private boolean isEnable = false;
    private final SessionManager manager = new SessionManager();

    @Override
    public void onEnable() {
        this.isEnable = true;

        ILogger logger = new BungeeLogger(this);
        this.manager.setLogger(logger);

        logger.printInfo("Loading the plugin !");

        getProxy().getPluginManager().registerListener(this, this);
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
        ProxiedPlayer player = event.getPlayer();
        String hostName = player.getPendingConnection().getVirtualHost().getHostString();
        String playerIp = player.getAddress().getHostName();

        Session session = new Session(player.getUniqueId(), player.getName(), hostName, playerIp);
        this.manager.add(session);
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
