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
public class ForceCommand implements Command{

    private static final ForceCommand INSTANCE = new ForceCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static ForceCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if ((player.isOp()))
        {
            String pname = params.getStringArgument(0);
            String tname = params.getStringArgument(1);
            for(Player p : World.getWorld().getPlayerList().getPlayers())
            {
                if(p.getName().equals(pname))
                {
                    Server.log(player.getName()+" forced "+player.getName()+" to "+tname);
                    p.joinTeam(tname);
                }
            }
        }
        else if ((player.isVIP())) {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
        else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }

}
