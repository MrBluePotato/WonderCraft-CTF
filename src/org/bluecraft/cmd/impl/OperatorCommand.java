package org.bluecraft.cmd.impl;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe, S�ren Enevoldsen and Brett Russell.
 * All rights reserved.
 *
 * Distribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Distributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Distributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the OpenCraft nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 * Official /op command **NEEDS PERSISTENCE**
 * @author S�ren Enevoldsen
 */

public class OperatorCommand implements Command {
	
	/**
	 * The instance of this command.
	 */
	private static final OperatorCommand INSTANCE = new OperatorCommand();
	
	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static OperatorCommand getCommand() {
		return INSTANCE;
	}
	
	/**
	 * Default private constructor.
	 */
	private OperatorCommand() {
		/* empty */
	}
	
	@Override
	public void execute(Player player, CommandParameters params) {
		// Player using command is OP?
		if (player.isOwner()) {
                    /*if(params.getArgumentCount() >= 2)
                    {
                        String text = "";
                        for(int i = 0; i < params.getArgumentCount(); i++)
                        {
                            text += " "+params.getStringArgument(i);
                        }
                        Server.failMsg = new String(new byte[]{0x000b})+text;
                    }
                    else*/ if (params.getArgumentCount() == 1) {
				for (Player other : World.getWorld().getPlayerList().getPlayers()) {
					if (other.getName().toLowerCase().equals(params.getStringArgument(0).toLowerCase())) {
                                                Server.log(player.getName()+" opped "+other.getName());
						other.setAttribute("IsOperator", "true");
						other.getActionSender().sendChatMessage("&eYou were promoted to op by "+(player.getName()));
						player.getActionSender().sendChatMessage(other.getName() + " &ewas promoted to op by"+(player.getName()));
						return;
					}
				}
				// Player not found
				player.getActionSender().sendChatMessage(params.getStringArgument(0) + " was not found.");
			} else
                        {
				player.getActionSender().sendChatMessage("Wrong number of arguments");
                                player.getActionSender().sendChatMessage("&a/Op UserName");
                        }
		} else {
                player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
            }
	}
}
