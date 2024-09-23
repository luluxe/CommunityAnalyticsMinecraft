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

    public String toString() {
        return "PlayerInfo{ip_connect=" + ip_connect + ", ip_user=" + ip_user + "}";
    }
}
