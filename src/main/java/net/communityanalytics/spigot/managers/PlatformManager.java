package net.communityanalytics.spigot.managers;

import net.communityanalytics.spigot.SpigotAPI;
import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.api.ApiResponse;

public class PlatformManager {
    public boolean success = false;
    public String version = "1.0.5";

    public void getPlatformInfo() {
        try {
            ApiResponse response = SpigotAPI.platformShow().sendRequest();
            if (response.getStatus() != 200) {
                SpigotPlugin.logger().printError("Can't auth to API:");
                SpigotPlugin.logger().printError("Check your token in config.yml");
                error();
                return;
            }

            success = true;
            version = response.getStringArg("plugin_version");

            SpigotPlugin.logger().printInfo("Connected to CommunityAnalytics API");
            if (isOutdated()) {
                SpigotPlugin.logger().printError("Your plugin is outdated! (Last version " + version + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            error();
        }
    }

    public void error() {
        success = false;
        version = "";
    }

    // Get

    public boolean isSuccess() {
        return success;
    }

    public String getVersion() {
        return version;
    }

    public boolean isOutdated() {
        return !version.equals(SpigotPlugin.instance.getDescription().getVersion());
    }
}
