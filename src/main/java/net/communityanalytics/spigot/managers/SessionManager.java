package net.communityanalytics.spigot.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.spigot.SpigotAPI;
import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.api.APIRequest;
import net.communityanalytics.spigot.api.ApiResponse;
import net.communityanalytics.spigot.data.Session;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class SessionManager {
    private final List<Session> sessions = new ArrayList<>();
    private BukkitTask scheduledFuture = null;

    public SessionManager() {
        SpigotPlugin.logger().printDebug("Session manager started");
        scheduledFuture = Bukkit.getScheduler().runTaskTimerAsynchronously(SpigotPlugin.instance, () -> {
            if (this.scheduledFuture != null && SpigotPlugin.logger() != null && !SpigotPlugin.logger().isPluginEnable()) {
                this.scheduledFuture.cancel();
            } else {
                Bukkit.getScheduler().runTaskAsynchronously(SpigotPlugin.instance, this::sendAPI);
            }
        }, 20 * 30, CommunityAnalytics.SESSION_API_SECONDS * 20);
    }

    /**
     * Recovers a session based on a uuid, if no session is found then the
     * optional will be empty
     *
     * @param uuid User's UUID
     * @return Optional<Session> Optional that can contain the session
     */
    public Optional<Session> find(UUID uuid) {
        return this.sessions
                .stream()
                .filter(session -> session.getUuid().equals(uuid) && !session.isFinish())
                .findFirst();
    }

    /**
     * Add a session to the list
     *
     * @param session New session
     */
    public void add(Session session) {
        this.sessions.add(session);
    }

    public void sendAPI() {
        JsonObject data = new JsonObject();
        JsonArray sessions = new JsonArray();

        SpigotPlugin.logger().printDebug("All tracked sessions : " + this.sessions);

        this.removeInvalidSessions();

        // Session to send
        List<Session> sessionsToSend = this.sessions
                .stream()
                .filter(session -> session.isFinish() && session.isValid())
                .collect(Collectors.toList());
        sessionsToSend.forEach(session -> sessions.add(session.toJSONObject()));

        // No sessions to send
        if (sessions.isEmpty()) {
            SpigotPlugin.logger().printDebug("No session to send to API");
            return;
        }

        SpigotPlugin.logger().printDebug("Sending sessions : " + sessionsToSend);

        data.addProperty("where", SpigotPlugin.config().getServerId());
        data.add("sessions", sessions);

        // Send request
        SpigotPlugin.logger().printDebug("Sending " + sessions.size() + " sessions to API");
        APIRequest request = SpigotAPI.sessionStore(data);

        ApiResponse response = request.sendRequest();

        if (response.getStatus() == 402) {
            SpigotPlugin.logger().printError("Your subscription no longer allows you to receive new information. Please upgrade your subscription.");
            return;
        }

        if (response.getStatus() == 500) {
            SpigotPlugin.logger().printError("Unable to connect to the server. Please check your network connection or try again later.");
            return;
        }

        if (response.getStatus() != 200) {
            SpigotPlugin.logger().printError("Can't auth to API: Check your token in config.yml");
            return;
        }

        this.sessions.removeAll(sessionsToSend);
        SpigotPlugin.logger().printDebug("Sessions sent to API with success.");
    }

    private void removeInvalidSessions() {
        List<Session> sessionsToRemove = this.sessions
                .stream()
                .filter(session -> session.isFinish() && !session.isValid())
                .collect(Collectors.toList());
        for (Session session : sessionsToRemove) {
            SpigotPlugin.logger().printError("The session is invalid: " + session.getIpConnect());
            SpigotPlugin.logger().printError("Contact CommunityAnalytics on Discord, if you can't solve this problem.");
            this.sessions.remove(session);
        }
    }
}
