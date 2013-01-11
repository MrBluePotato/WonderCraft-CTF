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

//Tickle ©2013 MrBluePotato (Michael Cummings) <MrBluePotato@wondercraft.org>
public class TickleCommand implements Command{

	private static final TickleCommand INSTANCE = new TickleCommand();

	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static TickleCommand getCommand() {
		return INSTANCE;
	}


	@Override
	public void execute(Player player, CommandParameters params) {
            if (player.isVIP() || (player.isOp() || (player.isOwner())))
            {
                    for (Player other : World.getWorld().getPlayerList().getPlayers()) {
                            if (other.getName().toLowerCase().equals(params.getStringArgument(0).toLowerCase())) {
                                    Server.log(player.getName()+" tickled "+other.getName());
                                    World.getWorld().broadcast(other.parseName()+" was &dtickled&e by "+(player.getName()));
                                    return;
                            }
                    }
            }
            else {
                player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
            }
	}
}
