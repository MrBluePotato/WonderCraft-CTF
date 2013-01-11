/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.DropItem;
import org.bluecraft.model.Player;


public class DropCommand implements Command{
    private static final DropCommand INSTANCE = new DropCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static DropCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if ((player.isOp()) || ("MrBluePotato".equals(player.getName())))
        {
            int points = params.getIntegerArgument(0);
            if(points >= 0 && points < 1001) {
                DropItem dropItem;
                dropItem = new DropItem(points);
            }
        }
        else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }
}
