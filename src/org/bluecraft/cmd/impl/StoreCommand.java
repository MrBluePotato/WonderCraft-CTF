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
public class StoreCommand implements Command{
    private static final StoreCommand INSTANCE = new StoreCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static StoreCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        player.getActionSender().sendChatMessage("- &aYou have "+player.getStorePoints()+" points.");
        Object[] items = Server.getStore().getItems();
        int i = 1;
        for(Object obj : items)
        {
            String msg = "- &a"+i+". ";
            StoreItem item = (StoreItem) obj;
            msg = msg + item.name + " - " + item.description + " - " + item.price+" - /"+item.command;
            player.getActionSender().sendChatMessage(msg);
            i++;
        }
        player.getActionSender().sendChatMessage("- &aSay a command to buy something!");
    }
}
