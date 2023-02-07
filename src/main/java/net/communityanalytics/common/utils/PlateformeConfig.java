package net.communityanalytics.common.utils;

public class PlateformeConfig {

    private final int platformId;
    private final String platformToken;
    private final String serverName;
    private final boolean debug;
    private final int minimumSessionDuration;

    public PlateformeConfig(int platformId, String platformToken, String serverName, boolean debug, int minimumSessionDuration) {
        this.platformId = platformId;
        this.platformToken = platformToken;
        this.serverName = serverName;
        this.debug = debug;
        this.minimumSessionDuration = minimumSessionDuration;
    }

    public int getPlatformId() {
        return platformId;
    }

    public String getPlatformToken() {
        return platformToken;
    }

    public String getServerName() {
        return serverName;
    }

    public boolean isDebug() {
        return debug;
    }

    public int getMinimumSessionDuration() {
        return minimumSessionDuration;
    }

    public void toJSONObject(JSONObject data) {
        data.put("platform_id", this.platformId);
        data.put("platform_token", this.platformToken);
        data.put("where", this.serverName);
    }

}
