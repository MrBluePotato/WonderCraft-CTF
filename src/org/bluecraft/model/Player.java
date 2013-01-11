package org.bluecraft.model;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe, S�ren Enevoldsen and Brett Russell.
 * All rights reserved.
 *
 * Distribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Distributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Distributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the OpenCraft nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bluecraft.Server;
import org.bluecraft.game.impl.CTFGameMode;

import org.bluecraft.net.ActionSender;
import org.bluecraft.net.MinecraftSession;

/**
 * Represents a connected player.
 * @author Graham Edgecombe
 */
public class Player extends Entity {
	
	/**
	 * The player's session.
	 */
	private final MinecraftSession session;
	
	/**
	 * The player's name.
	 */
	private final String name;
	
	/**
	 * A map of attributes that can be attached to this player.
	 */
	private final Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * Creates the player.
	 * @param name The player's name.
	 */

        public boolean muted = false;
        public boolean afk = false;

        private Player instance;

        public int team = -1;
        public int killstreak = 0;
        public boolean safe = false;
        
        public boolean hasTNT = false;
        public int tntX;
        public int tntY;
        public int tntZ;

        public Mine mine = null;
        public int tntRadius = 2;

        public boolean hasFlag = false;
        
        public int placeBlock = -1;
        public boolean placeSolid = false;
        public boolean moveFlag = false;
        public boolean teleporting = false;
        public boolean isVisible = true;
        public boolean brush = false;

        public boolean hasVoted = false;

        //STORE STUFF
        public int bigTNTRemaining = 0;
        public boolean hasGhost = false;
        public boolean hasShield = false;
        public boolean shield = false;

        public long lastBlockTimestamp;

        public boolean exploded = false;
        public int boxStartX = -1;
        public int boxStartY = -1;
        public int boxStartZ = -1;
        public int teleX1 = -1;
        public int teleY1 = -1;
        public int teleZ1 = -1;
        public int teleX2 = -1;
        public int teleY2 = -1;
        public int teleZ2 = -1;
        public int buildMode;

        public int accumulatedStorePoints = 0;
        
        public Position teleportPosition;
        
        private Thread followThread;

	public Player(MinecraftSession session, String name) {
		this.session = session;
		this.name = name;
                instance = this;
	}

        public void gotKill(Player defender)
        {
            killstreak++;
            Killstats.kill(this, defender);
            if(killstreak % 5 == 0)
                World.getWorld().broadcast(getColoredName()+" &ehas a killstreak of " +killstreak);
        }
        public void follow(final Player p)
        {
            if(p == null && followThread != null)
                followThread.stop();
            else
            {
                if(followThread != null)
                    followThread.stop();
                followThread = new Thread(new Runnable()
                {
                    public void run() {
                        while(true)
                        {
                            Position pos = p.getPosition();
                            Rotation r = p.getRotation();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                            }
                            getActionSender().sendTeleport(pos, r);
                        }
                    }
                });
                followThread.start();
            }
        }
        public void died(Player attacker)
        {
            if(killstreak >= 10)
                World.getWorld().broadcast(attacker.getColoredName()+" &eended "+getColoredName()+"&e's killstreak of "+killstreak);
            killstreak = 0;
        }

        public String getNameChar()
        {

            if (("".equals(getName())))
                if(team == 0)
                       return "&4";
                   else if(team == 1)
                       return "&1";
                   else
                       return "&d";

            else {
                if(isOp())
                {
                   if(team == 0)
                       return "&4";
                   else if(team == 1)
                       return "&1";
                   else
                        return "&8";
                }
            
                if(isVIP())
                {
                    if(team == 0)
                        return "&c";
                    else if(team == 1)
                       return "&9";
                   else
                    return "&f";
                }
            
                 else
                 {
                   if(team == 0) {
                         return "&c";
                     }
                  else if(team == 1) {
                         return "&9";
                     }
                   else {
                         return "&7";
                     }
            }
            }
        }

        public boolean isVIP()
        {
            return getAttribute("VIP") != null && getAttribute("VIP").equals("true");
        }
        
        public boolean isOp()
        {
            return (getAttribute("IsOperator") != null && getAttribute("IsOperator").equals("true")) || isOwner();
        }
        
        public boolean isOwner()
        {
            return getAttribute("IsOwner") != null && getAttribute("IsOwner").equals("true");
        }
        public boolean isEv()
        {
            return getAttribute("IsEv") != null && getAttribute("IsEv").equals("true");
        }
        public boolean isChell()
        {
            return getAttribute("IsChell") != null && getAttribute("IsChell").equals("true");
        }
        public boolean hasVisited()
        {
            return getAttribute("hasVisited") != null && getAttribute("hasVisited").equals("true");
        }


        public String parseName()
        {
            return getNameChar()+name+"&e";
        }
        public void makeInvisible()
        {
            for(Player p : World.getWorld().getPlayerList().getPlayers())
            {
                p.getActionSender().sendRemoveEntity(instance);
            }
        }
        public void makeVisible()
        {
            for(Player p : World.getWorld().getPlayerList().getPlayers())
            {
                if(p != instance)
                    p.getActionSender().sendAddEntity(instance);
            }
        }
        public void activateGhost()
        {
            new Thread(new Runnable()
            {
                public void run()
                {
                    makeInvisible();
                    isVisible = false;
                    hasGhost = false;
                    try
                    {
                        getActionSender().sendChatMessage("&eYou activated ghost mode!");
                        Thread.sleep(10 * 1000);
                        getActionSender().sendChatMessage("&eGhost mode expires in 5 seconds!");
                        Thread.sleep(5 * 1000);
                    }
                    catch(InterruptedException ex)
                    {

                    }
                    makeVisible();
                    getActionSender().sendChatMessage("&eYou are no longer a ghost!");
                    isVisible = true;
                }
            }).start();
        }
        public void activateShield()
        {
            new Thread(new Runnable()
            {
                public void run()
                {
                    hasShield = false;
                    shield = true;
                    try
                    {
                        getActionSender().sendChatMessage("&eShield will expire in 25 seconds");
                        Thread.sleep(15 * 1000);
                        getActionSender().sendChatMessage("&eShield mode expires in 5 seconds!");
                        Thread.sleep(5 * 1000);
                    }
                    catch(InterruptedException ex)
                    {

                    }
                    makeVisible();
                    getActionSender().sendChatMessage("&eShield has expired!");
                    shield = false;
                }
            }).start();
        }
        public void joinTeam(String team)
        {
            accumulatedStorePoints = 0;
            Level l = World.getWorld().getLevel();
            CTFGameMode ctf = (CTFGameMode) World.getWorld().getGameMode();
            if(ctf.voting == true)
                return;
            if(this.team == 0)
                ctf.redPlayers--;
            else if(this.team == 1)
                ctf.bluePlayers--;
            int diff = ctf.redPlayers - ctf.bluePlayers;
            boolean unbalanced = false;
            if(diff >= 1 && team.equals("red"))
                unbalanced = true;
            else if(diff <= -1 && team.equals("blue"))
                unbalanced = true;
            for (Player p : World.getWorld().getPlayerList().getPlayers())
            {
                if(p != this)
                {
                    p.getActionSender().sendRemoveEntity(this);
                }
            }
            boolean bad = false;
            if(hasFlag)
            {
                if(this.team == 0)
                {
                    ctf.blueFlagTaken = false;
                    ctf.placeBlueFlag();
                }
                else
                {
                    ctf.redFlagTaken = false;
                    ctf.placeRedFlag();
                }
                hasFlag = false;
                World.getWorld().broadcast(parseName()+" &edropped the flag!");
            }
            if(team.equals("red"))
            {
                if(unbalanced && ctf.redPlayers > ctf.bluePlayers)
                {
                    ctf.bluePlayers++;
                    this.team = 1;
                    team = "blue";
                    getActionSender().sendChatMessage("&ecRed &eteam is full. Force-joining &1Blue");
                }
                else
                {
                    ctf.redPlayers++;
                    this.team = 0;
                }
            }
            else if(team.equals("blue"))
            {
                if(unbalanced && ctf.bluePlayers > ctf.redPlayers)
                {
                    ctf.redPlayers++;
                    this.team = 0;
                    team = "red";
                    this.getActionSender().sendChatMessage("&1Blue team is full. Force-joining &cRed.");
                }
                else
                {
                    ctf.bluePlayers++;
                     this.team = 1;
                }
            }
            else if(team.equals("spec"))
            {
                this.team = -1;
            }
            else
            {
                bad = true;
                getActionSender().sendChatMessage("&cThat is not a team. Please type &a/blue&e or &a/red");
            }
            if(mine != null)
            {
                mine.team = this.team;
            }
            if(isVisible)
            {
                for (Player p : World.getWorld().getPlayerList().getPlayers())
                {
                    if(p != this)
                    {
                        p.getActionSender().sendAddEntity(this);
                    }
                }
            }
            if(!bad)
            {
                World.getWorld().broadcast(parseName()+" &ejoined the "+team+" &eteam");
                getActionSender().sendTeleport(l.getTeamSpawn(team), new Rotation(0, 0));
                setPosition(l.getTeamSpawn(team));
            }
        }

        public void addStorePoints(int n)
        {
            if(getAttribute("storepoints") == null)
            {
                setAttribute("storepoints", 0);
            }
            accumulatedStorePoints += n;
            setAttribute("storepoints", (Integer) getAttribute("storepoints") + n);
        }
        public int getStorePoints()
        {
            return (Integer) getAttribute("storepoints");
        }
        public void subtractStorePoints(int n)
        {
            if(getAttribute("storepoints") == null)
            {
                setAttribute("storepoints", 0);
            }
            setAttribute("storepoints", (Integer) getAttribute("storepoints") - n);
        }
        public void setStorePoints(int n)
        {
            if(getAttribute("storepoints") == null)
            {
                setAttribute("storepoints", 0);
            }
            setAttribute("storepoints", n);
        }
        public void kickForHacking()
        {
            getActionSender().sendLoginFailure("Illegal Argument.");
            getSession().close();
            World.getWorld().broadcast("&e"+getName()+" was kicked for hacking");
        }
        public void sendToTeamSpawn()
        {
            String teamname;
            if(team == 0)
                teamname = "red";
            else
                teamname = "blue";
            getActionSender().sendTeleport(World.getWorld().getLevel().getTeamSpawn(teamname), new Rotation(0, 0));
            safe = true;
            new Thread(new PlayerUntagger(this)).start();
        }
	
	/**
	 * Sets an attribute of this player.
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 * @return The old value of the attribute, or <code>null</code> if there was
	 * no previous attribute with that name.
	 */
	public Object setAttribute(String name, Object value) {
		return attributes.put(name, value);
	}
	
	/**
	 * Gets an attribute.
	 * @param name The name of the attribute.
	 * @return The attribute, or <code>null</code> if there is not an attribute
	 * with that name.
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	/**
	 * Checks if an attribute is set.
	 * @param name The name of the attribute.
	 * @return <code>true</code> if set, <code>false</code> if not.
	 */
	public boolean isAttributeSet(String name) {
		return attributes.containsKey(name);
	}
	
	/**
	 * Removes an attribute.
	 * @param name The name of the attribute.
	 * @return The old value of the attribute, or <code>null</code> if an
	 * attribute with that name did not exist.
	 */
	public Object removeAttribute(String name) {
		return attributes.remove(name);
	}
	
	@Override
	public String getName() {
		return name;
	}

	public String getColoredName() {
		return getNameChar()+name;
	}
	
	/**
	 * Gets the player's session.
	 * @return The session.
	 */
	public MinecraftSession getSession() {
		return session;
	}
	
	/**
	 * Gets this player's action sender.
	 * @return The action sender.
	 */
	public ActionSender getActionSender() {
            if(session != null)
		return session.getActionSender();
            else
                return null;
	}

        public void wrapText(String message)
        {
            String[] lines = Server.wrapText(message, 62);
            for(String l : lines)
            {
                getActionSender().sendChatMessage("&e"+l);
            }
        }

        public void unexplode()
        {
            new Thread(new Runnable()
            {
                public void run()
                {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                }
                    exploded = false;
                }
            }).start();
        }

	/**
	 * Gets the attributes map.
	 * @return The attributes map.
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
}