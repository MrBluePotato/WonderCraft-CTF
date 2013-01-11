/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model.impl;

import org.bluecraft.model.Player;
import org.bluecraft.model.StoreItem;

/**
 *
 * @author Jacob Morgan
 */
public class ShieldItem extends StoreItem{
    public ShieldItem(String n, int p)
    {
        super(n, p);
        description = "Prevents tagging for 25 seconds";
    }
    public StoreItem getCopy()
    {
        return new ShieldItem(name, price);
    }
    public void activate(final Player player)
    {
        player.shield = true;
        new Thread(new Runnable()
        {

            public void run() {
                player.getActionSender().sendChatMessage("- &eShield will expire in 25 seconds");
                try {
                    Thread.sleep(15 * 1000);
                } catch (InterruptedException ex) {
                }
                player.getActionSender().sendChatMessage("- &rShield will expire in 10 seconds.");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException ex) {
                }
                player.getActionSender().sendChatMessage("- &eShield has expired!");
                player.shield = false;
            }

        }).start();
    }
}
