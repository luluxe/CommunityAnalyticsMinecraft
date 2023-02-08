package net.communityanalytics.common;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Session {

    private final UUID uuid;
    private final String name;
    private String ipConnect;
    private String ipPlayer;
    private final LocalDateTime firstDate;
    private LocalDateTime endDate;

    /**
     * @param uuid       Player's UUID
     * @param name       Player's name
     * @param ip_connect Player's ip used for join the server
     * @param ip_player  Player's ip
     */
    public Session(UUID uuid, String name, String ip_connect, String ip_player) {
        super();
        this.uuid = uuid;
        this.name = name;
        this.ipConnect = ip_connect;
        this.ipPlayer = ip_player;
        this.firstDate = LocalDateTime.now();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getIpConnect() {
        return ipConnect;
    }

    public String getIpPlayer() {
        return ipPlayer;
    }

    public LocalDateTime getFirstDate() {
        return firstDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setIpConnect(String ipConnect) {
        this.ipConnect = ipConnect;
    }

    public void setIpPlayer(String ipPlayer) {
        this.ipPlayer = ipPlayer;
    }

    /**
     * Check if a session is finish, for a session to be finished the end date must not be null
     *
     * @return boolean
     */
    public boolean isFinish() {
        return this.endDate != null;
    }

    /**
     * Allows to transform a session into a JSONObject to be sent to the API
     *
     * @return JSONObject
     */
    public JsonObject toJSONObject() {
        JsonObject session = new JsonObject();
        session.addProperty("identifier", this.uuid.toString());
        session.addProperty("name", this.name);
        session.addProperty("ip_connection", this.ipConnect);
        session.addProperty("ip_user", this.ipPlayer);
        session.addProperty("join_at", this.firstDate.toString());
        session.addProperty("quit_at", this.endDate.toString());
        return session;
    }

    /**
     * Ends a session
     */
    public void finish() {
        this.endDate = LocalDateTime.now();
    }

    /**
     * Check if a session is valid
     * For a session to be valid, the number of seconds between the start
     * of the session and the end of the session must be greater than
     * the minimum session time configured in the config.yml file.
     * We will check first if the session is finished.
     *
     * @param minimumSessionDuration The minimum time for a session to be valid
     * @return boolean
     */
    public boolean isValid(int minimumSessionDuration) {
        return ChronoUnit.SECONDS.between(this.firstDate, this.endDate) >= minimumSessionDuration;
    }

    @Override
    public String toString() {
        return "Session{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", ipConnect='" + ipConnect + '\'' +
                ", ipPlayer='" + ipPlayer + '\'' +
                ", firstDate=" + firstDate +
                ", endDate=" + endDate +
                '}';
    }
}
