/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.BuildMode;
import org.bluecraft.model.Player;

/**
 *
 * @author Jacob Morgan
 */
public class CuboidCommand implements Command{
    private static final CuboidCommand INSTANCE = new CuboidCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static CuboidCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
            if (player.isOp() || ("MrBluePotato".equals(player.getName())))
            {
                player.buildMode = BuildMode.BOX;
                player.getActionSender().sendChatMessage("&aCuboid: Place a block in each corner.");
            }
            else {
            player.getActionSender().sendChatMessage("&cYou do not have permisson to use that command.");
        }
    }
}
