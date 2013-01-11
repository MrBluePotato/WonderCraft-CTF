/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.task.impl;

import org.bluecraft.game.impl.CTFGameMode;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;
import org.bluecraft.task.ScheduledTask;

/**
 *
 * @author Jacob Morgan
 */
public class CTFProcessTask extends ScheduledTask{

    private static final long DELAY = 500;
    private static CTFGameMode ctf = (CTFGameMode)World.getWorld().getGameMode();
    private static World world = World.getWorld();

    public CTFProcessTask()
    {
        super(DELAY);
    }

    public void execute() {
        for (Player player : world.getPlayerList().getPlayers())
        {
            ctf.processPlayerMove(player);
        }
    }

}
