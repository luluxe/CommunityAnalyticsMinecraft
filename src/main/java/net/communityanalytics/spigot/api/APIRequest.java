package net.communityanalytics.spigot.api;

import com.google.gson.*;
import net.communityanalytics.CommunityAnalytics;
import net.communityanalytics.spigot.SpigotPlugin;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class APIRequest {
    // call
    private final String url;
    private final MethodEnum method;
    private final JsonObject parameters;

    public APIRequest(String api, MethodEnum method, JsonObject parameters) {
        this.url = CommunityAnalytics.API_URL + "/" + api;
        this.method = method;
        this.parameters = parameters;
    }

    /**
     * Get default header for API requests
     *
     * @return Map <String, String>
     */
    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Community-Analytics-Token", SpigotPlugin.config().getPlatformApiToken());
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return headers;
    }

    public ApiResponse sendRequest() throws URISyntaxException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response;
        HttpRequestBase request;

        switch (method.name()) {
            case "GET":
                HttpGet httpGet = new HttpGet(this.url);
                // Request parameters
                if (parameters.size() > 0) {
                    URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());
                    for (Map.Entry<String, JsonElement> entry : parameters.entrySet()) {
                        uriBuilder.addParameter(entry.getKey(), entry.getValue().getAsString());
                    }
                    URI uri = uriBuilder.build();
                    httpGet.setURI(uri);
                }

                request = httpGet;
                break;
            case "DELETE":
                request = new HttpDelete(this.url);
                break;
            case "PUT":
                HttpPut httpput = new HttpPut(this.url);
                // Request parameters and other properties.
                StringEntity entity = new StringEntity(parameters.toString());
                httpput.setEntity(entity);

                request = httpput;
                break;
            default: // POST
                HttpPost httppost = new HttpPost(this.url);
                // Request parameters and other properties.
                entity = new StringEntity(parameters.toString());
                httppost.setEntity(entity);

                request = httppost;
                break;
        }
        for (Map.Entry<String, String> entry : getHeaders().entrySet())
            request.addHeader(entry.getKey(), entry.getValue());

        // Call API
        response = httpclient.execute(request);

        // Build response
        int status_code = response.getStatusLine().getStatusCode();
        String response_string = null;
        JsonElement json_element = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                response_string = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                JsonParser parser = new JsonParser();

                json_element = parser.parse(response_string);
                if (!json_element.isJsonNull()) {
                    JsonObject json_object = (JsonObject) json_element;
                }
            }
        }
        httpclient.close();

        return new ApiResponse(status_code, response_string, json_element);
    }
}