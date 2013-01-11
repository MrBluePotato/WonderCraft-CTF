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
public class MuteCommand implements Command {
    
    private static final MuteCommand INSTANCE = new MuteCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static MuteCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
       
        if (player.isOwner() == true || player.isVIP() == true || player.isOp() == true)
        {
            for (Player other : World.getWorld().getPlayerList().getPlayers()) {
                if (other.getName().toLowerCase().equals(params.getStringArgument(0).toLowerCase()) ) {
                    
                    
                    if(other.muted == false)
                    { 
                    
                        other.muted = true;
                        Server.log(player.getName()+" muted "+other.getName());
                        World.getWorld().broadcast(player.getName()+" &emuted "+other.getName());
                        other.getActionSender().sendChatMessage("&cYou were muted by "+player.getName());
                        //other.getActionSender().sendChatMessage("- &eSay /mute [name] again to unmute them");
                    }
                else if(other.muted == true)
                    {
                        
                        other.muted = false;
                        Server.log(player.getName()+" unmuted "+other.getName());
                        World.getWorld().broadcast(player.getName()+" unmuted "+other.getName());
                        other.getActionSender().sendChatMessage("&cYou are no longer muted.");
                    }
                
                else{
                    player.wrapText("&cMake sure you enter the command correct. Ex: &a/mute PlayerName");
                }}

        }}
        
        
        
        else
        {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
      
    }

}