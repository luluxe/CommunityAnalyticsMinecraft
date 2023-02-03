package net.communityanalytics.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import net.communityanalytics.common.utils.JSONObject;

public class Session {

	private final UUID uuid;
	private final String name;
	private final String ip_connect;
	private final String ip_player;
	private final LocalDateTime first_date;
	private LocalDateTime end_date;

	/**
	 * @param uuid
	 * @param name
	 * @param ip_connect
	 * @param ip_player
	 * @param first_date
	 * @param end_date
	 */
	public Session(UUID uuid, String name, String ip_connect, String ip_player, LocalDateTime first_date,
			LocalDateTime end_date) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.ip_connect = ip_connect;
		this.ip_player = ip_player;
		this.first_date = first_date;
		this.end_date = end_date;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the ip_connect
	 */
	public String getIp_connect() {
		return ip_connect;
	}

	/**
	 * @return the ip_player
	 */
	public String getIp_player() {
		return ip_player;
	}

	/**
	 * @return the first_date
	 */
	public LocalDateTime getFirst_date() {
		return first_date;
	}

	/**
	 * @return the end_date
	 */
	public LocalDateTime getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date
	 *            the end_date to set
	 */
	public void setEnd_date(LocalDateTime end_date) {
		this.end_date = end_date;
	}

	/**
	 * Check if a session is completed
	 * 
	 * @return boolean
	 */
	public boolean isFinish() {
		return this.end_date != null;
	}

	public JSONObject toJSONObject() {
		JSONObject session = new JSONObject();
		session.put("uuid", this.uuid);
		session.put("name", this.name);
		session.put("ip_connect", this.ip_connect);
		session.put("ip_player", this.ip_player);
		session.put("first_date", this.first_date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		session.put("end_date", this.end_date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		return session;
	}

}
