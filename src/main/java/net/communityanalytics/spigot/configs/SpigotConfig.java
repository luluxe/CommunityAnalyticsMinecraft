package net.communityanalytics.spigot.configs;

import net.communityanalytics.spigot.SpigotPlugin;

public class SpigotConfig {

    private final String platform_api_token;
    private final String server_id;
    private final boolean debug;
    private final int minimum_session_duration;

    public SpigotConfig(String platform_api_token, String server_id, boolean debug, int minimum_session_duration) {
        this.platform_api_token = platform_api_token;
        this.server_id = server_id;
        this.debug = debug;
        this.minimum_session_duration = minimum_session_duration;

        SpigotPlugin.logger().printInfo("Loaded configurations:");
        SpigotPlugin.logger().printInfo("=> server_id: " + this.server_id);
        SpigotPlugin.logger().printInfo("=> debug: " + this.debug);
        SpigotPlugin.logger().printInfo("=> minimum_session_duration: " + this.minimum_session_duration);
    }

    public String getPlatformApiToken() {
        return platform_api_token;
    }

    public String getServerId() {
        return server_id;
    }

    public boolean isDebug() {
        return debug;
    }

    public int getMinimumsSessionDuration() {
        return minimum_session_duration;
    }
}
