/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jacob Morgan
 */
public class MineActivator implements Runnable{
    private Mine mine;
    private Player player;
    public MineActivator(Mine m, Player p)
    {
        this.mine = m;
        this.player = p;
    }
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        mine.active = true;
        player.getActionSender().sendChatMessage("-&fYour &4mine&f is now active!");
    }

}
