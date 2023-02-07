package net.communityanalytics.spigot;

import net.communityanalytics.common.utils.ConfigLoader;
import net.communityanalytics.common.utils.PlateformeConfig;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotConfigLoader implements ConfigLoader {

    private final YamlConfiguration yamlConfiguration;

    public SpigotConfigLoader(YamlConfiguration yamlConfiguration) {
        this.yamlConfiguration = yamlConfiguration;
    }

    @Override
    public PlateformeConfig loadConfig() {

        int platformId = yamlConfiguration.getInt("platform-id");
        String platformToken = yamlConfiguration.getString("platform-token");
        boolean debug = yamlConfiguration.getBoolean("debug");
        int minimumSessionDuration = yamlConfiguration.getInt("minimum-session-duration");
        String serverName = yamlConfiguration.getString("server-name");

        return new PlateformeConfig(platformId, platformToken, serverName, debug, minimumSessionDuration);
    }

}
