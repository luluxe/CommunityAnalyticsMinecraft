package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class Command {
    protected final String name;
    protected final List<String>  args;
    protected final CommandSender sender;

    /**
     * Constructor
     * @param name String
     * @param args List<String>
     * @param sender CommandSender
     */
    public Command(String name, List<String> args, CommandSender sender) {
        this.name = name;
        this.args = args;
        this.sender = sender;
    }

    /**
     *
     * @param plugin SpigotPlugin
     */
    protected abstract void execute(SpigotPlugin plugin);
}
