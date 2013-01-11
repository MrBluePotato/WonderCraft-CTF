/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.task.impl;

import java.util.Random;
import org.bluecraft.model.World;
import org.bluecraft.task.ScheduledTask;

/**
 *
 * @author Jacob Morgan
 */
public class MessageTask extends ScheduledTask{

    private static final long DELAY = 3 * 60 * 1000;

    private Random r = new Random();

    public MessageTask() {
            super(0);
    }

    public void execute() {
        if (this.getDelay() == 0) {
                this.setDelay(DELAY);
        }
        int id = r.nextInt(4);
        String msg = "_";
        switch(id)
        {
            case 0:
                msg = "Remember to throw out old/rotting vegatbles.";
            break;
            case 1:
                msg = "Use appropriate language.";
            break;
            case 2:
                msg = "Have you read the &8/rules &eyet?";
            break;
            case 3:
                msg = "Check your score! www.is.gd/wondercraftstats";
            break;
            case 4:
                msg = "Have you read the &7/rules &eyet?";
            break;
            case 5:
                msg = "Visit our forum at &1www.forum.wondercraft.org";
            break;
            case 6:
                msg = "The server is a very naughty potato";
            break;
            case 7:
                msg = "Be nice to people, and they will be nice to you.";
            break;
            case 8:
                msg = "Just want to build? Join WonderCraft 24/7 Freebuild!";
            break;
            case 9:
                msg = "If you want to become a VIP, then donate! &1www.wondercraft.org/donate";
            break;
            case 10:
                msg = "Use common sense.";
            break;
            case 11:
                msg= "Just want to build? Then join &7WonderCraft 24/7 Freebuild";
            break;
            case 12:
                msg= "Server powered by &1BlueCraft";
            break;
        }
        World.getWorld().broadcast("&c[A]&f -&e "+msg);
    }

}