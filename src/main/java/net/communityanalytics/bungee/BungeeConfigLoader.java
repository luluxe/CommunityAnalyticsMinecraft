package net.communityanalytics.bungee;

import net.communityanalytics.common.utils.ConfigLoader;
import net.communityanalytics.common.utils.PlateformeConfig;
import net.md_5.bungee.config.Configuration;

public class BungeeConfigLoader implements ConfigLoader {

    private final Configuration configuration;

    public BungeeConfigLoader(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public PlateformeConfig loadConfig() {
        
        int platformId = configuration.getInt("platform-id");
        String platformToken = configuration.getString("platform-token");
        boolean debug = configuration.getBoolean("debug");
        int minimumSessionDuration = configuration.getInt("minimum-session-duration");
        String serverName = configuration.getString("server-name");

        return new PlateformeConfig(platformId, platformToken, serverName, debug, minimumSessionDuration);
    }
}
