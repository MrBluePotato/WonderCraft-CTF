/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.game.impl.CTFGameMode;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class StatusCommand implements Command{

    private static final StatusCommand INSTANCE = new StatusCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static StatusCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        int redPlayers = 0;
        int bluePlayers = 0;
        String hasOtherFlag = null;
        String hasOurFlag = null;
        for (Player p : World.getWorld().getPlayerList().getPlayers())
        {
            if(p.team == 0) {
                redPlayers++;
            }
            else if(p.team == 1) {
                bluePlayers++;
            }
            if(p.hasFlag == true)
            {
                if(p.team == player.team) {
                    hasOtherFlag = p.getName();
                }
                else {
                    hasOurFlag = p.getName();
                }
            }
        }
        if(hasOtherFlag == null) {
            hasOtherFlag = "No one";
        }
        if(hasOurFlag == null) {
            hasOurFlag = "No one";
        }
        player.getActionSender().sendChatMessage("- &e"+redPlayers+" players on &cred:");
        Object[] names = World.getWorld().getPlayerList().getPlayers().toArray();
        if(names.length > 0)
        {
            String msg = "";
            for(Object map : names)
            {
                if(((Player)map).team == 0) {
                    msg += ((Player)map).getName()+", ";
                }
            }
            String[] lines = Server.wrapText(msg, 60);
            if(!msg.equals("")) {
                for(String l : lines)
                {
                    player.getActionSender().sendChatMessage("- &c"+l);
                }
            }
        }
        player.getActionSender().sendChatMessage("- &e"+bluePlayers+" players on &1blue:");
        Object[] othernames = World.getWorld().getPlayerList().getPlayers().toArray();
        String othermsg = "";
        for(Object map : othernames)
        {
            if(((Player)map).team == 1) {
                othermsg += ((Player)map).getName()+", ";
            }
        }
        String[] otherlines = Server.wrapText(othermsg, 60);
        if(!othermsg.equals("")) {
            for(String l : otherlines)
            {
                player.getActionSender().sendChatMessage("- &c"+l);
            }
        }
        player.getActionSender().sendChatMessage("&e"+hasOtherFlag+" has the other flag.");
        player.getActionSender().sendChatMessage("&e"+hasOurFlag+" has your flag.");
        player.getActionSender().sendChatMessage("&cRed: "+((CTFGameMode)World.getWorld().getGameMode()).getRedCaptures()+" -- &1Blue: "+((CTFGameMode)World.getWorld().getGameMode()).getBlueCaptures());
        //player.getActionSender().sendChatMessage("&aCurrent map:"+ ((CTFGameMode)World.getWorld().getL);
        //newMap = MapController.getLevel(mapName);
    }

}
