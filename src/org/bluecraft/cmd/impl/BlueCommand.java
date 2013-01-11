/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.Constants;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;

/**
 *
 * @author Jacob Morgan
 */
public class BlueCommand implements Command{

    private static final BlueCommand INSTANCE = new BlueCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static BlueCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if(!Constants.tournament) {
            player.joinTeam("blue");
        }
    }

}
