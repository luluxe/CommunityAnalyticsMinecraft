package net.communityanalytics.spigot.api;

import com.google.gson.JsonElement;

import java.io.IOException;

public class ApiResponse {
    // response
    public int status_code;
    public String response_string = null;
    public JsonElement json_object = null;

    public ApiResponse(int status_code, String response_string, JsonElement json_object) {
        this.status_code = status_code;
        this.response_string = response_string;
        this.json_object = json_object;
    }

    public boolean has(String arg) throws IOException {
        return json_object.getAsJsonObject().has(arg);
    }

    public String getStringArg(String arg) throws IOException {
        return json_object.getAsJsonObject().get(arg).getAsString();
    }

    public int getStatus() {
        return status_code;
    }
}