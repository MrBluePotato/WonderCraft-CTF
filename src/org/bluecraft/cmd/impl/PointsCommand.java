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
public class PointsCommand implements Command {
    private static final PointsCommand INSTANCE = new PointsCommand();

    public static PointsCommand getCommand() {
            return INSTANCE;
    }


    @Override
    public void execute(Player player, CommandParameters params) {
        if(params.getArgumentCount() != 2) {
            player.getActionSender().sendChatMessage("&a/Points UserName PointValue");
        }
        else if(player.isOp())
        {
            for (Player other : World.getWorld().getPlayerList().getPlayers()) {
                if (other.getName().toLowerCase().equals(params.getStringArgument(0).toLowerCase())) {
                    other.setStorePoints(params.getIntegerArgument(1));
                    return;
                }
            }
        }
        else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }
}
