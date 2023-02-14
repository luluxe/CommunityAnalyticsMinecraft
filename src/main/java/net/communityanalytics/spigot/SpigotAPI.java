package net.communityanalytics.spigot;

import com.google.gson.JsonObject;
import net.communityanalytics.spigot.api.APIRequest;
import net.communityanalytics.spigot.api.MethodEnum;

public class SpigotAPI {
    public static APIRequest sessionStore(JsonObject data) {
        return new APIRequest("v1/sessions", MethodEnum.POST, data);
    }

    public static APIRequest platformShow() {
        JsonObject data = new JsonObject();
        data.addProperty("version", SpigotPlugin.instance.getDescription().getVersion());
        return new APIRequest("v1/platform", MethodEnum.POST, data);
    }
}
