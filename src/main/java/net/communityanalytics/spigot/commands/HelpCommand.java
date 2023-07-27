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
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §7List of commands:");
        sender.sendMessage("§e» §a/communityanalytics setup §b<key> [server_id]");
        sender.sendMessage("§e» §a/communityanalytics action §b<name/uuid> <action>");
        sender.sendMessage("§e» §a/communityanalytics reload");
        sender.sendMessage("§e» §a/communityanalytics help");
    }
}
