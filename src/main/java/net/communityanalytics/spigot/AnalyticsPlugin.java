package net.communityanalytics.spigot;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class AnalyticsPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("Chargement du plugin !");
	}

	@Override
	public void onDisable() {
		getLogger().info("Je me décharge !");
	}

}
