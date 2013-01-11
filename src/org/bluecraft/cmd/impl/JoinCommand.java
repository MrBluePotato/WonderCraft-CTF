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
public class JoinCommand implements Command{

	/**
	 * The instance of this command.
	 */
	private static final JoinCommand INSTANCE = new JoinCommand();

	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static JoinCommand getCommand() {
		return INSTANCE;
	}

	/**
	 * Default private constructor.
	 */
	private JoinCommand() {
		/* empty */
	}

	@Override
	public void execute(Player player, CommandParameters params) {
            if (player.isVIP() || player.isOp() || player.isOwner()) {
                player.joinTeam(params.getStringArgument(0));
            }
        }
}
