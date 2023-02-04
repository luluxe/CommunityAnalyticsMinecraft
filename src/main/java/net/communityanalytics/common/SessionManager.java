package net.communityanalytics.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import net.communityanalytics.common.utils.HttpRequest;
import net.communityanalytics.common.utils.JSONObject;
import net.communityanalytics.common.utils.PlateformeConfig;
import net.communityanalytics.common.utils.Response;

public class SessionManager {

	private final String API_URL = "https://api.communityanalytics.fr/";
	private final List<Session> sessions = new ArrayList<Session>();
	private final PlateformeConfig config;

	/**
	 * @param config
	 */
	public SessionManager(PlateformeConfig config) {
		super();
		this.config = config;
	}

	/**
	 * Recovers a session based on a uuid, if no session is found then the
	 * optional will be empty
	 * 
	 * @param uuid
	 *            User's UUID
	 * @return Optional<Session> Optional that can contain the session
	 */
	public Optional<Session> find(UUID uuid) {
		return this.sessions.stream().filter(session -> session.getUuid().equals(uuid)).findFirst();
	}

	/**
	 * Add a session to the list
	 * 
	 * @param session
	 */
	public void add(Session session) {
		this.sessions.add(session);
	}

	public void sendAPI() {

		// LoggerManager.printDebug("Send sessions to API")

		JSONObject data = new JSONObject();
		List<JSONObject> sessions = new ArrayList<>();

		Iterator<Session> iterator = this.sessions.stream().filter(Session::isFinish).iterator();
		while (iterator.hasNext()) {
			Session session = iterator.next();
			iterator.remove();
			sessions.add(session.toJSONObject());

		}
		this.config.toJSONObject(data);
		data.put("sessions", sessions.toArray());

		HttpRequest httpRequest = new HttpRequest(this.API_URL + "sessions", data);

		try {
			Response response = httpRequest.submit().get();
			if (response.getHttpCode() != 200) {
				LoggerManager.printDebug("Error !");
			}
		} catch (InterruptedException | ExecutionException e) {
			LoggerManager.printDebug("Error !");
			e.printStackTrace();
		}

	}

}
