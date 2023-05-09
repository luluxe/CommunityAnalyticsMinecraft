package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DispatchCommand implements CommandExecutor {

    private final SpigotPlugin plugin;

    public DispatchCommand(SpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        MainCommand main_command = new MainCommand("communityanalytics", args, sender);

        if (args.length == 0) {
            main_command.execute(plugin);
            return true;
        }

        String first_arg = args[0];
        // TODO remove argument pas utile
        switch (first_arg) {
            case "reload":
                new ReloadCommand("reload", args, sender).execute(plugin);
                break;
            case "setup":
                new SetupCommand("setup", args, sender).execute(plugin);
                main_command.execute(plugin);
                break;
            case "help":
                new HelpCommand("help", args, sender).execute(plugin);
                break;
            default:
                sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cUnknown command ! type /communityanalytics help for more informations");
                break;
        }
        return true;
    }
}
