/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

import org.bluecraft.cmd.impl.GrenadeCommand;
import org.bluecraft.cmd.impl.ShieldCommand;
import org.bluecraft.cmd.impl.ActivateItemCommand;
import org.bluecraft.cmd.impl.CreeperCommand;
import org.bluecraft.cmd.impl.LineCommand;
import org.bluecraft.cmd.impl.RocketCommand;
import org.bluecraft.cmd.impl.RemoteExplodeCommand;
import java.util.HashMap;
import org.bluecraft.game.impl.CTFGameMode;
import org.bluecraft.model.impl.BigTNTItem;
import org.bluecraft.model.impl.GhostItem;
//import org.opencraft.server.model.impl.ShieldItem;
import org.bluecraft.model.impl.SimpleItem;

/**
 *
 * @author Jacob Morgan
 */
public class Store {
    private HashMap<String, StoreItem> items = new HashMap<String, StoreItem>(16);
    public Store()
    {
        addItem("BigTNT", new BigTNTItem("BigTNT", 150), "bigtnt");
        addItem("Ghost", new GhostItem("Ghost", 175), "g");
        addItem("Rocket", new SimpleItem("Rocket", 50, "Shoots a rocket from your face", RocketCommand.getCommand()), "r");
        addItem("Grenade", new SimpleItem("Grenade", 20, "Throwable TNT", GrenadeCommand.getCommand()), "gr");
        addItem("Line", new SimpleItem("Line", 30, "Builds a bridge", LineCommand.getCommand()), "line");
        addItem("Creeper", new SimpleItem("Creeper", 40, "Makes you explode", CreeperCommand.getCommand()), "cr");
        addItem("RemoteExplode", new SimpleItem("RemoteExplode", 60, "Explodes where you are looking", RemoteExplodeCommand.getCommand()), "re");   
        addItem("Shield", new SimpleItem("Shield", 20, "Prevents tagging for 25 seconds", ShieldCommand.getCommand()), "Sh");
     }
    public boolean buy(Player p, String itemname)
    {
        StoreItem item = null;
        item = items.get(itemname);
        if(item == null)
        {
            p.getActionSender().sendChatMessage("- &eStore item does not exist.");
            return false;
        }
        if(item.price > p.getStorePoints())
        {
            p.getActionSender().sendChatMessage("- &eYou don't have enough points!");
            return false;
        }
        p.subtractStorePoints(item.price);
        p.getActionSender().sendChatMessage("- &eYou have "+p.getStorePoints()+" points left");
        return true;
    }
    public void addItem(String name, StoreItem item, String command)
    {
        items.put(name, item);
        item.command = command;
        ((CTFGameMode)World.getWorld().getGameMode()).registerCommand(command, new ActivateItemCommand(item));
    }
    public Object[] getItems()
    {
        return items.values().toArray();
    }
}
