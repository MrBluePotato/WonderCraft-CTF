/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

/**
 *
 * @author Jacob Morgan
 */
public class Mine {
    public int x;
    public int y;
    public int z;
    public int team;
    public Player owner;
    public boolean active = false;
    public Mine(int x, int y, int z, int team, Player owner)
    {
        this.x = x * 32 + 16;
        this.y = y * 32 + 16;
        this.z = z * 32 + 16;
        this.team = team;
        this.owner = owner;
    }
}
