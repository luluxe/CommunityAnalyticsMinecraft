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
        String platform_api_token = yamlConfiguration.getString("platform-api-token");
        boolean debug = yamlConfiguration.getBoolean("debug");
        int minimum_session_duration = yamlConfiguration.getInt("minimum-session-duration");
        String server_id = yamlConfiguration.getString("server-name");
        if(server_id == null) {
            server_id = yamlConfiguration.getString("server-id");
        }

        return new SpigotConfig(platform_api_token, server_id, debug, minimum_session_duration);
    }
}
