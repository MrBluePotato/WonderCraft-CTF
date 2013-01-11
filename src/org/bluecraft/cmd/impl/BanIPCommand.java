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
public class BanIPCommand implements Command{

	private static final BanIPCommand INSTANCE = new BanIPCommand();

	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static BanIPCommand getCommand() {
		return INSTANCE;
	}


	@Override
	public void execute(Player player, CommandParameters params) {
            if (player.isOp() || ("MRBluePotato".equals(player.getName())))
            {
                if(params.getStringArgument(0).contains("."))
                {
                    Server.log(player.getName()+" IP banned "+params.getStringArgument(0));
                    Server.banIP(params.getStringArgument(0));
                    player.getActionSender().sendChatMessage(params.getStringArgument(0)+" has been IP banned.");
                }
                else {
                    for (Player other : World.getWorld().getPlayerList().getPlayers()) {
                            if (other.getName().toLowerCase().equals(params.getStringArgument(0).toLowerCase())) {
                                    Server.banIP(other.getSession().getIP());
                                    Server.log(player.getName()+" IP banned "+other.getName());
                                    other.getActionSender().sendLoginFailure("Sorry you were banned! Appeal at www./is.gd/wcbanappeal");
                                    other.getSession().close();
                                    player.getActionSender().sendChatMessage(other.getSession().getIP()+" has been IP banned.");
                                    World.getWorld().broadcast("- "+other.parseName() + " has been banned!");
                                    return;
                            }
                    }
                }
            }
            else {
                player.getActionSender().sendChatMessage("&cYou do not have permisson to use that command.");
            }
	}
}
