/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;

/**
 *
 * @author Jacob Morgan
 */
public class TutorialCommand implements Command{
    private static final TutorialCommand INSTANCE = new TutorialCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static TutorialCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
       player.wrapText("Try to capture the other team's flag and bring it back to your own side. Capture by clicking the other team's flag; return the flag by clicking your own flag. You can stop the enemy by tagging them when they're on your own side, blowing them up with TNT (place a TNT block, then say &a/t &eor &a/tnt &eto explode it) or placing landmines (dark gray blocks). You gain points for doing well; type &a/store &eto find out how many points you have and what you can buy.");
    }
}
