/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class HiddenCommand implements Command{
    private static final HiddenCommand INSTANCE = new HiddenCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static HiddenCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
		if (player.isOp()) {
                    String message = "- &eHidden players: ";
                    for(Player p : World.getWorld().getPlayerList().getPlayers())
                    {
                        if(!p.isVisible) {
                            message += p.parseName()+",";
                        }
                    }
                    player.getActionSender().sendChatMessage(message);
		} else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }
}
