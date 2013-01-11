/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.model;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import org.bluecraft.Constants;
import org.bluecraft.Server;

/**
 *
 * @author Jacob Morgan
 */
public class MapController {
    public static Vector<String> levelNames = new Vector<String>(16);
    private static HashMap<String, Integer> levelVotes = new HashMap<String, Integer>(16);
    private static int nLevels = 0;
    public static void create()
    {
        File dir = new File(Constants.ROOT_PATH+"/maps");
        String[] maps = dir.list();
        for(String mapName : maps)
        {
            if(mapName.contains(".dat"))
            {
                levelNames.add(mapName.replace(".dat", ""));
                levelVotes.put(mapName.replace(".dat", ""), 0);
            }
                //registerLevel(Constants.ROOT_PATH+"/maps/"+mapName, mapName.replace(".dat", ""));
        }
        nLevels = levelNames.size();
    }
    public static Level randomLevel()
    {
        int r = Server.r.nextInt(nLevels);
        String name = levelNames.get(r);
        return new Level().load("maps/"+name+".dat", name);
    }
    public static Level getLevel(String id)
    {
        try
        {
            return new Level().load("maps/"+id+".dat", id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    public static boolean addVote(String id)
    {
        levelVotes.put(id, levelVotes.get(id) + 1);
        return true;
    }
    public static void resetVotes()
    {
        for(int i = 0; i < nLevels; i++)
        {
            levelVotes.put(levelNames.get(i), 0);
        }
    }
    public static Level getMostVotedForMap()
    {
        int highestVotes = 0;
        String highest = null;
        for(String l : levelVotes.keySet())
        {
            int votes = levelVotes.get(l);
            if(votes > highestVotes)
            {
                highest = l;
                highestVotes = votes;
            }
        }
        if(highest == null)
            return randomLevel();
        else
            return new Level().load("maps/"+highest+".dat", highest);
    }
}
