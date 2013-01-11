package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;

public class SolidCommand implements Command{
    private static final SolidCommand INSTANCE = new SolidCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static SolidCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
		if (player.isOp())
                {
                    if(player.placeSolid)
                    {
                        player.placeSolid = false;
                        player.getActionSender().sendChatMessage("&8No longer placing adminium");
                    }
                    else
                    {
                        player.placeSolid = true;
                        player.getActionSender().sendChatMessage("&8Now placing adminium");
                    }
                }
                else {
            player.getActionSender().sendChatMessage("&cYou do not have permission to use that command.");
        }
    }
}
