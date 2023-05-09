package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.commands.util.Util;
import org.bukkit.command.CommandSender;

public class SetupCommand extends Command {

    public SetupCommand(String name, String[] args, CommandSender sender) {
        super(name, args, sender);
    }

    @Override
    protected void execute(SpigotPlugin plugin) {
        if (args.length == 1) {
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cYou need to specify a key when entering the command !");
            return;
        }

        String server_id = "";
        if (args.length == 3)
            server_id = args[2];
        String path_name = "plugins/CommunityAnalytics/config.yml";

        Util.FileReplacer(path_name, "platform-api-token:", "platform-api-token: '" + args[1] + "'");
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aToken has been set!");

        if (args.length == 3)
            Util.FileReplacer(path_name, "server-id:", "server-id: '" + server_id + "'");
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aServer id has been set!");

        plugin.reload();
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aConfig reload!");
    }
}
