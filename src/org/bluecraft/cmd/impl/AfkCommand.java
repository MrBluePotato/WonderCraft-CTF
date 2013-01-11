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
 * @author Quinton Marchi is a lame butt
 * @author Jacob Morgan
 */
public class AfkCommand implements Command {
    
    private static final AfkCommand INSTANCE = new AfkCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static AfkCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(final Player player, CommandParameters params) {
        String text = "";
        if (player.muted == false){
        for(int i = 0; i < params.getArgumentCount(); i++)
        {
            text += " "+params.getStringArgument(i);
        }
                    if((!player.afk) && params.getArgumentCount() > 0)
                    {
                        
                        player.afk = true;
                        Server.log(player.getName()+" is now AFK");
                        World.getWorld().broadcast(player.getColoredName()+" &eis now AFK.&1 ("+text+" )");
                        player.getActionSender().sendChatMessage("&8Type /Afk to return");
                        
                    }
                    else if ((!player.afk) && params.getArgumentCount() < 1)
                        {
                        
                        player.afk = true;
                        Server.log(player.getName()+" is now AFK");
                        World.getWorld().broadcast(player.getColoredName()+" &eis now AFK &1( Away From Keyboard )");
                        player.getActionSender().sendChatMessage("&8Type /Afk to return");
                        }
                    else
                    {
                        
                        player.afk = false;
                        Server.log(player.getName()+"is back!");
                        World.getWorld().broadcast(player.getColoredName()+" &eis no longer AFK &1( Away from Keyboard )");
                    }
           } 
        else {
            player.getActionSender().sendChatMessage("&cYou cannot use this command while you are muted.");
        }     
            
    }

}
