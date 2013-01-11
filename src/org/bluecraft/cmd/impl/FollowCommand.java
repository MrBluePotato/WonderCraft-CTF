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
public class FollowCommand implements Command{

	private static final FollowCommand INSTANCE = new FollowCommand();

	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static FollowCommand getCommand() {
		return INSTANCE;
	}


	@Override
	public void execute(Player player, CommandParameters params) {
            if (player.isOp())
            {
                if(params.getArgumentCount() == 1) {
                    for (Player other : World.getWorld().getPlayerList().getPlayers()) {
                            if (other.getName().toLowerCase().equals(params.getStringArgument(0).toLowerCase())) {
                                player.follow(other);
                                player.getActionSender().sendChatMessage("&aFollow: You are now following"+other.getName());
                                player.getActionSender().sendChatMessage("&aType /follow to stop");
                                Server.log(player.getName()+" is now following "+other.getName());
                            }
                    }
                }
                else {
                    player.follow(null);
                }
            }
            else {
                player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
            }
	}
}
