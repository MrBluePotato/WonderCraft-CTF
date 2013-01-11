/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.fusesource.jansi.AnsiConsole;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;
import org.bluecraft.persistence.LoadPersistenceRequest;

/**
 *
 * @author Jacob Morgan
 */
public class PInfoCommand implements Command {

    private static final PInfoCommand INSTANCE = new PInfoCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static PInfoCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        Player target = null;
        for (Player other : World.getWorld().getPlayerList().getPlayers()) {
                if (other.getName().toLowerCase().equals(params.getStringArgument(0).toLowerCase())) {
                    target = other;
                    break;
                }
        }
        if(target == null)
        {
            target = new Player(null, params.getStringArgument(0));
            try
            {
                new LoadPersistenceRequest(target).perform();
            }
            catch(Exception e)
            {
                target = null;
            }
        }
        if(target != null)
        {
            if (player.isOwner()){
               AnsiConsole.out.println("- &eStats for "+target.getName());
               AnsiConsole.out.println("- &eWins: "+target.getAttribute("wins")+" - "+"Games Played: "+target.getAttribute("games")+" ");
               AnsiConsole.out.println("- &eTags: "+target.getAttribute("tags")+" - "+"Captures: "+target.getAttribute("captures")+" ");
               AnsiConsole.out.println("- &eExplodes: "+target.getAttribute("explodes")+" - "+"Mines: "+target.getAttribute("mines")+" ");
               AnsiConsole.out.println("- &eRagequits: "+target.getAttribute("ragequits"));
               AnsiConsole.out.println("- &eStore Points: "+target.getStorePoints());
               AnsiConsole.out.println("- &eIP:"+target.getSession().getIP());
            }
            player.getActionSender().sendChatMessage("- &eStats for "+target.getName());
            player.getActionSender().sendChatMessage("- &eWins: "+target.getAttribute("wins")+" - "+"Games Played: "+target.getAttribute("games")+" ");
            player.getActionSender().sendChatMessage("- &eTags: "+target.getAttribute("tags")+" - "+"Captures: "+target.getAttribute("captures")+" ");
            player.getActionSender().sendChatMessage("- &eExplodes: "+target.getAttribute("explodes")+" - "+"Mines: "+target.getAttribute("mines")+" ");
            player.getActionSender().sendChatMessage("- &eRagequits: "+target.getAttribute("ragequits"));
            player.getActionSender().sendChatMessage("- &eStore Points: "+target.getStorePoints());
            if(target != null && player.isOp() && target.getSession() != null) {
                player.getActionSender().sendChatMessage("- &eIP:"+target.getSession().getIP());
            }
        }
    }

}
