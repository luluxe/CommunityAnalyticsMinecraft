package net.communityanalytics.common;

import net.communityanalytics.common.utils.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final String API_URL = "https://communityanalytics.net/api/v1/";
    private final List<Session> sessions = new ArrayList<Session>();
    private PlateformeConfig config;
    private ILogger logger;

    public PlateformeConfig getConfig() {
        return config;
    }

    public SessionManager() {
        this.executor.scheduleAtFixedRate(this::sendAPI, 1, 1, TimeUnit.MINUTES);
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

        // json object that will be sent to the api
        JSONObject data = new JSONObject();

        // List of sessions transformed into JSONObject
        List<JSONObject> sessions = new ArrayList<>();

        // We will retrieve the list of sessions that are completed and valid
        Iterator<Session> iterator = this.sessions.iterator();

        System.out.println("---");
        System.out.println(this.sessions.size());
        System.out.println(iterator);

        while (iterator.hasNext()) {
            Session session = iterator.next();

            System.out.println(session);

            // Si la session est terminée
            if (session.isFinish()) {

                iterator.remove();

                // If the session is valid
                if (session.isValid(this.config.getMinimumSessionDuration())) {
                    sessions.add(session.toJSONObject());
                }
            }

            System.out.println(sessions.size());
        }


        System.out.println(">>");
        System.out.println("> " + sessions);
        System.out.println(">>>");

        if (sessions.isEmpty()) {
            this.logger.printDebug("No session to sent");
            return;
        }

        this.config.toJSONObject(data);
        data.put("sessions", sessions.toArray());

        System.out.println(data);

        HttpRequest httpRequest = new HttpRequest(this.API_URL + "sessions", data, this.config.isDebug());

        System.out.println(httpRequest);

        try {
            Response response = httpRequest.submit().get();

            System.out.println(response);

            if (response.getHttpCode() != 200) {
                this.logger.printDebug("Error !");
            } else {
                this.logger.printDebug("Success !");
            }
        } catch (InterruptedException | ExecutionException e) {
            this.logger.printDebug("Error !");
            e.printStackTrace();
        }
    }

}
