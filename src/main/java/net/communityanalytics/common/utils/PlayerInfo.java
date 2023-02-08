package net.communityanalytics.common.utils;

public class PlayerInfo {

    private final String host;
    private final String ip;

    public PlayerInfo(String host, String ip) {
        this.host = host;
        this.ip = ip;
    }

    public String getHost() {
        return host;
    }

    public String getIp() {
        return ip;
    }
}
