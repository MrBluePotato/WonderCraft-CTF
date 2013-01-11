/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bluecraft.model.impl;

import org.bluecraft.model.BuildMode;
import org.bluecraft.model.Player;
import org.bluecraft.model.StoreItem;

/**
 *
 * @author Jacob
 */
public class TeleporterItem extends StoreItem{
    public TeleporterItem(String n, int p)
    {
        super(n, p);
    }
    
    @Override
    public StoreItem getCopy() {
        return new TeleporterItem(name, price);
    }

    @Override
    public void activate(Player p) {
        p.buildMode = BuildMode.TELE_ENTRANCE;
    }
    
}
