package org.bluecraft.cmd;

/*
 * Insert REALLY LONG Opencraft License here.
 */

import org.bluecraft.model.Player;
import java.io.IOException;

/**
 * Represents a specific command.
 * @author Graham Edgecombe
 */
public interface Command {
	
	/**
	 * Executes this command for the specified player.
	 * @param player The player.
	 * @param params The parameters.
	 */
	public void execute(Player player, CommandParameters params) throws IOException;
	
}
