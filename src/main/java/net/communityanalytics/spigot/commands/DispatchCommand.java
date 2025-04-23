package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DispatchCommand implements CommandExecutor {
    private final SpigotPlugin plugin;

    public DispatchCommand(SpigotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args) {
        Command commandToExecute = args.length == 0
                ? new MainCommand("communityanalytics", getArgs(args,0), sender)
                : getSubCommand(args[0], args, sender);

        this.executeCommand(commandToExecute, sender);

        return true;
    }

    private void executeCommand(Command command, @NotNull CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (command == null) {
                sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cUnknown command ! type /communityanalytics help for more informations");
                return;
            }

            try {
                command.execute(plugin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Command getSubCommand(String commandName, String[] args, @NotNull CommandSender sender) {
        switch (commandName) {
            case "reload":
                return new ReloadCommand("reload", getArgs(args,1), sender);
            case "setup":
                return new SetupCommand("setup", getArgs(args,1), sender);
            case "help":
                return new HelpCommand("help", getArgs(args,1), sender);
            default:
                return null;
        }
    }

    public List<String> getArgs(String [] args , int start) {
        List<String> list = new ArrayList<>(Arrays.asList(args));
        list.subList(0, start).clear();
        return list;
    }
}
