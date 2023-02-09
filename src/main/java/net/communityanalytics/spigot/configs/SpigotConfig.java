package net.communityanalytics.spigot.configs;

public class SpigotConfig {

    private final String platform_api_token;
    private final String server_name;
    private final boolean debug;
    private final int minimums_session_duration;

    public SpigotConfig(String platformApiToken, String serverName, boolean debug, int minimumSessionDuration) {
        this.platform_api_token = platformApiToken;
        this.server_name = serverName;
        this.debug = debug;
        this.minimums_session_duration = minimumSessionDuration;
    }

    public String getPlatformApiToken() {
        return platform_api_token;
    }

    public String getServerName() {
        return server_name;
    }

    public boolean isDebug() {
        return debug;
    }

    public int getMinimumsSessionDuration() {
        return minimums_session_duration;
    }
}
