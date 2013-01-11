/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.game.impl.CTFGameMode;
import org.bluecraft.model.Level;
import org.bluecraft.model.MapController;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class NewGameCommand implements Command{
    private static final NewGameCommand INSTANCE = new NewGameCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static NewGameCommand getCommand() {
            return INSTANCE;
    }
    @Override
    public void execute(Player player, CommandParameters params) {
        if ((player.isOp()) || player.isVIP())
        {
            Server.log(player.getName()+" used /newgame");
            String mapName;
            try {
            mapName = params.getStringArgument(0);
            }
            catch(Exception ex)
            {
                mapName = null;
            }
            Level newMap;
            if(mapName == null) {
                newMap = MapController.randomLevel();
            }
            else {
                newMap = MapController.getLevel(mapName);
            }
            ((CTFGameMode)World.getWorld().getGameMode()).startGame(newMap);
        }
        else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }
}
