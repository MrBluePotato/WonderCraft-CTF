/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;

//Info command ©2013 MrBluePotato (Michael Cummings) <MrBluePotato@wondercraft.org>
public class InfoCommand implements Command{
    private static final InfoCommand INSTANCE = new InfoCommand();

    public static InfoCommand getCommand() {
            return INSTANCE;
    }
    
    @Override
    public void execute(Player player, CommandParameters params){
        player.wrapText("Server Info:");
        player.wrapText("Running &1BlueCraft &70.100 &eon &dUbuntu 12.04 LTS");
        player.wrapText("There are '&731&e' maps available");
        
        
    }
    
}
