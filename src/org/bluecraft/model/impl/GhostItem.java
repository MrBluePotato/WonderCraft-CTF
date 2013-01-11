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
public class GhostItem extends StoreItem{
    public GhostItem(String n, int p)
    {
        super(n, p);
        description = "Make yourself invisible";
    }
    public StoreItem getCopy()
    {
        return new GhostItem(name, price);
    }

    public void activate(Player player) {
        player.activateGhost();
    }
}
