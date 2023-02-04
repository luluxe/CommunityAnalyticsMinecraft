package net.communityanalytics.common.utils;

public class PlateformeConfig {

	private final String plateformId;
	private final String plateformToken;
	private final String serverName;

	/**
	 * @param plateformId
	 * @param plateformToken
	 * @param serverName
	 */
	public PlateformeConfig(String plateformId, String plateformToken, String serverName) {
		super();
		this.plateformId = plateformId;
		this.plateformToken = plateformToken;
		this.serverName = serverName;
	}

	/**
	 * @return the plateformId
	 */
	public String getPlateformId() {
		return plateformId;
	}

	/**
	 * @return the plateformToken
	 */
	public String getPlateformToken() {
		return plateformToken;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	public void toJSONObject(JSONObject data) {
		data.put("platform_id", this.plateformId);
		data.put("platform_token", this.plateformToken);
		data.put("where", this.serverName);
	}

}
