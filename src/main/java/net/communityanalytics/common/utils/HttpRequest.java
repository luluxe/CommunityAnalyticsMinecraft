package net.communityanalytics.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

public class HttpRequest {

	private final String url;
	private final JSONObject data;
	private String bearer;

	/**
	 * @param url
	 * @param data
	 */
	public HttpRequest(String url, JSONObject data) {
		super();
		this.url = url;
		this.data = data;
	}

	public void setBearer(String bearer) {
		this.bearer = bearer;
	}

	public CompletableFuture<Response> submit() {
		return CompletableFuture.supplyAsync(() -> {			

			Map<String, Object> map = new HashMap<>();
			HttpURLConnection connection = null;
			int responseCode = -1;

			try {

				URL url = new URL(this.url);
				connection = (HttpURLConnection) url.openConnection();

				connection.setRequestMethod("POST");
				connection.addRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);

				if (this.bearer != null) {
					connection.setRequestProperty("Authorization", "Bearer " + this.bearer);
				}

				DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

				responseCode = connection.getResponseCode();

				String jsonInputString = this.data.toString();
				byte[] input = jsonInputString.getBytes("utf-8");
				outputStream.write(input, 0, input.length);
				outputStream.flush();
				outputStream.close();

				InputStream inputStream = connection.getInputStream();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					builder.append(line);
				}
				bufferedReader.close();

				Gson gson = new Gson();
				map = gson.fromJson(builder.toString(), Map.class);

			} catch (Exception e) {
				// e.printStackTrace();
				return new Response(500, new HashMap<>());
			}

			return new Response(responseCode, map);			
		});

	}

}
