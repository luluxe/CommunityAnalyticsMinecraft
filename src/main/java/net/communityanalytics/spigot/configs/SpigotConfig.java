package net.communityanalytics.spigot.configs;

import net.communityanalytics.spigot.SpigotPlugin;

public class SpigotConfig {

    private final String platform_api_token;
    private final String server_tag;
    private final boolean debug;
    private final int minimum_session_duration;

    public SpigotConfig(String platform_api_token, String server_tag, boolean debug, int minimum_session_duration) {
        this.platform_api_token = platform_api_token;
        this.server_tag = server_tag;
        this.debug = debug;
        this.minimum_session_duration = minimum_session_duration;

        SpigotPlugin.logger().printInfo("Loaded configurations:");
        SpigotPlugin.logger().printInfo("=> server_tag: " + this.server_tag);
        SpigotPlugin.logger().printInfo("=> debug: " + this.debug);
        SpigotPlugin.logger().printInfo("=> minimum_session_duration: " + this.minimum_session_duration);
    }

    public String getPlatformApiToken() {
        return platform_api_token;
    }

    public String getServerId() {
        return server_tag;
    }

    public boolean isDebug() {
        return debug;
    }

    public int getMinimumsSessionDuration() {
        return minimum_session_duration;
    }
}
