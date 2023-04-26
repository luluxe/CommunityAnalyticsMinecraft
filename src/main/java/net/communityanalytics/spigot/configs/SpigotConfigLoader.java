package net.communityanalytics.spigot.configs;

import net.communityanalytics.common.interfaces.ConfigLoader;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotConfigLoader implements ConfigLoader {

    private final YamlConfiguration yamlConfiguration;

    public SpigotConfigLoader(YamlConfiguration yamlConfiguration) {
        this.yamlConfiguration = yamlConfiguration;
    }

    @Override
    public SpigotConfig loadConfig() {
        String platformApiToken = yamlConfiguration.getString("platform-api-token");
        boolean debug = yamlConfiguration.getBoolean("debug");
        int minimumSessionDuration = yamlConfiguration.getInt("minimum-session-duration");
        String serverName = yamlConfiguration.getString("server-name");
        if(serverName == null) {
            serverName = yamlConfiguration.getString("server-id");
        }

        return new SpigotConfig(platformApiToken, serverName, debug, minimumSessionDuration);
    }
}
