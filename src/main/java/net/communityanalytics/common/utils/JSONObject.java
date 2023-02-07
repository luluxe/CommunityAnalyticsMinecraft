package net.communityanalytics.common.utils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class JSONObject {

	private final HashMap<String, Object> map = new HashMap<>();

	public void put(String key, Object value) {
		if (value != null) {
			map.put(key, value);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Set<Map.Entry<String, Object>> entrySet = map.entrySet();
		builder.append("{");

		int i = 0;
		for (Map.Entry<String, Object> entry : entrySet) {
			Object val = entry.getValue();
			builder.append(quote(entry.getKey())).append(":");

			System.out.println("CURRENT : " + val + " -> " + val.getClass());

			if (val instanceof String) {
				builder.append(quote(String.valueOf(val)));
			} else if (val instanceof UUID) {
				builder.append(quote(val.toString()));
			} else if (val instanceof Integer) {
				builder.append(Integer.valueOf(String.valueOf(val)));
			} else if (val instanceof Long) {
				builder.append(Long.valueOf(String.valueOf(val)));
			} else if (val instanceof Boolean) {
				builder.append(val);
			} else if (val instanceof JSONObject) {
				builder.append(val.toString());
			} else if (val.getClass().isArray()) {
				builder.append("[");
				int len = Array.getLength(val);
				for (int j = 0; j < len; j++) {
					builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
				}
				builder.append("]");
			}

			builder.append(++i == entrySet.size() ? "}" : ",");
		}

		return builder.toString();
	}

	private String quote(String string) {
		return "\"" + string + "\"";
	}
}