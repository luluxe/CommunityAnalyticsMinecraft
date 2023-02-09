package net.communityanalytics.spigot.sessions;

import com.google.gson.JsonObject;
import net.communityanalytics.spigot.SpigotPlugin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Session {

    private final UUID uuid;
    private final String name;
    private String ip_connect;
    private String ip_user;
    private final LocalDateTime join_at;
    private LocalDateTime quit_at = null;

    /**
     * @param uuid       Player's UUID
     * @param name       Player's name
     * @param ip_connect Player's ip used for join the server
     * @param ip_user  Player's ip
     */
    public Session(UUID uuid, String name, String ip_connect, String ip_user) {
        super();
        this.uuid = uuid;
        this.name = name;
        this.ip_connect = ip_connect;
        this.ip_user = ip_user;
        this.join_at = LocalDateTime.now();
    }

    public UUID getUuid() {
        return uuid;
    }

    // Use when proxy send player info

    public void setIp_connect(String ip_connect) {
        this.ip_connect = ip_connect;
    }

    public void setIp_user(String ip_user) {
        this.ip_user = ip_user;
    }

    /**
     * Check if a session is finish, for a session to be finished the end date must not be null
     *
     * @return boolean
     */
    public boolean isFinish() {
        return this.quit_at != null;
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
        session.addProperty("ip_connection", this.ip_connect);
        session.addProperty("ip_user", this.ip_user);
        session.addProperty("join_at", this.join_at.toString());
        session.addProperty("quit_at", this.quit_at.toString());
        return session;
    }

    /**
     * Ends a session
     */
    public void finish() {
        this.quit_at = LocalDateTime.now();
    }

    /**
     * Check if a session is valid
     * For a session to be valid, the number of seconds between the start
     * of the session and the end of the session must be greater than
     * the minimum session time configured in the config.yml file.
     * We will check first if the session is finished.
     *
     * @return boolean
     */
    public boolean isValid() {
        return ChronoUnit.SECONDS.between(this.join_at, this.quit_at) >= SpigotPlugin.config().getMinimumsSessionDuration();
    }

    @Override
    public String toString() {
        return "Session{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", ipConnect='" + ip_connect + '\'' +
                ", ipPlayer='" + ip_user + '\'' +
                ", firstDate=" + join_at +
                ", endDate=" + quit_at +
                '}';
    }
}
