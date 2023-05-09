package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends Command {

    public ReloadCommand(String name, List<String> args, CommandSender sender) {
        super(name, args, sender);
    }

    @Override
    protected void execute(SpigotPlugin plugin) {
        plugin.reload();
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aConfig reload!");
    }
}
