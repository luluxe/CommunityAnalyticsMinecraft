package net.communityanalytics.spigot.api;

import com.google.gson.JsonObject;
import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.spigot.SpigotPlugin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpigotHttpRequest {
    private String api;
    private MethodEnum method;
    private JsonObject data;

    public SpigotHttpRequest(String api, MethodEnum method, JsonObject data) {
        this.api = api;
        this.method = method;
        this.data = data;
    }

    public void sendRequest() throws IOException, InterruptedException, Exception {
        // Send request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CommunityAnalytics.API_URL + api))
                .header("X-Community-Analytics-Token", SpigotPlugin.config().getPlatformApiToken())
                .header("Content-Type", "application/json")
                .method(method.name(), HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();

        // Send http request
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Send debug
        if (response == null) {
            SpigotPlugin.logger().printDebug("Session request error no response");
        } else {
            SpigotPlugin.logger().printDebug("Session request : status code " + response.statusCode() + " | body " + response.body());
        }
    }
}
