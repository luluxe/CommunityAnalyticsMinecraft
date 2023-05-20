package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.util.FileUtil;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetupCommand extends Command {

    public SetupCommand(String name, List<String> args, CommandSender sender) {
        super(name, args, sender);
    }

    @Override
    protected void execute(SpigotPlugin plugin) {
        if (args.size() == 0) {
            sender.sendMessage("§7Missing arg usage: §a/community setup §b<key> [server_id]");
            return;
        }

        // Parsing
        String server_id = null;
        if (args.size() == 2)
            server_id = args.get(1);

        String path_name = "plugins/CommunityAnalytics/config.yml";
        try {
            FileUtil.replace(path_name, "platform-api-token:", "platform-api-token: '" + args.get(0)+ "'");
            if (server_id != null)
                FileUtil.replace(path_name, "server-id:", "server-id: '" + server_id + "'");
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aConfig.yml was updated!");

            plugin.reload();
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aConfig reload!");

            new MainCommand("communityanalytics", new ArrayList<>(), sender).execute(SpigotPlugin.instance);
        } catch(IOException exception) {
            exception.printStackTrace();
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cError was occurred: " + exception.getMessage());
        }
    }
}
