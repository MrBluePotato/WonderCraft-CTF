/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class OpChatCommand implements Command{

    private static final OpChatCommand INSTANCE = new OpChatCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static OpChatCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if (player.isOp()) {
                if (params.getArgumentCount() == 0) {
                        player.getActionSender().sendChatMessage("No message to send");
                        player.getActionSender().sendChatMessage("/say <message>");
                        return;
                }
                String text = "";
                for(int i = 0; i < params.getArgumentCount(); i++)
                {
                    text += " "+params.getStringArgument(i);
                }
                Server.log("[OpChat]:"+(player.getName()+":"+text));
                for (Player t : World.getWorld().getPlayerList().getPlayers())
                {
                    if(t.isOp())
                    {
                        t.getActionSender().sendChatMessage("&3[OpChat] "+player.parseName() + "&f: " + text);
                    }
                }
        } else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }
}
