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
 * @author Quinton Marchi
 */
public class ShieldCommand implements Command{

    private static final ShieldCommand INSTANCE = new ShieldCommand();

    public static ShieldCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if(player.hasShield == false) {
            player.activateShield();
        }
        else {
            player.getActionSender().sendChatMessage("- &eYou need to buy shield from the store!");
        }
            
    }
}
