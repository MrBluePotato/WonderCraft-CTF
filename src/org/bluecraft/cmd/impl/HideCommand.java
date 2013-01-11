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
//Hide ©2013 MrBluePotato (Michael Cummings) <MrBluePotato@wondercraft.org>
//Hide was re-designed, written, and re-worked
public class HideCommand implements Command{
    private static final HideCommand INSTANCE = new HideCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static HideCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
		if (player.isOp() && params.getArgumentCount() == 0) {
                    if(player.isVisible)
                    {
                        Server.log(player.getName()+" is now hidden.");
                        player.makeInvisible();
                        player.getActionSender().sendChatMessage("&8Type /hide again to unhide");
                        World.getWorld().broadcast(player.getColoredName()+"&e left the server");
                        player.isVisible = false;
                    }
                    else
                    {
                        Server.log(player.getName()+" is now unhidden");
                        player.makeVisible();
                        player.getActionSender().sendChatMessage("&8You are now unhidden");
                        World.getWorld().broadcast(player.getColoredName()+"&e joined the game");
                        player.isVisible = true;
                    }
		}
                else if (player.isOp() && params.getArgumentCount() == 1){
                if(player.isVisible)
                    {
                        Server.log(player.getName()+" is now hidden");
                        player.makeInvisible();
                        player.getActionSender().sendChatMessage("&8Type /hide again to unhide");
                        World.getWorld().broadcast(player.getColoredName()+"&e left the server");
                        player.isVisible = false;
                    }
                    else
                    {
                        Server.log(player.getName()+" is now unhidden");
                        player.makeVisible();
                        player.getActionSender().sendChatMessage("&8You are now unhidden");
                        World.getWorld().broadcast(player.getColoredName()+"&e joined the game");
                        player.isVisible = true;
                    }
                }
                else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }
}
