package net.communityanalytics.spigot.api;

import com.google.gson.JsonParser;

import java.net.http.HttpResponse;

public class ApiResponse {
    private final HttpResponse<String> response;

    public ApiResponse(java.net.http.HttpResponse<String> response) {
        this.response = response;
    }

    public String getStringArg(String arg) {
        return JsonParser.parseString(response.body()).getAsJsonObject().get(arg).getAsString();
    }

    public int getStatus() {
        return response.statusCode();
    }
}
