package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.util.FileUtil;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;

public class SetupCommand extends Command {

    public SetupCommand(String name, List<String> args, CommandSender sender) {
        super(name, args, sender);
    }

    @Override
    protected void execute(SpigotPlugin plugin) {
        // /setup <key> <server_id>
        if (args.size() == 0) {
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cYou need to specify a key when entering the command !");
            return;
        }

        // Parsing
        String server_id = "";
        if (args.size() == 2)
            server_id = args.get(1);
        String path_name = "plugins/CommunityAnalytics/config.yml";

        try {
            FileUtil.replace(path_name, "platform-api-token:", "platform-api-token: '" + args.get(0)+ "'");
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aToken has been set!");

            if (args.size() == 2)
                FileUtil.replace(path_name, "server-id:", "server-id: '" + server_id + "'");
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aServer id has been set!");

            plugin.reload();
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aConfig reload!");
        } catch(IOException exception) {
            exception.printStackTrace();
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cError: " + exception.getMessage());
        }
    }
}
