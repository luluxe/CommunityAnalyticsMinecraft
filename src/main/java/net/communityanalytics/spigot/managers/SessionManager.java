package net.communityanalytics.spigot.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.spigot.SpigotAPI;
import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.api.APIRequest;
import net.communityanalytics.spigot.api.ApiResponse;
import net.communityanalytics.spigot.data.Session;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SessionManager {
    private final List<Session> sessions = new ArrayList<Session>();
    private ScheduledFuture<?> scheduledFuture = null;

    public SessionManager() {
        SpigotPlugin.logger().printDebug("Session manager started");
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (SpigotPlugin.logger() != null && !SpigotPlugin.logger().isPluginEnable() && this.scheduledFuture != null) {
                this.scheduledFuture.cancel(true);
            } else {
                this.sendAPI();
            }
        }, 1, CommunityAnalytics.SESSION_API_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * Recovers a session based on a uuid, if no session is found then the
     * optional will be empty
     *
     * @param uuid User's UUID
     * @return Optional<Session> Optional that can contain the session
     */
    public Optional<Session> find(UUID uuid) {
        return this.sessions.stream().filter(session -> session.getUuid().equals(uuid)).findFirst();
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

        // We will retrieve the list of sessions that are completed and valid
        Iterator<Session> iterator = this.sessions.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            // Si la session est terminate
            if (session.isFinish()) {
                iterator.remove();

                // If the session is valid
                if (session.isValid()) {
                    sessions.add(session.toJSONObject());
                }
            }
        }

        if (sessions.isEmpty()) {
            // No sessions to send
            SpigotPlugin.logger().printDebug("No session to send to API");
            return;
        }
        data.addProperty("where", SpigotPlugin.config().getServerId());
        data.add("sessions", sessions);

        // Send request
        SpigotPlugin.logger().printDebug("Sending " + sessions.size() + " sessions to API");
        APIRequest request = SpigotAPI.sessionStore(data);

        try {
            ApiResponse response = request.sendRequest();
            if (response.getStatus() == 402) {
                SpigotPlugin.logger().printError("Your subscription no longer allows you to receive new information. Please upgrade your subscription.");
                return;
            }
            if (response.getStatus() != 200) {
                SpigotPlugin.logger().printError("Can't auth to API: Check your token in config.yml");
                return;
            }

            SpigotPlugin.logger().printDebug("Sessions sent to API with success.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
