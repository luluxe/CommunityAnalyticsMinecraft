package net.communityanalytics.spigot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CommandReload implements CommandExecutor {

    private final AnalyticsPlugin plugin;

    public CommandReload(AnalyticsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        this.plugin.reloadConfig();
        this.plugin.loadConfiguration();

        sender.sendMessage("§aConfig reload !");

        System.out.println("J'envoie la sauce");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://communityanalytics.net/api/v1/sessions"))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"platform_token\":\"xQhfYy8IhykTUU5VMIuWJEWyNhNLawIw5UNHzBHzXLmMctIZ18lLiMC4bQ7xUcGi\",\"sessions\":[{\"identifier\":\"1027fce0-0e2a-3bda-bde7-e699bd40bdc3\",\"join_at\":\"2023-02-08T10:13:45.256056511\",\"quit_at\":\"2023-02-08T10:14:24.573739667\",\"name\":\"Maxlego08\",\"ip_user\":\"37.174.61.147\",\"ip_connection\":\"join.onoria.fr.:26345\"}],\"platform_id\":1,\"where\":\"skyblock\"}"))
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        assert response != null;

        System.out.println(response.statusCode());
        System.out.println(response.body());

        return true;
    }
}
