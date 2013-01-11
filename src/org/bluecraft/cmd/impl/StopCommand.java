/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bluecraft.cmd.impl;

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;
import java.io.IOException;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author qmarchi
 */
public class StopCommand implements Command{
    private static final StopCommand INSTANCE = new StopCommand();
        public static StopCommand getCommand() {
            return INSTANCE;
        }
    @Override
  public void execute(Player player, CommandParameters params) throws IOException {
      
      	// Player using command is OP?
		if (player.isOwner()) {
                    
                    
                }
                else{
                player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
                Server.log(player.getName()+" attempted to shutdown the server");
                }
                
	}
      
      
      
  }  

