/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Mine;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class DefuseCommand implements Command{
    private static final DefuseCommand INSTANCE = new DefuseCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static DefuseCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if(player.mine != null)
        {
            Mine m = player.mine;
            World.getWorld().removeMine(m);
            World.getWorld().getLevel().setBlock((m.x - 16)/32, (m.y - 16)/32,(m.z - 16)/32, 0);
            player.mine = null;
            player.getActionSender().sendChatMessage("- &eYour mine is no longer active!");
        }
        else {
            player.getActionSender().sendChatMessage("- &eYou have not placed a mine.");
        }
    }
}
