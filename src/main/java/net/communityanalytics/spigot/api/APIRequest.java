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
import org.jetbrains.annotations.NotNull;

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
        HttpRequestBase request = getHttpRequestBase();

        for (Map.Entry<String, String> entry : getHeaders().entrySet())
            request.addHeader(entry.getKey(), entry.getValue());

        return callApi(request);
    }

    private @NotNull HttpRequestBase getHttpRequestBase() throws URISyntaxException, UnsupportedEncodingException {
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

                return httpGet;
            case "DELETE":
                return new HttpDelete(this.url);
            case "PUT":
                HttpPut httpput = new HttpPut(this.url);
                // Request parameters and other properties.
                StringEntity entity = new StringEntity(parameters.toString());
                httpput.setEntity(entity);

                return httpput;
            default: // POST
                HttpPost httppost = new HttpPost(this.url);
                // Request parameters and other properties.
                entity = new StringEntity(parameters.toString());
                httppost.setEntity(entity);

                return httppost;
        }
    }

    private ApiResponse callApi(HttpRequestBase request) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Call API
        HttpResponse response = httpclient.execute(request);

        // Build response
        ApiResponse apiResponse = buildApiResponse(response);

        // Close client
        httpclient.close();

        return apiResponse;
    }


    private ApiResponse buildApiResponse(HttpResponse response) throws IOException {
        int status_code = response.getStatusLine().getStatusCode();
        String response_string = null;
        JsonElement json_element = null;

        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                response_string = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                json_element = JsonParser.parseString(response_string);
            }
        }

        return new ApiResponse(status_code, response_string, json_element);
    }
}