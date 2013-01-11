/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.game.impl.CTFGameMode;
import org.bluecraft.model.MapController;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class VoteCommand implements Command{
    private static final VoteCommand INSTANCE = new VoteCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static VoteCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        if(params.getArgumentCount() == 1)
        {
            if(player.hasVoted) {
                player.getActionSender().sendChatMessage("- &eYou have already voted!");
            }
            else if(((CTFGameMode)World.getWorld().getGameMode()).voting == false) {
                player.getActionSender().sendChatMessage("- &eVoting is not currently open!");
            }
            else
            {
                boolean r = MapController.addVote(params.getStringArgument(0));
                if(r == false) {
                    player.getActionSender().sendChatMessage("- &eUnknown map!");
                }
                else {
                    player.hasVoted = true;
                }
            }
        }
    }
}
