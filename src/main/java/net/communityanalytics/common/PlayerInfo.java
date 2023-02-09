package net.communityanalytics.common;

public class PlayerInfo {

    private final String ip_connect;
    private final String ip_user;

    public PlayerInfo(String host, String ip) {
        this.ip_connect = host;
        this.ip_user = ip;
    }

    public String getIpConnect() {
        return ip_connect;
    }

    public String getIpUser() {
        return ip_user;
    }
}
