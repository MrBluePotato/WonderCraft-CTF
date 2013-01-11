/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.game.impl.CTFGameMode;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class EndCommand implements Command{

    private static final EndCommand INSTANCE = new EndCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static EndCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if (player.isOp() || player.isVIP() || player.isOwner())
        {
            Server.log(player.getName()+" used /end");
            ((CTFGameMode)World.getWorld().getGameMode()).endGame();
        }
        else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }
}
