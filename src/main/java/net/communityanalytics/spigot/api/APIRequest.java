package net.communityanalytics.spigot.api;

import com.google.gson.JsonObject;
import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.spigot.SpigotPlugin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class APIRequest {
    private final String api;
    private final MethodEnum method;
    private final JsonObject data;

    public APIRequest(String api, MethodEnum method, JsonObject data) {
        this.api = api;
        this.method = method;
        this.data = data;
    }

    public ApiResponse sendRequest() throws IOException, InterruptedException {
        // Send request
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(CommunityAnalytics.API_URL + "/" + api))
                .header("X-Community-Analytics-Token", SpigotPlugin.config().getPlatformApiToken())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method(method.name(), java.net.http.HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();

        // Return response
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response);
    }
}
