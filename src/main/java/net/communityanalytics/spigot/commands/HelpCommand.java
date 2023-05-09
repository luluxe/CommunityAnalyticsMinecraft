package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand(String name, List<String> args, @NotNull CommandSender sender) {
        super(name, args, sender);

    }

    @Override
    protected void execute(SpigotPlugin plugin) {
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aCommands:");
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §a- /communityanalytics setup <key> <server_id>");
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §a- /communityanalytics reload");
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §a- /communityanalytics help");
    }
}
