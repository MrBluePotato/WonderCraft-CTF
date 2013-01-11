/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;
import org.bluecraft.model.StoreItem;

/**
 *
 * @author Jacob Morgan
 */
public class ActivateItemCommand implements Command{
        private StoreItem item = null;
        public ActivateItemCommand(StoreItem item)
        {
            super();
            this.item = item;
        }

	@Override
	public void execute(Player player, CommandParameters params) {
            if(player.team == -1) {
                player.getActionSender().sendChatMessage("- &eYou need to join a team to do that!");
            }
            else
            {
                if(!(item.name.equals("Brush") && player.brush == true))
                {
                    boolean r = Server.getStore().buy(player, item.name);
                    if(r) {
                        item.activate(player);
                    }
                }
                else {
                    item.activate(player);
                }
            }
        }
}
