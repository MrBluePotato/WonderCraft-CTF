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

//Rape ©2013 MrBluePotato (Michael Cummings) <MrBluePotato@wondercraft.org>
public class RapeCommand implements Command{

	private static final RapeCommand INSTANCE = new RapeCommand();

	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static RapeCommand getCommand() {
		return INSTANCE;
	}


	@Override
	public void execute(Player player, CommandParameters params) {
            if (player.isOwner())
            {
                    for (Player other : World.getWorld().getPlayerList().getPlayers()) {
                            if (other.getName().toLowerCase().equals(params.getStringArgument(0).toLowerCase())) {
                                    Server.log(player.getName()+" raped "+other.getName());
                                    World.getWorld().broadcast(other.parseName()+" was &draped&e by "+(player.getName()));
                                    return;
                            }
                    }
            }
            else {
                player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
            }
	}
}
