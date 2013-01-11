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
public class GhostCommand implements Command{

    private static final GhostCommand INSTANCE = new GhostCommand();

    public static GhostCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if(player.hasGhost == false) {
            player.getActionSender().sendChatMessage("- &eYou need to buy ghost mode from the store.");
        }
        else {
            player.activateGhost();
        }
    }
}
