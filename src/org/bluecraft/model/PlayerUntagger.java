/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

/**
 *
 * @author Jacob Morgan
 */
public class PlayerUntagger implements Runnable{
    private Player tagged;
    public PlayerUntagger(Player p)
    {
        tagged = p;
    }
    public void run() {
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException ex) {
        }
        tagged.safe = false;
    }

}
