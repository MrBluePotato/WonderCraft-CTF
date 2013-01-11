/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

import java.util.ArrayList;
import java.util.Iterator;


public class Killstats {
    public static ArrayList<PlayerKillRecord> killRecords = new ArrayList<PlayerKillRecord>(32);
    static class PlayerKillRecord
    {
        private Player dominator = null;
        public Player p1;
        public Player p2;
        public int balance;
        public boolean equals(Object o)
        {
            if(!(o instanceof PlayerKillRecord))
                return false;
            PlayerKillRecord other = (PlayerKillRecord) o;
            return (other.p1 == p1 && other.p2 == p2) || (other.p2 == p1 && other.p1 == p2);
        }
        public void checkBalance()
        {
            if(balance >= 5 && dominator == null)
            {
                dominator = p1;
                World.getWorld().broadcast("- "+p1.getColoredName()+"&b is DOMINATING "+p2.getColoredName());
            }
            else if(balance <= -5 && dominator == null)
            {
                dominator = p2;
                World.getWorld().broadcast("- "+p2.getColoredName()+"&b is DOMINATING "+p1.getColoredName());
            }
            else if(balance < 5 && dominator == p1)
            {
                dominator = null;
                World.getWorld().broadcast("- "+p2.getColoredName()+"&b got REVENGE on "+p1.getColoredName());
            }
            else if(balance > -5 && dominator == p2)
            {
                dominator = null;
                World.getWorld().broadcast("- "+p1.getColoredName()+"&b got REVENGE on "+p2.getColoredName());
            }
        }
    }
    public static PlayerKillRecord getKillRecord(Player p1, Player p2)
    {
        PlayerKillRecord test = new PlayerKillRecord();
        test.p1 = p1;
        test.p2 = p2;
        int idx = killRecords.indexOf(test);
        if(idx == -1)
        {
            killRecords.add(test);
            return test;
        }
        else
        {
            return killRecords.get(idx);
        }
    }
    public static void kill(Player attacker, Player defender)
    {
        PlayerKillRecord kr = getKillRecord(attacker, defender);
        if(attacker == kr.p1)
            kr.balance++;
        else
            kr.balance--;
        kr.checkBalance();
    }
    public static void removePlayer(Player player)
    {
        Iterator<PlayerKillRecord> itr = killRecords.iterator();
        while(itr.hasNext())
        {
            PlayerKillRecord kr = itr.next();
            if(kr.p1 == player || kr.p2 == player)
                itr.remove();
        }
    }
}
