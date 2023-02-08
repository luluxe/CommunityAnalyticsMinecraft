package net.communityanalytics.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.communityanalytics.common.utils.ILogger;
import net.communityanalytics.common.utils.PlateformeConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private ScheduledFuture<?> scheduledFuture = null;
    private final String API_URL = "https://communityanalytics.net/api/v1/";
    private final List<Session> sessions = new ArrayList<Session>();
    private PlateformeConfig config;
    private ILogger logger;

    public PlateformeConfig getConfig() {
        return config;
    }

    public SessionManager() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (this.logger != null && this.logger.isPluginEnable() && this.scheduledFuture != null) {
                this.scheduledFuture.cancel(true);
            } else {
                this.sendAPI();
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    public void setConfig(PlateformeConfig config) {
        this.config = config;
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    public ILogger getLogger() {
        return logger;
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

        this.logger.printDebug("Send sessions to API");

        JsonObject data = new JsonObject();
        JsonArray sessions = new JsonArray();

        // We will retrieve the list of sessions that are completed and valid
        Iterator<Session> iterator = this.sessions.iterator();

        while (iterator.hasNext()) {
            Session session = iterator.next();

            // Si la session est terminée
            if (session.isFinish()) {

                iterator.remove();

                // If the session is valid
                if (session.isValid(this.config.getMinimumSessionDuration())) {
                    sessions.add(session.toJSONObject());
                }
            }
        }

        if (sessions.isEmpty()) {
            this.logger.printDebug("No session to sent");
            return;
        }

        this.config.toJSONObject(data);
        data.add("sessions", sessions);

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create("https://communityanalytics.net/api/v1/sessions"))
                .header("Content-Type", "application/json")
                .method("POST", java.net.http.HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
