package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import org.bukkit.command.CommandSender;

public abstract class Command {
    protected final String name;
    protected final String [] args;
    protected final CommandSender sender;

    /**
     * Constructor
     * @param name String
     * @param args String[]
     * @param sender CommandSender
     */
    public Command(String name, String [] args, CommandSender sender) {
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
