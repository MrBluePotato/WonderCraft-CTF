/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

/**
 *
 * @author Jacob Morgan
 */
public abstract class StoreItem {
    public String name;
    public int price;
    public String description;
    public String command;
    public StoreItem(String n, int p)
    {
        name = n;
        price = p;
    }
    public abstract StoreItem getCopy();
    public abstract void activate(Player p);

}
