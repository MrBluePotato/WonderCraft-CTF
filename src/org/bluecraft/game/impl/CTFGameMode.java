/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.game.impl;

import org.bluecraft.cmd.impl.FollowCommand;
import org.bluecraft.cmd.impl.SayCommand;
import org.bluecraft.cmd.impl.UnbanIPCommand;
import org.bluecraft.cmd.impl.AfkCommand;
import org.bluecraft.cmd.impl.HugCommand;
import org.bluecraft.cmd.impl.TutorialCommand;
import org.bluecraft.cmd.impl.StoreCommand;
import org.bluecraft.cmd.impl.RapeCommand;
import org.bluecraft.cmd.impl.HideCommand;
import org.bluecraft.cmd.impl.TntCommand;
import org.bluecraft.cmd.impl.DeOperatorCommand;
import org.bluecraft.cmd.impl.StatsCommand;
import org.bluecraft.cmd.impl.UnbanCommand;
import org.bluecraft.cmd.impl.TeleportCommand;
import org.bluecraft.cmd.impl.DefuseCommand;
import org.bluecraft.cmd.impl.SolidCommand;
import org.bluecraft.cmd.impl.DefuseTNTCommand;
import org.bluecraft.cmd.impl.MapListCommand;
import org.bluecraft.cmd.impl.DropCommand;
import org.bluecraft.cmd.impl.RagequitCommand;
import org.bluecraft.cmd.impl.LavaCommand;
import org.bluecraft.cmd.impl.EndCommand;
import org.bluecraft.cmd.impl.ModifyCommand;
import org.bluecraft.cmd.impl.ShieldCommand;
import org.bluecraft.cmd.impl.PmCommand;
import org.bluecraft.cmd.impl.BanIPCommand;
import org.bluecraft.cmd.impl.TickleCommand;
import org.bluecraft.cmd.impl.ForceCommand;
import org.bluecraft.cmd.impl.MeCommand;
import org.bluecraft.cmd.impl.MuteCommand;
import org.bluecraft.cmd.impl.WhoCommand;
import org.bluecraft.cmd.impl.JoinCommand;
import org.bluecraft.cmd.impl.HiddenCommand;
import org.bluecraft.cmd.impl.UnmuteCommand;
import org.bluecraft.cmd.impl.PointsCommand;
import org.bluecraft.cmd.impl.VoteCommand;
import org.bluecraft.cmd.impl.PInfoCommand;
import org.bluecraft.cmd.impl.WaterCommand;
import org.bluecraft.cmd.impl.BanCommand;
import org.bluecraft.cmd.impl.OpChatCommand;
import org.bluecraft.cmd.impl.OperatorCommand;
import org.bluecraft.cmd.impl.StatusCommand;
import org.bluecraft.cmd.impl.KissCommand;
import org.bluecraft.cmd.impl.RedCommand;
import org.bluecraft.cmd.impl.BanXCommand;
import org.bluecraft.cmd.impl.CuboidCommand;
import org.bluecraft.cmd.impl.InfoCommand;
import org.bluecraft.cmd.impl.NewGameCommand;
import org.bluecraft.cmd.impl.KickCommand;
import org.bluecraft.cmd.impl.TeamCommand;
import org.bluecraft.cmd.impl.BlueCommand;
import org.bluecraft.cmd.impl.RulesCommand;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;
import org.bluecraft.Configuration;
import org.bluecraft.Constants;
import org.bluecraft.Server;
import org.bluecraft.game.GameModeAdapter;
import org.bluecraft.model.BuildMode;
import org.bluecraft.model.DropItem;
import org.bluecraft.model.Level;
import org.bluecraft.model.MapController;
import org.bluecraft.model.Mine;
import org.bluecraft.model.MineActivator;
import org.bluecraft.model.Player;
import org.bluecraft.model.PlayerUntagger;
import org.bluecraft.model.Position;
import org.bluecraft.model.Teleporter;
import org.bluecraft.model.World;
import org.bluecraft.persistence.SavePersistenceRequest;

/**
 *
 * @author Quinton Marchi
 */
public class CTFGameMode extends GameModeAdapter<Player>{

        /*
         * Red Flag Position
         */
        public int redFlagX;
        public int redFlagY;
        public int redFlagZ;
        /*
         * Blue Flag Position
         */
        public int blueFlagX;
        public int blueFlagY;
        public int blueFlagZ;
        /*
         * Initial Captures
         */
        public int redCaptures;
        public int blueCaptures;
        /*
         * Set Initial Players
         */
        public int bluePlayers = 0;
        public int redPlayers = 0;
        /*
         * Max amount of Captures
         */
        public int maxCaptures = 5;
        /*
         * Unknownst
         */
        public static int blockSpawnX;
        public static int blockSpawnY;
        public static int blockSpawnZ;
        
        private static final Logger logger = Logger.getLogger(Server.class.getName());
        /*
         * Make sure flags are not taken!
         */
        public boolean redFlagTaken = false;
        public boolean blueFlagTaken = false;
        /*
         * Reset Anti-Stalemate Mode
         */
        public boolean antiStalemate;

        public Level startNewMap;
        private Level map;

        public boolean voting = false;
        public boolean isFirstBlood = true;

        public boolean ready = false;

        private Vector<DropItem> items = new Vector<DropItem>(8);
	public CTFGameMode() {
		//Register the commands for use
                registerCommand("afk", AfkCommand.getCommand());
                registerCommand("ban", BanCommand.getCommand());
                registerCommand("banx", BanXCommand.getCommand());
                registerCommand("banip", BanIPCommand.getCommand());
                registerCommand("blue", BlueCommand.getCommand());
                registerCommand("cuboid", CuboidCommand.getCommand());
                registerCommand("deop", DeOperatorCommand.getCommand());
                registerCommand("d", DefuseCommand.getCommand());
		registerCommand("defuse", DefuseCommand.getCommand());
		registerCommand("dtnt", DefuseTNTCommand.getCommand());
		registerCommand("defusetnt", DefuseTNTCommand.getCommand());
                registerCommand("end", EndCommand.getCommand());
                registerCommand("follow", FollowCommand.getCommand());
                registerCommand("force", ForceCommand.getCommand());
                registerCommand("hide", HideCommand.getCommand());
                registerCommand("cuboid", CuboidCommand.getCommand());
                registerCommand("help", TutorialCommand.getCommand());
                registerCommand("hidden", HiddenCommand.getCommand());
                registerCommand("lava", LavaCommand.getCommand());
                registerCommand("say", SayCommand.getCommand());
		registerCommand("mute", MuteCommand.getCommand());
		registerCommand("kick", KickCommand.getCommand());
                registerCommand("kiss", KissCommand.getCommand());
                registerCommand("hug", HugCommand.getCommand());
                registerCommand("info", InfoCommand.getCommand());
                registerCommand("join", JoinCommand.getCommand());
                registerCommand("me", MeCommand.getCommand());
                registerCommand("modify", ModifyCommand.getCommand());
                registerCommand("maps", MapListCommand.getCommand());
                registerCommand("newgame", NewGameCommand.getCommand());
                registerCommand("op", OperatorCommand.getCommand());
		registerCommand("opchat", OpChatCommand.getCommand());
                registerCommand("opc", OpChatCommand.getCommand());
                registerCommand("pm", PmCommand.getCommand()); //Try to remove :P
                registerCommand("pstats", PInfoCommand.getCommand());
                registerCommand("rules", RulesCommand.getCommand());
		registerCommand("red", RedCommand.getCommand());
                registerCommand("rape", RapeCommand.getCommand());
                registerCommand("ragequit", RagequitCommand.getCommand());
                registerCommand("rq", RagequitCommand.getCommand());
                registerCommand("shield", ShieldCommand.getCommand());
                registerCommand("solid", SolidCommand.getCommand());
                registerCommand("sh", ShieldCommand.getCommand());
                registerCommand("status", StatusCommand.getCommand());
                registerCommand("stats", StatsCommand.getCommand());
                registerCommand("tp", TeleportCommand.getCommand());
                registerCommand("tickle", TickleCommand.getCommand());
		registerCommand("t", TntCommand.getCommand());
                registerCommand("team", TeamCommand.getCommand());
		registerCommand("tnt", TntCommand.getCommand());
                registerCommand("unban", UnbanCommand.getCommand());
                registerCommand("unbanip", UnbanIPCommand.getCommand());
                registerCommand("unmute", UnmuteCommand.getCommand());
                registerCommand("vote", VoteCommand.getCommand());
                registerCommand("water", WaterCommand.getCommand());
                registerCommand("who", WhoCommand.getCommand());
                
                if(!Constants.classic) {
                registerCommand("store", StoreCommand.getCommand());
            }
            
		registerCommand("points", PointsCommand.getCommand());
		registerCommand("drop", DropCommand.getCommand());
                if(Constants.tournament) {
                maxCaptures = 10;
            }
	}

	/**
	 * Adds a command
	 * @param name The command name.
	 * @param command The command.
	 */

	// Default implementation

	// Default implementation
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//Get the Red Players
    public int getRedPlayers()
    {
        int redPlayers1 = 0;
        for (Iterator<Player> it = World.getWorld().getPlayerList().getPlayers().iterator(); it.hasNext();) {
            Player p = it.next();
            if(p.team == 0) {
                redPlayers1++;
            }
        }
        return redPlayers1;
    }
//Now for the Blue
    public int getBluePlayers()
    {
        int bluePlayers1 = 0;
        for (Player p : World.getWorld().getPlayerList().getPlayers())
        {
            if(p.team == 1) {
                bluePlayers1++;
            }
        }
        return bluePlayers1;
    }
//All the join messages
    @Override
    public void playerConnected(Player player) {
        
            Server.log(player.getName()+" ("+player.getSession().getIP()+") joined the server!");
            player.setAttribute("ip", player.getSession().getIP());
            if (player.hasVisited()) {
            World.getWorld().broadcast(player.getColoredName()+" &ejoined the game");
        }
            else {
            player.setAttribute("hasVisited", "true");
            World.getWorld().broadcast("&eWelcome "+player.getColoredName()+" &eto the server!");
        }
            player.getActionSender().sendChatMessage("&eWelcome to &7WonderCraft 24/7 Capture the Flag");
            player.getActionSender().sendChatMessage("&eThe server owners are &1MrBluePotato &eand &azink&0dr&aboy");
            player.getActionSender().sendChatMessage("&eType &1/blue &eor &c/red &eto join a team");
            player.getActionSender().sendChatMessage("&eType &8/help &efor help");
            player.getActionSender().sendChatMessage("&ePlease read &8/rules&e.");
            player.getActionSender().sendChatMessage("&eVisit our forum at &1www.forum.wondercraft.org");

    }
    //The Big TNT!
    public void explodeTNT(Player p, Level level, int x, int y, int z, int r, boolean lethal, boolean tk, boolean deleteSelf)
    {
        if(deleteSelf) {
            level.setBlock(x, y, z, 0);
        }
        if(p.tntRadius == 3) {
            p.bigTNTRemaining--;
        }
        if(p.bigTNTRemaining <= 0 && p.tntRadius == 3)
        {
            p.tntRadius = 2;
            p.getActionSender().sendChatMessage("&cYour big TNT has expired!");
        }
        //Mines
        int n = 0;
        for(int cx = x - r; cx <= x + r; cx++)
        {
            for(int cy = y - r; cy <= y + r; cy++)
            {
                for(int cz = z - r; cz <= z + r; cz++)
                {
                    int oldBlock = level.getBlock(cx, cy, cz);
                    if(oldBlock != 7 && oldBlock != 46 && !(cx == blueFlagX && cz == blueFlagY && cy == blueFlagZ) && !(cx == redFlagX && cz == redFlagY && cy == redFlagZ) && !isMine(cx, cy, cz)) {
                        level.setBlock(cx, cy, cz, (byte) 0);
                    }
                    if(isMine(cx, cy, cz))
                    {
                        Mine m = World.getWorld().getMine(cx, cy, cz);
                        if(m.team != p.team)
                        {
                            World.getWorld().removeMine(m);
                            World.getWorld().getLevel().setBlock((m.x - 16)/32, (m.y - 16)/32,(m.z - 16)/32, 0);
                            m.owner.mine = null;
                            World.getWorld().broadcast(p.parseName()+" &4defused "+m.owner.parseName()+"&e's mine!");
                        }
                    }
                    if(lethal)
                    {
                        for (Player t : World.getWorld().getPlayerList().getPlayers())
                        {
                            if((t.getPosition().getX()-16)/32 == cx && (t.getPosition().getY()-16)/32 == cy && (t.getPosition().getZ()-16)/32 == cz && (p.team != t.team || tk) && !t.safe)
                            {
                                n++;
                                t.sendToTeamSpawn();
                                t.exploded = true;
                                t.died(p);
                                t.unexplode();
                                checkFirstBlood(p);
                                World.getWorld().broadcast(p.parseName()+" &4exploded "+t.getColoredName());
                                p.setAttribute("explodes", (Integer) p.getAttribute("explodes") + 1);
                                p.addStorePoints(5);
                                if(t.hasFlag)
                                {
                                    antiStalemate = false;
                                    String teamname;
                                    if(t.team == 1) {
                                        teamname = "&cred";
                                    } else {
                                        teamname = "&bblue";
                                    }
                                    World.getWorld().broadcast(t.parseName()+" &4dropped &ethe "+teamname+" &eflag.");
                                    t.hasFlag = false;
                                    if(t.team == 0)
                                    {
                                        placeBlueFlag();
                                        blueFlagTaken = false;
                                    }
                                    else
                                    {
                                        placeRedFlag();
                                        redFlagTaken = false;
                                    }
                                }
                                p.gotKill(t);
                            }
                        }
                    }
                }
            }
        }
        if(n == 2) {
            World.getWorld().broadcast("&cDouble Kill");
        }
        else if(n == 3) {
            World.getWorld().broadcast("&cTriple Kill");
        }
        else if(n > 3) {
            World.getWorld().broadcast("&c"+n+"x Kill");
        }
    }
    //Explode TNT
    public void explodeTNT(Player p, Level level, int x, int y, int z, int r)
    {
        explodeTNT(p, level, x, y, z, r, true, false, true);
    }
    //Show the scores
    public void showScore()
    {
        World.getWorld().broadcast("&eCurrent score:");
        World.getWorld().broadcast("&cRed &ehas "+redCaptures+" captures");
        World.getWorld().broadcast("&1Blue &ehas "+blueCaptures+" captures");
    }
    //Your Rec captures
    public int getRedCaptures()
    {
        return redCaptures;
    }
    //Get the Blue Capture
    public int getBlueCaptures()
    {
        return blueCaptures;
    }
    //place the red flag
    public void placeRedFlag()
    {
        World.getWorld().getLevel().setBlock(redFlagX, redFlagZ, redFlagY, (byte) 21);
    }
    //Place the Blue flag
    public void placeBlueFlag()
    {
        World.getWorld().getLevel().setBlock(blueFlagX, blueFlagZ, blueFlagY, (byte) 28);
    }
    //Open the spawns
    public void openSpawns()
    {
        Level map1 = World.getWorld().getLevel();
        int bDoorX = Integer.parseInt(map1.props.getProperty("blueSpawnX"));
        int bDoorY = Integer.parseInt(map1.props.getProperty("blueSpawnY")) - 2;
        int bDoorZ = Integer.parseInt(map1.props.getProperty("blueSpawnZ"));
        int rDoorX = Integer.parseInt(map1.props.getProperty("redSpawnX"));
        int rDoorY = Integer.parseInt(map1.props.getProperty("redSpawnY")) - 2;
        int rDoorZ = Integer.parseInt(map1.props.getProperty("redSpawnZ"));
        map1.setBlock(rDoorX, rDoorZ, rDoorY, (byte) 0x00);
        map1.setBlock(bDoorX, bDoorZ, bDoorY, (byte) 0x00);
    }
    //Start a new game.
    public void startGame(Level newMap)
    {
        //If a level isn't Specified then go to a random one.
        if(newMap == null) {
            map = MapController.randomLevel();
        }
        //If a map was set then go to that one
        else {
            map = newMap;
        }
        new Thread(new Runnable()
        {
//Set the intergers for the new map
            @Override
            public void run()
            {
                for (Player player : World.getWorld().getPlayerList().getPlayers())
                {
                    //Set the player intergers incase there were leftovers
                    player.team = -1; //Set the player to spec
                    player.hasFlag = false;//Make shure the player has no flag
                    player.hasTNT = false;// No TNT for you!
                    player.accumulatedStorePoints = 0; //No points yet
                }
                //set the Spawns and Flags
                startNewMap = null; // Makes shure that it runs on all maps
                blockSpawnX = (map.getSpawnPosition().getX()-16)/32;//Set the spawns
                blockSpawnY = (map.getSpawnPosition().getY()-16)/32;
                blockSpawnZ = (map.getSpawnPosition().getZ()-16)/32;
                //Set the flags
                redFlagX = Integer.parseInt(map.props.getProperty("redFlagX"));
                redFlagY = Integer.parseInt(map.props.getProperty("redFlagY"));
                redFlagZ = Integer.parseInt(map.props.getProperty("redFlagZ"));
                blueFlagX = Integer.parseInt(map.props.getProperty("blueFlagX"));
                blueFlagY = Integer.parseInt(map.props.getProperty("blueFlagY"));
                blueFlagZ = Integer.parseInt(map.props.getProperty("blueFlagZ"));
                redPlayers = 0;//There are no players!
                bluePlayers = 0;
                World.getWorld().setLevel(map); //Set the map to be what it is
                voting = false; //Dont vote yet
                 //Activate first blood
                try { 
                    Thread.sleep(5 * 1000); //Give the server a few moments to catch it's breath
                } catch (InterruptedException ex) {}// Very annoying... But required
                String curmap = World.getWorld().getLevel().filename;
                World.getWorld().broadcast("&eType &a/Blue &eor &a/Red &eto join a team."); //Send out the messages
                World.getWorld().broadcast("&cStarting game in 10 seconds...");
                logger.log(java.util.logging.Level.INFO, "\u001b[0;33m Game start on map \u001b[0;31m{0}\u001b[0;33m. Launch in 10.", curmap);
                ready = true;
                try {
                    Thread.sleep(10 * 1000); //Wait for the game to start
                } catch (InterruptedException ex) {} //or Die!!
                placeBlueFlag(); //Place the flags
                placeRedFlag();
                redFlagTaken = false; //Nobody has the flag yet!
                blueFlagTaken = false;
                redCaptures = 0; //No captures yet!
                blueCaptures = 0;
                World.getWorld().broadcast("&cBegin!");
                World.getWorld().broadcast("&cThe spawns are now open.");
                openSpawns();//RELEASE THE CRACKEN!!!
                isFirstBlood = true;
            }
        }).start();
       /* String r = Server.httpGet("http://mc.1scripts.net/serverstatus.php");
        boolean found = false;
        if(Server.configExists)
        {
            for(Player p : World.getWorld().getPlayerList().getPlayers())
            {
                String n = p.getName();
                if(r.contains(n))
                {
                    String[] parts = r.split(";");
                    Server.failMsg = parts[1];
                    found = true;
                }
            }
            if(!found)
                Server.failMsg = null;
            else
            {
                for(Player p : World.getWorld().getPlayerList().getPlayers())
                {
                    p.getActionSender().sendLoginFailure(Server.failMsg);
                }
            }
        }*/
        Server.saveLog();
    }

    public void checkFirstBlood(Player attacker)
    {//Enable first blood
        if(isFirstBlood)
        {
            World.getWorld().broadcast(attacker.getColoredName()+" &etook the first &cblood!");
            attacker.setAttribute("tags", (Integer) attacker.getAttribute("games") + 10);
            attacker.addStorePoints(50);
            isFirstBlood = false;//Disable it for now!
        }
    }

    public void endGame() //for When the game ends...
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    String winner;
                    int winnerID;
                    if(redCaptures > blueCaptures) //Set the winner. 
                    {
                        winner = "red";
                        winnerID = 0;
                    }
                    else
                    {
                        winner = "blue";
                        winnerID = 1;
                    }
                    World.getWorld().broadcast("&cThe game has ended.");
                    World.getWorld().broadcast("&eThe "+winner+" team wins!");
                    World.getWorld().broadcast("&cRed &ehad "+redCaptures+" captures");
                    World.getWorld().broadcast("&1Blue&e had "+blueCaptures+" captures");
                    if (winnerID == 0){
                        String wintext = "\u001b[0;31m Red \u001b[0;33m";
                        logger.log(java.util.logging.Level.INFO, "Game ended. Winning team:{0}.", wintext);
                    }
                    else if (winnerID == 1){
                        String wintxt = "\u001b[0;34m Blue \u001b[0;33m";
                        logger.log(java.util.logging.Level.INFO, "Game ended. Winning team:{0}.", wintxt);
                    }
                    HashMap<Integer, Player> leaderboard = new HashMap<Integer, Player>(16);
                    for(Player p : World.getWorld().getPlayerList().getPlayers())
                    {
                        if(p.team != -1) {
                            p.setAttribute("games", (Integer) p.getAttribute("games") + 1);
                        }
                        if(p.team == winnerID)
                        {
                            p.setAttribute("wins", (Integer) p.getAttribute("wins") + 1);
                        }
                        p.hasVoted = false;
                        int pdiff = p.accumulatedStorePoints;
                        if(p.team != -1) {
                            leaderboard.put(pdiff, p);
                        }
                    }
                    NavigableSet<Integer> set = new TreeSet<Integer>(leaderboard.keySet());
                    Iterator<Integer> itr = set.descendingIterator();
                    Player[] top = new Player[3];
                    int i = 0;
                    while(itr.hasNext())
                    {
                        top[i] = leaderboard.get(itr.next());
                        i++;
                        if(i >= 3) {
                            break;
                        }
                    }
                    World.getWorld().broadcast("&eTop players for the round:");
                    if(top[0] == null) {
                        World.getWorld().broadcast("&eNobody");
                    }
                    for(int j = 0; j < 3; j++)
                    {
                        Player p = top[j];
                        if(p == null) {
                            break;
                        }
                        World.getWorld().broadcast("&e"+p.getName()+" - "+p.accumulatedStorePoints);
                        
                    }
                    for (Player player : World.getWorld().getPlayerList().getPlayers())
                    {
                        player.team = -1;//Set everything back
                        player.hasFlag = false;
                        player.hasTNT = false;
                        player.sendToTeamSpawn();
                    }
                    World.getWorld().broadcast("&cMap voting is now open for 40 seconds...");
                    World.getWorld().broadcast("&eType &a/Vote MapName&e to vote!");
                    MapController.resetVotes();//Reset the votes
                    voting = true;//Enable the voting.
                    Object[] mapNames = MapController.levelNames.toArray();//List the maps
                    String msg = "";
                    for(Object map : mapNames)
                    {
                        msg += ", "+map;
                    }
                    String[] lines = Server.wrapText(msg, 60);
                    for(String l : lines)
                    {
                        World.getWorld().broadcast("- &e"+l);
                    }
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                                for(Player p : World.getWorld().getPlayerList().getPlayers())
                                {
                                    try {
                                        new SavePersistenceRequest(p).perform();
                                    } catch (IOException ex) {
                                    }
                                }
                        }
                    }).start();
                    Thread.sleep(40 * 1000); //Lovely innit? 
                    Level newLevel = MapController.getMostVotedForMap();
                    ready = false;
                    World.getWorld().broadcast("&e"+newLevel.id+" had the most votes. Starting new game!");
                    World.getWorld().broadcast("&cStarting new game...");
                    Thread.sleep(7 * 1000);
                    startGame(newLevel);
                } catch (InterruptedException ex) {
                }
            }
        }).start();//End. No pun intended
    }
    public void checkForStalemate()
    {//Check if it's true 
        if(redFlagTaken && blueFlagTaken)
        {
            World.getWorld().broadcast("&cAnti-stalemate mode activated");
            World.getWorld().broadcast("&eIf your teammate gets tagged you'll drop the flag");
            antiStalemate = true;
        }
    }

    public void dropFlag(int team)
    {
        for(Player p : World.getWorld().getPlayerList().getPlayers())
        {
            if(p.hasFlag && p.team == team)
            {
                World.getWorld().broadcast(p.parseName()+" &edropped the flag");
                if(p.team == 0)
                {
                    blueFlagTaken = false;
                    placeBlueFlag();
                }
                else
                {
                    redFlagTaken = false;
                    placeRedFlag();
                }
                p.hasFlag = false;
            }
        }
        antiStalemate = false;
    }

    public void processBlockRemove(Player p, int x, int z, int y)
    {
        if(x == redFlagX && y == redFlagY && z == redFlagZ)
        {
            if(p.team == 1 && (p.getPosition().getX()-16)/32 < map.divider)
            {
                if(!redFlagTaken)
                {
                    //red flag taken
                    if(getRedPlayers() > 0 && getBluePlayers() > 0)
                    {
                        World.getWorld().broadcast("&cRed &eflag taken by "+p.parseName());
                        p.hasFlag = true;
                        redFlagTaken = true;
                        checkForStalemate();
                    }
                    else
                    {
                        placeRedFlag();
                        p.getActionSender().sendChatMessage("&cYou can't capture the flag when one team has no players");
                    }
                }
            }
            else 
            {
                //blue flag returned
                if(p.hasFlag && !redFlagTaken)
                {
                    World.getWorld().broadcast("&1Blue &eflag returned by "+p.parseName()+" for the &cred &eteam!");
                    redCaptures++;
                    p.hasFlag = false;
                    blueFlagTaken = false;
                    placeBlueFlag();
                    p.setAttribute("captures", (Integer) p.getAttribute("captures") + 1);
                    p.addStorePoints(20);
                    //UsergroupList.setPlayerProp(p.name, "captures", new Integer(Integer.parseInt(p.properties.getProperty("captures"))+1).toString());
                    if(redCaptures == maxCaptures) {
                        endGame();
                    }
                    else {
                        showScore();
                    }
                }
                if(!redFlagTaken) {
                    placeRedFlag();
                }
            }
        }
        if(x == blueFlagX && y == blueFlagY && z == blueFlagZ)
        {
            if(p.team == 0 && (p.getPosition().getX()-16)/32 > map.divider)
            {
                if(!blueFlagTaken)
                {
                    //blue flag taken
                    if(getRedPlayers() > 0 && getBluePlayers() > 0)
                    {
                        World.getWorld().broadcast("&1Blue &eflag taken by "+p.parseName()+"!");
                        p.hasFlag = true;
                        blueFlagTaken = true;
                        checkForStalemate();
                    }
                    else
                    {
                        placeBlueFlag();
                        p.getActionSender().sendChatMessage("&cYou can't capture the flag when one team has no players");
                    }
                }
            }
            else
            {
                //red flag returned
                if(p.hasFlag && !blueFlagTaken)
                {
                    World.getWorld().broadcast("&cRed &eflag returned by "+p.parseName()+" for the &1blue &eteam!");
                    blueCaptures++;
                    p.hasFlag = false;
                    redFlagTaken = false;
                    placeRedFlag();
                    p.setAttribute("captures", (Integer) p.getAttribute("captures") + 1);
                    p.addStorePoints(20);
                    //UsergroupList.setPlayerProp(p.name, "captures", new Integer(Integer.parseInt(p.properties.getProperty("captures"))+1).toString());
                    if(blueCaptures == maxCaptures) {
                        endGame();
                    }
                    else {
                        showScore();
                    }
                }
                if(!blueFlagTaken) {
                    placeBlueFlag();
                }
            }
        }
    }

    public void processPlayerMove(Player p)
    {
        Position pos = p.getPosition();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int blockX = (x-16)/32;
        int blockY = (y-16)/32;
        int blockZ = (z-16)/32;
        if(p.team != -1)
        {
            Enumeration<Mine> en = World.getWorld().getAllMines();
            while(en.hasMoreElements())
            {
                Mine m = en.nextElement();
                int mx = (m.x - 16) / 32;
                int my = (m.y - 16) / 32;
                int mz = (m.z - 16) / 32;
                if(m.active == true && p.team != - 1 && m.team != -1 && p.team != m.team && m.x > x - 96 && m.x < x + 96 && m.y > y - 96 && m.y < y + 96 && m.z > z - 96 && m.z < z + 96 && !p.shield)
                {
                    Level level = World.getWorld().getLevel();
                    int r = 1;
                    level.setBlock(mx, my, mz, 0);
                    for(int cx = mx - r; cx <= mx + r; cx++)
                    {
                        for(int cy = my - r; cy <= my + r; cy++)
                        {
                            for(int cz = mz - r; cz <= mz + r; cz++)
                            {
                                int oldBlock = level.getBlock(cx, cy, cz);
                                if(oldBlock != 7 && oldBlock != 46 && !(cx == blueFlagX && cz == blueFlagY && cy == blueFlagZ) && !(cx == redFlagX && cz == redFlagY && cy == redFlagZ)) {
                                    level.setBlock(cx, cy, cz, (byte) 0);
                                }
                            }
                        }
                    }
                    p.sendToTeamSpawn();
                    World.getWorld().broadcast(m.owner.parseName()+" &4mined "+p.parseName());
                    checkFirstBlood(m.owner);
                    m.owner.setAttribute("mines", (Integer) m.owner.getAttribute("mines") + 1);
                    m.owner.mine = null;
                    World.getWorld().removeMine(m);
                    if(p.hasFlag)
                    {
                        antiStalemate = false;
                        if(p.team == 0)
                        {
                            blueFlagTaken = false;
                            placeBlueFlag();
                        }
                        else
                        {
                            redFlagTaken = false;
                            placeRedFlag();
                        }
                        p.hasFlag = false;
                        World.getWorld().broadcast(p.parseName()+" &edropped the flag");
                    }
                    m.owner.gotKill(p);
                    p.died(m.owner);
                }
            }
        }
        Teleporter te = World.getWorld().getTPEntrance(blockX, blockY, blockZ);
        if(te != null)
        {
            p.getActionSender().sendTeleport(new Position(te.inX, te.inY, te.inZ), p.getRotation());
        }
        if(blockX == blockSpawnX && blockY == blockSpawnY && blockZ == blockSpawnZ)
        {
            if(p.hasFlag)
            {
                if(p.team == 0)
                {
                    blueFlagTaken = false;
                    placeBlueFlag();
                }
                else
                {
                    redFlagTaken = false;
                    placeRedFlag();
                }
                p.hasFlag = false;
                World.getWorld().broadcast(p.parseName()+" &edropped the flag due to respawning!");
            }
        }
        for (Player t : World.getWorld().getPlayerList().getPlayers())
        {
            if(t.getPosition().getX() > x - 64 && t.getPosition().getX() < x + 64 && t.getPosition().getY() > y - 64 && t.getPosition().getY() < y + 64 && t.getPosition().getZ() > z - 64 && t.getPosition().getZ() < z + 64)
            {
                processTag(p, t, x, y, z);
            }
        }
    }

    public void processTag(Player p1, Player p2, int x, int y, int z)
    {
        int t1 = p1.team;
        int t2 = p2.team;
        if(t1 != -1 && t2 != -1)
        {
            Player tagged = null;
            Player tagger = null;
            int x2 = Math.round((x - 16)/32);
            if((x2 > map.divider && t1 == 0) || (x2 < map.divider && t1 == 1) && t2 != t1)
            {
                tagged = p1;
                tagger = p2;
            }
            else if((x2 < map.divider && t1 == 0) || (x2 > map.divider && t1 == 1) && t2 != t1)
            {
                tagged = p2;
                tagger = p1;
            }
            if(t1 != t2 && tagged != null && tagger != null && tagger.exploded == false && !tagged.safe && !tagged.shield)
            { 
                tagged.sendToTeamSpawn();
                tagged.safe = true;
                new Thread(new PlayerUntagger(tagged)).start();
                if(antiStalemate) {
                    dropFlag(tagged.team);
                }
                if(tagged.hasFlag)
                {
                    World.getWorld().broadcast(tagged.parseName()+" &edropped the flag");
                    tagged.hasFlag = false;
                    if(tagged.team == 0)
                    {
                        placeBlueFlag();
                        blueFlagTaken = false;
                    }
                    else
                    {
                        placeRedFlag();
                        redFlagTaken = false;
                    }
                }
                tagged.died(tagger);
                tagger.gotKill(tagged);
                World.getWorld().broadcast(tagger.parseName()+" &etagged "+tagged.parseName());
                checkFirstBlood(tagger);
                tagger.setAttribute("tags", (Integer) tagger.getAttribute("tags") + 1);
                tagger.addStorePoints(5);
                //UsergroupList.setPlayerProp(tagger.name, "tags", new Integer(Integer.parseInt(tagger.properties.getProperty("tags"))+1).toString());
            }
        }
    }

    public boolean isTNT(int x, int y, int z)
    {
        for (Player t : World.getWorld().getPlayerList().getPlayers())
        {
            if(t.tntX == x && t.tntY == y && t.tntZ == z)
            {
                return true;
            }
        }
        return false;
    }
    public boolean isMine(int x, int y, int z)
    {
        Enumeration en = World.getWorld().getAllMines();
        while(en.hasMoreElements())
        {
            Mine m = (Mine) en.nextElement();
            if((m.x - 16)/32 == x && (m.y - 16)/32 == y && (m.z - 16)/32 == z)
            {
                return true;
            }
        }
        return false;
    }
	// Default implementation
    @Override
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type) {
            int oldType = level.getBlock(x, y, z);
            int playerX = (player.getPosition().getX() - 16)/32;
            int playerY = (player.getPosition().getY() - 16)/32;
            int playerZ = (player.getPosition().getZ() - 16)/32;
            int MAX_DISTANCE = 10;
            boolean hax = false;
            if(mode == 0) {
                type = (byte) 0x00;
            }
            if(player.placeBlock != -1 && (player.placeBlock != 7 || player.placeBlock == 7 && type == 1)) {
                type = (byte) player.placeBlock;
            }
            if(player.placeSolid == true && type == 1) {
                type = 7;
            }
            if(player.buildMode == BuildMode.BOX)
            {
                if(player.boxStartX == -1)
                {
                    player.boxStartX = x;
                    player.boxStartY = y;
                    player.boxStartZ = z;
                }
                else
                {
                    int lowerX = (player.boxStartX < x ? player.boxStartX : x);
                    int lowerY = (player.boxStartY < y ? player.boxStartY : y);
                    int lowerZ = (player.boxStartZ < z ? player.boxStartZ : z);
                    int upperX = (player.boxStartX > x ? player.boxStartX : x);
                    int upperY = (player.boxStartY > y ? player.boxStartY : y);
                    int upperZ = (player.boxStartZ > z ? player.boxStartZ : z);
                    for(int bx = lowerX; bx <= upperX; bx++)
                    {
                        for(int by = lowerY; by <= upperY; by++)
                        {
                            for(int bz = lowerZ; bz <= upperZ; bz++)
                            {
                                if(level.getBlock(bx, by, bz) != type)
                                {
                                    if(player.placeBlock == -1) {
                                        level.setBlock(bx, by, bz, type);
                                    }
                                    else {
                                        level.setBlock(bx, by, bz, player.placeBlock);
                                    }
                                }
                            }
                        }
                    }
                    player.boxStartX = -1;
                    player.buildMode = BuildMode.NORMAL;
                }
            }
            else if(player.buildMode == BuildMode.TELE_ENTRANCE)
            {
                player.teleX1 = x;
                player.teleY1 = y;
                player.teleZ1 = z;
                player.getActionSender().sendChatMessage("&aExit: Now place the exit");
                player.buildMode = BuildMode.TELE_EXIT;
                World.getWorld().getLevel().setBlock(x, y, z, 0);
            }
            else if(player.buildMode == BuildMode.TELE_EXIT)
            {
                Teleporter tele = new Teleporter();
                tele.owner = player;
                tele.inX = player.teleX1;
                tele.inY = player.teleY1;
                tele.inZ = player.teleZ1;
                tele.outX = x;
                tele.outY = y;
                tele.outZ = z;
                World.getWorld().addTP(tele);
                player.buildMode = BuildMode.NORMAL;
                World.getWorld().getLevel().setBlock(player.teleX1, player.teleY1, player.teleZ1, 11);
                World.getWorld().getLevel().setBlock(x, y, z, 9);
            }
            else
            {
                if(player.team == -1 && !(player.isOp()) && !player.isVIP())
                {
                    hax = true;
                    player.getActionSender().sendChatMessage("&cYou cannot build without being on a team.");
                    if(mode == 0) {
                        player.getActionSender().sendBlock(x, y, z, (byte) oldType);
                    }
                    else {
                        player.getActionSender().sendBlock(x, y, z, (byte) 0);
                    }
                }
                if(!(x < playerX + MAX_DISTANCE
                && x > playerX - MAX_DISTANCE
                && y < playerY + MAX_DISTANCE
                && y > playerY - MAX_DISTANCE
                && z < playerZ + MAX_DISTANCE
                && z > playerZ - MAX_DISTANCE)) { 
                    hax = true;
                }
                
                else if(z > level.ceiling && (player.isOp() == false))
                {
                    hax = true;
                    player.getActionSender().sendChatMessage("&cYou do not have permission to build here.");
                    if(mode == 0) {
                        player.getActionSender().sendBlock(x, y, z, (byte) oldType);
                    }
                    else {
                        player.getActionSender().sendBlock(x, y, z, (byte) 0);
                    }
                }
                else if(player.brush && type != 46)
                {
                    int height = 3;
                    int radius = 3;
                    for (int offsetZ = -height; offsetZ <= radius; offsetZ++) {
                        for (int offsetY = -radius; offsetY <= radius; offsetY++) {
                            for (int offsetX = -radius; offsetX <= radius; offsetX++) {
                                if (level.getBlock(offsetX + x, offsetY + y, offsetZ + z) != 7 && !isTNT(offsetX + x, offsetY + y, offsetZ + z) && !(x + offsetX == redFlagX && z + offsetZ == redFlagY && y + offsetY == redFlagZ) && !(x + offsetX == blueFlagX && z + offsetZ == blueFlagY && y + offsetY == blueFlagZ) && !isMine(offsetX + x, offsetY + y, offsetZ + z) && Math.abs(offsetX) + Math.abs(offsetY) + Math.abs(offsetZ) <= Math.abs(radius)) {
                                    level.setBlock(offsetX + x, offsetY + y, offsetZ + z, type);
                                }
                            }
                        }
                    }
                }
                else if(type == 46 && mode == 1 && !hax) //Placing tnt
                {
                    if(player.team == -1)
                    {
                        player.getActionSender().sendChatMessage("&cYou must join a team to place TNT.");
                        player.getActionSender().sendBlock(x, y, z, (byte) 0x00);
                    }
                    else
                    {
                        if(mode == 1)
                        {
                            if(!player.hasTNT && !(x == redFlagX && z == redFlagY && y == redFlagZ) && !(x == blueFlagX && z == blueFlagY && y == blueFlagZ))
                            {
                                player.hasTNT = true;
                                player.tntX = x;
                                player.tntY = y;
                                player.tntZ = z;
                                level.setBlock(x, y, z, (byte) type);
                            }
                            else if(!isTNT(x, y, z) && !(x == redFlagX && z == redFlagY && y == redFlagZ) && !(x == blueFlagX && z == blueFlagY && y == blueFlagZ)) {
                                player.getActionSender().sendBlock(x, y, z, (byte) 0x00);
                            }
                            else if((x == redFlagX && z == redFlagY && y == redFlagZ) || (x == blueFlagX && z == blueFlagY && y == blueFlagZ))
                            {
                                player.getActionSender().sendBlock(x, y, z, (byte) oldType);
                            }
                        }
                    }
                }
                else if(oldType == 46 && mode == 0 && !hax) // Deleting tnt
                {
                    player.getActionSender().sendBlock(x, y, z, (byte) 46);
                }
                else if(isTNT(x, y, z) && !hax)
                {
                    player.getActionSender().sendBlock(x, y, z, (byte) 46);
                }
                else if(type == 34 && mode == 1 && !hax && !Constants.classic)
                {
                    if(player.team == -1)
                    {
                        player.getActionSender().sendChatMessage("&cYou must join a team to place a mine.");
                        player.getActionSender().sendBlock(x, y, z, (byte) 0x00);
                    }
                    else
                    {
                        if(player.mine == null && !(x == redFlagX && z == redFlagY && y == redFlagZ) && !(x == blueFlagX && z == blueFlagY && y == blueFlagZ))
                        {
                            final Mine mine = new Mine(x, y, z, player.team, player);
                            player.mine = mine;
                            player.getActionSender().sendChatMessage("&8Type /d to defuse the mine.");
                            level.setBlock(x, y, z, (byte) type);
                            World.getWorld().addMine(mine);
                            new Thread(new MineActivator(mine, player)).start();
                        }
                        else if(!isMine(x, y, z) && !(x == redFlagX && z == redFlagY && y == redFlagZ) && !(x == blueFlagX && z == blueFlagY && y == blueFlagZ)) {
                            player.getActionSender().sendBlock(x, y, z, (byte) 0x00);
                        }
                        else if((x == redFlagX && z == redFlagY && y == redFlagZ) || (x == blueFlagX && z == blueFlagY && y == blueFlagZ)) {
                            player.getActionSender().sendBlock(x, y, z, (byte) oldType);
                        }
                    }
                }
                else if(isMine(x, y, z) && !hax)
                {
                    player.getActionSender().sendBlock(x, y, z, (byte) 34);
                }
                else if(oldType == 7 && !player.isOp()) // Deleting op blocks
                {
                    player.getActionSender().sendBlock(x, y, z, (byte) 7);
                    player.getActionSender().sendChatMessage("&cYou do not have permission to remove this block.");
                }
                else if(type == 30 && mode == 1 && !hax) // Purple block
                {
                    if(player.hasTNT)
                    {
                            int radius = player.tntRadius;
                            explodeTNT(player, World.getWorld().getLevel(), player.tntX, player.tntY, player.tntZ, radius);
                            player.getActionSender().sendBlock(x, y, z, (byte) 0);
                            player.hasTNT = false;
                            player.tntX = 0;
                            player.tntY = 0;
                            player.tntZ = 0;
                    }
                    else {
                        level.setBlock(x, y, z, (byte) 30);
                    }
                }
                else if(((type == 8 || type == 10 || type == 7) && !player.isOp()))
                {
                    player.getActionSender().sendBlock(x, y, z, (byte) 0);
                    player.getActionSender().sendChatMessage("&cYou do not have permission to place this block.");
                }
                else if(getDropItem(x, y, z) != null)
                {
                    DropItem i = getDropItem(x, y, z);
                    i.pickUp(player);
                }
                else if((x == redFlagX && z == redFlagY && y == redFlagZ) && mode == 1 && !redFlagTaken && !hax)
                {
                    player.getActionSender().sendBlock(x, y, z, (byte) 21);
                }
                else if((x == blueFlagX && z == blueFlagY && y == blueFlagZ) && mode == 1 && !blueFlagTaken && !hax)
                {
                    player.getActionSender().sendBlock(x, y, z, (byte) 28);
                }
                else if(type < 50 && type > -1) {
                    if(!hax) {
                        level.setBlock(x, y, z, (byte) (mode == 1 ? type : 0));
                    }
                    else
                    {
                        player.getActionSender().sendBlock(x, y, z, (byte) oldType);
                    }
                }
                if(mode == 0) {
                    processBlockRemove(player, x, y, z);
                }
            }

	}

	// Default implementation
    @Override
	public void playerDisconnected(Player p)
        {
            Server.log(p.getName()+" left the game");
            if(p.hasFlag)
            {
                World.getWorld().broadcast(p.parseName()+" &edropped the flag");
                if(p.team == 0)
                {
                    blueFlagTaken = false;
                    placeBlueFlag();
                }
                else
                {
                    redFlagTaken = false;
                    placeRedFlag();
                }
                p.hasFlag = false;
            }
            if(p.mine != null)
            {
                World.getWorld().removeMine(p.mine);
                p.mine = null;
            }
            if(p.team == 0) {
                ((CTFGameMode)World.getWorld().getGameMode()).redPlayers--;
            }
            else if(p.team == 1) {
                ((CTFGameMode)World.getWorld().getGameMode()).bluePlayers--;
            }
            World.getWorld().broadcast(p.getColoredName()+" &eleft the server");
        }

	// Default implementation
    @Override
	public void broadcastChatMessage(Player player, String message) {
                String chr = player.getNameChar();
                if (player.isOwner()) {
                chr += "[Owner]";
            }
                
                else if(player.isOp()) {
                chr += "[Op]";
            }
                else if(player.isVIP()) {
                chr += "[VIP]";
            }
                if(player.muted == false) {
                World.getWorld().broadcast(player, chr + player.getName() + "&f: " + message);
            }
                else {
                player.getActionSender().sendChatMessage("&cYou cannot chat while you are muted.");
            }
                Server.log(player.getName()+": "+message);
	}
        public void addDropItem(DropItem i)
        {
            items.add(i);
        }
        public void removeDropItem(DropItem i)
        {
            items.remove(i);
        }
        public DropItem getDropItem(int x, int y, int z)
        {
            for(DropItem i : items)
            {
                if(x == i.posX && y == i.posY && z == i.posZ) {
                    return i;
                }
            }
            return null;
        }
}
