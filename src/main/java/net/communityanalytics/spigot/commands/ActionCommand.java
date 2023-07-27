package net.communityanalytics.spigot.commands;

import net.communityanalytics.spigot.SpigotPlugin;
import net.communityanalytics.spigot.data.Action;
import net.communityanalytics.spigot.data.Session;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public class ActionCommand extends Command {
    /**
     * Constructor of Command /communityanalytics action <name/uuid> <action>
     *
     * @param name   String
     * @param args   List<String>
     * @param sender CommandSender
     */
    public ActionCommand(String name, List<String> args, CommandSender sender) {
        super(name, args, sender);
    }

    @Override
    protected void execute(SpigotPlugin plugin) {
        if (args.size() <= 1) {
            sender.sendMessage("§7Missing arg usage: §a/community setup §b<name/uuid> <action>");
            return;
        }

        String player_name = args.get(0);
        String action = args.get(1);
        Optional<Session> player_session;

        //Get session by name
        if (!player_name.contains("-")) {
            player_session = SpigotPlugin.manager().find(player_name);
        }

        //Or get session by uuid
        else {
            try {
                player_session = SpigotPlugin.manager().find(java.util.UUID.fromString(player_name));
            } catch (IllegalArgumentException exception) {
                player_session = Optional.empty();
            }
        }

        if (!player_session.isPresent()) {
            sender.sendMessage("§f(§b§lCommunityAnalytics§f) §cPlayer not found!");
            return;
        }
        //Add the action to the player actions list
        player_session.get().addAction(new Action(action));
        sender.sendMessage("§f(§b§lCommunityAnalytics§f) §aAction added!");
    }
}
