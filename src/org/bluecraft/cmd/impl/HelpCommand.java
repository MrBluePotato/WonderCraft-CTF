package org.bluecraft.cmd.impl;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;


/**
 *
 * @author Jacob Morgan
 */
public class HelpCommand implements Command{

    private static final HelpCommand INSTANCE = new HelpCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static HelpCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        player.getActionSender().sendChatMessage("Sorry... this comand is still under construction.");
               /* player.getActionSender().sendChatMessage("&aHelp:");
                player.getActionSender().sendChatMessage("&eTo see a list of all commands, type &a/Commands");
                player.getActionSender().sendChatMessage("&eTo see detailed help for a command, type &a/Help CommandName");
                player.getActionSender().sendChatMessage("&eTo see your stats, go to &1www.is.gd/wondercraftstats");
                player.getActionSender().sendChatMessage("&eTo see available maps, type &a/Maps");
                player.getActionSender().sendChatMessage("&eTo join a team, type either &1/Blue&e or &c/Red");
                player.getActionSender().sendChatMessage("&eTo send a private message, type &a@PlayerName Message"); 
                
                
        if (params.getArgumentCount() == 0) {
            if (message.startsWith("help")) {
                player.getActionSender().sendChatMessage("&aHelp:");
                player.getActionSender().sendChatMessage("&eTo see a list of all commands, type &a/Commands");
                player.getActionSender().sendChatMessage("&eTo see detailed help for a command, type &a/Help CommandName");
                player.getActionSender().sendChatMessage("&eTo see your stats, go to &1www.is.gd/wondercraftstats");
                player.getActionSender().sendChatMessage("&eTo see available maps, type &a/Maps");
                player.getActionSender().sendChatMessage("&eTo join a team, type either &1/Blue&e or &c/Red");
                player.getActionSender().sendChatMessage("&eTo send a private message, type &a@PlayerName Message");
            }
           else if (params.contain("commands")) {
                player.getActionSender().sendChatMessage("Help: Displays the Commands.");
            }
            else if (params.equals("red")) {
                player.getActionSender().sendChatMessage("Help: Join the Red team.");
            }
            else if (params.equals("blue")) {
                player.getActionSender().sendChatMessage("Help: Join the Blue team.");
            }
            else if (params.equals("team")) {
                player.getActionSender().sendChatMessage("Help: Talk to your team.");
            }
            else if (params.equals("rules")) {
                player.getActionSender().sendChatMessage("Help: Display yours or a players stats.");
            }
            else if (params.equals("me")) {
                player.getActionSender().sendChatMessage("Help: Say somthing in 3rd person.");
            }
            else if (params.equals("pm")) {
                player.getActionSender().sendChatMessage("Help: Talk privately with another user.");
            }
            else if (params.equals("water")) {
                player.getActionSender().sendChatMessage("Help: Start placing water. Say again to stop.");
            }
            else if (params.equals("lava")) {
                player.getActionSender().sendChatMessage("Help: Start placing lava. Say again to stop.");
            }
            else if (params.equals("status")) {
                player.getActionSender().sendChatMessage("Help: Display the status of the current game.");
            }
            else if (params.equals("stats")) {
                player.getActionSender().sendChatMessage("Help: Same as /pstats");
            }
            else if (params.equals("store")) {
                player.getActionSender().sendChatMessage("Help: See what's in the store!");
            }
            else if (params.equals("me")) {
                player.getActionSender().sendChatMessage("Help: Vote for a map at the end of a game.");
            }
            else if (params.equals("t")) {
                player.getActionSender().sendChatMessage("Help: Detonate your TNT.");
            }
            else if (params.equals("dtnt")) {
                player.getActionSender().sendChatMessage("Help: Deactivate your TNT.");
            }
            else if (params.equals("d")) {
                player.getActionSender().sendChatMessage("Help: Deactivate your mine.");
            }
            else if (params.equals("me")) {
                player.getActionSender().sendChatMessage("Help: Show the Devs.");
            }
        else {
                player.wrapText("FUCK!!!!!! It still doesn't work.");
            }
        }
        if (player.isVIP() || player.isOp() || player.isOwner()) {
            player.getActionSender().sendChatMessage("VIP+: /kick, /mute, /newgame, /join");
        }
        if (player.isOp() || player.isOwner()) {
            player.getActionSender().sendChatMessage("Op+: /ban, /xban, /banip, /say, /points");
        }
        if (player.isOwner()) {
            player.getActionSender().sendChatMessage("Owner: /modify, /op, /deop");
        }*/
    }

}

                       