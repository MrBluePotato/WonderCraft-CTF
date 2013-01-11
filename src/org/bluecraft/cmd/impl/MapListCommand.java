/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.MapController;
import org.bluecraft.model.Player;

/**
 *
 * @author Jacob Morgan
 */
public class MapListCommand implements Command{
    private static final MapListCommand INSTANCE = new MapListCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static MapListCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        Object[] names = MapController.levelNames.toArray();
        player.getActionSender().sendChatMessage("- &eAvailable maps:");
        String msg = "";
        for(Object map : names)
        {
            msg += ", "+map;
        }
        String[] lines = Server.wrapText(msg, 60);
        for(String l : lines)
        {
            player.getActionSender().sendChatMessage("- &a"+l);
        }
    }
}
