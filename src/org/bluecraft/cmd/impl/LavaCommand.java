/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;

/**
 *
 * @author Jacob Morgan
 */
public class LavaCommand implements Command{

    private static final LavaCommand INSTANCE = new LavaCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static LavaCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if(player.placeBlock == -1)
        {
            player.placeBlock = 11;
            player.getActionSender().sendChatMessage("&cLava&e mode&f: On");
        }
        else
        {
            player.placeBlock = -1;
            player.getActionSender().sendChatMessage("&cLava&e mode&f: Off");
        }
    }

}
