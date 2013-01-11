/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class TeamCommand implements Command{

    private static final TeamCommand INSTANCE = new TeamCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static TeamCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if(params.getArgumentCount() == 0) {
            return;
        }
        String text = "";
        for(int i = 0; i < params.getArgumentCount(); i++)
        {
            text += " "+params.getStringArgument(i);
        }
        for (Player t : World.getWorld().getPlayerList().getPlayers())
        {
            if(t.team == player.team)
            {
                t.getActionSender().sendChatMessage("[TeamChat]"+player.getColoredName()+"&f:"+text);
            }
        }
    }

}
