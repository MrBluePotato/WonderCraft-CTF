/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bluecraft.game.impl.CTFGameMode;


public class DropItem implements Runnable{
    public int points;
    public int posX;
    public int posY;
    public int posZ;
    private Thread t;
    public DropItem(int p)
    {
        points = p;
        t = new Thread(this);
        t.start();
        World.getWorld().broadcast("&dA bookcase worth "+points+" points has been dropped somewhere on the map");
    }
    public void pickUp(Player p)
    {
        World.getWorld().broadcast(p.getColoredName()+" &dhas found "+points+" points.");
        p.addStorePoints(points);
        World.getWorld().getLevel().setBlock(posX, posY, posZ, 0);
        ((CTFGameMode)World.getWorld().getGameMode()).removeDropItem(this);
    }
    public void run()
    {
        posX = (int) (4 + Math.random() * (World.getWorld().getLevel().getWidth() - 8));
        posY = (int) (4 + Math.random() * (World.getWorld().getLevel().getHeight() - 8));
        posZ = World.getWorld().getLevel().ceiling - 8;
        ((CTFGameMode)World.getWorld().getGameMode()).addDropItem(this);
        boolean done = false;
        while(!done)
        {
            if(World.getWorld().getLevel().getBlock(posX, posY, posZ) != 7)
                World.getWorld().getLevel().setBlock(posX, posY, posZ, 0);
            posZ--;
            if(World.getWorld().getLevel().getBlock(posX, posY, posZ) != 0 && World.getWorld().getLevel().getBlock(posX, posY, posZ) != 11 || posZ < 0)
            {
                done = true;
                posZ++;
                World.getWorld().getLevel().setBlock(posX, posY, posZ, 47);
            }
            else
            {
                World.getWorld().getLevel().setBlock(posX, posY, posZ, 47);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
        }
    }
}
