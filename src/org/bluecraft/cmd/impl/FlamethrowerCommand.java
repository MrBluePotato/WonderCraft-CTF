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
public class FlamethrowerCommand implements Command{

    private static final FlamethrowerCommand INSTANCE = new FlamethrowerCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static FlamethrowerCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        player.getActionSender().sendChatMessage("- &eAvailable commands: /help, /red, /blue, /spec, /me, /status, /score");
        player.getActionSender().sendChatMessage("- &e/team, /build[water, lava], /pm, /stats, /t, /d, /dtnt");
        player.getActionSender().sendChatMessage("- &e/store, /buy, /g");

    }

}
