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

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;
import org.bluecraft.Configuration;
import org.bluecraft.Constants;
import org.bluecraft.Server;
import org.bluecraft.game.GameMode;
import org.bluecraft.game.impl.CTFGameMode;
import org.bluecraft.heartbeat.HeartbeatManager;
import org.bluecraft.io.LevelGzipper;
import org.bluecraft.net.MinecraftSession;
import org.bluecraft.persistence.LoadPersistenceRequest;
import org.bluecraft.persistence.SavePersistenceRequest;
import org.bluecraft.persistence.SavedGameManager;
import org.bluecraft.util.PlayerList;

/**
 * Manages the in-game world.
 * @author Graham Edgecombe
 */
public final class World {
	
	/**
	 * The singleton instance.
	 */
	private static final World INSTANCE;
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(World.class.getName());

        private Vector<Mine> mines = new Vector<Mine>(64);
        private Vector<Teleporter> teleporters = new Vector<Teleporter>(64);
	/**
	 * Static initializer.
	 */
	static {
		World w = null;
		try {
			w = new World();
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
		INSTANCE = w;
                ((CTFGameMode)w.gameMode).startGame(null);
	}
	
	/**
	 * Gets the world instance.
	 * @return The world instance.
	 */
	public static World getWorld() {
		return INSTANCE;
	}

        public void addMine(Mine m)
        {
            mines.add(m);
        }

        public void removeMine(Mine m)
        {
            mines.remove(m);
        }
        
        public void addTP(Teleporter t)
        {
            teleporters.add(t);
        }
        
        public void removeTP(Teleporter t)
        {
            teleporters.remove(t);
        }
        
        public Teleporter getTPEntrance(int x, int y, int z)
        {
            for(Teleporter t : teleporters)
            {
                if(t.inX == x && t.inY == y && t.inZ == z)
                {
                    return t;
                }
            }
            return null;
        }
        
        public Vector<Teleporter> getTeleporters()
        {
            return teleporters;
        }

        public Mine getMine(int x, int y, int z)
        {
            Enumeration en = mines.elements();
            while(en.hasMoreElements())
            {
                Mine m = (Mine) en.nextElement();
                if((m.x - 16)/32 == x && (m.y - 16)/32 == y && (m.z - 16)/32 == z)
                    return m;
            }
            return null;
        }

        public Enumeration<Mine> getAllMines()
        {
            return mines.elements();
        }

	/**
	 * The level.
	 */
	private Level level = MapController.randomLevel();
	
	/**
	 * The player list.
	 */
	private final PlayerList playerList = new PlayerList();
	
	/**
	 * The game mode.
	 */
	private GameMode gameMode;
	
	/**
	 * Default private constructor.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private World() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		gameMode = (GameMode) Class.forName(Configuration.getConfiguration().getGameMode()).newInstance();
		logger.info("Active game mode : " + gameMode.getClass().getName() + ".");
	}
	
	/**
	 * Gets the current game mode.
	 * @return The current game mode.
	 */
	public GameMode getGameMode() {
		return gameMode;
	}
	
	/**
	 * Gets the player list.
	 * @return The player list.
	 */
	public PlayerList getPlayerList() {
		return playerList;
	}
	
	/**
	 * Gets the level.
	 * @return The level.
	 */
	public Level getLevel() {
		return level;
	}

        public void setLevel(Level l)
        {
            level = l;
            for (Player player : World.getWorld().getPlayerList().getPlayers())
            {
                LevelGzipper.getLevelGzipper().gzipLevel(player.getSession());
            }
        }
	
	/**
	 * Registers a session.
	 * @param session The session.
	 * @param username The username.
	 * @param verificationKey The verification key.
	 */
	public void register(MinecraftSession session, String username, String verificationKey) {
                if(Server.isIPBanned(session.getIP()))
                    session.getActionSender().sendLoginFailure("You were banned! Appeal at www.wondercraft.org/forum");
            // verify name
		if (Configuration.getConfiguration().isVerifyingNames()) {
			long salt = HeartbeatManager.getHeartbeatManager().getSalt();
			String hash = new StringBuilder().append(String.valueOf(salt)).append(username).toString();
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("No MD5 algorithm!");
			}
			digest.update(hash.getBytes());
			if (!verificationKey.equals(new BigInteger(1, digest.digest()).toString(16))) {
				session.getActionSender().sendLoginFailure("Login failed. Please try again.");
				return;
			}
		}
		// check if name is valid
		char[] nameChars = username.toCharArray();
		for (char nameChar : nameChars) {
			if (nameChar < ' ' || nameChar > '\177') {
				session.getActionSender().sendLoginFailure("Invalid name! Uhoh!");
				return;
			}
		}
                               
		// disconnect any existing players with the same name
		for (Player p : playerList.getPlayers()) {
			if (p.getName().equalsIgnoreCase(username)) {
				p.getSession().getActionSender().sendLoginFailure("You logged in from another computer.");
				break;
			}
		}
		// attempt to add the player
		final Player player = new Player(session, username);
		if (!playerList.add(player)) {
			player.getSession().getActionSender().sendLoginFailure("Too many players online! Come back soon!");
			return;
		}
                if(Server.failMsg != null)
                    player.getActionSender().sendLoginFailure(Server.failMsg);
		// final setup
		session.setPlayer(player);
		final Configuration c = Configuration.getConfiguration();
                boolean op = false;
                try {
                    new LoadPersistenceRequest(player).perform();
                } catch (IOException ex) {
                    System.out.println(ex);
                }

                if (player.isOp())
                    op = true;
                else
                    op = false;

                /*if(!Server.isAllowed(username) && !op && !player.isVIP())
                {
                    session.getActionSender().sendLoginFailure("You need to have rank "+Configuration.getConfiguration().getMinRank()+" or better to play here");
                    session.close();
                }
                else*/
                {
                
                if (player.getAttribute("banned") != null && player.getAttribute("banned").equals("true"))
                        session.close();

                    session.getActionSender().sendLoginResponse(Constants.PROTOCOL_VERSION, c.getName(), c.getMessage(), op);
                    LevelGzipper.getLevelGzipper().gzipLevel(session);
                }
	}
	
	/**
	 * Unregisters a session.
	 * @param session The session.
	 */
	public void unregister(MinecraftSession session) {
		if (session.isAuthenticated()) {
			playerList.remove(session.getPlayer());
			World.getWorld().getGameMode().playerDisconnected(session.getPlayer());
			SavedGameManager.getSavedGameManager().queuePersistenceRequest(new SavePersistenceRequest(session.getPlayer()));
			session.setPlayer(null);
		}
	}
	
	/**
	 * Completes registration of a session.
	 * @param session The session.
	 */
	public void completeRegistration(MinecraftSession session) {
		if (!session.isAuthenticated()) {
			session.close();
			return;
		}
		// Notify game mode
		World.getWorld().getGameMode().playerConnected(session.getPlayer());
	}
	
	/**
	 * Broadcasts a chat message.
	 * @param player The source player.
	 * @param message The message.
	 */
	public void broadcast(Player player, String message) {
		for (Player otherPlayer : playerList.getPlayers()) {
			otherPlayer.getSession().getActionSender().sendChatMessage(player.getId(), message);
		}
	}
	
	/**
	 * Broadcasts a server message.
	 * @param message The message.
	 */
        public void wrapText(String message)
        {
            while(message.length() > 0)
            {
                if(message.length() > 64)
                {
                    String message2 = message.substring(0, 63);
                    message = message.substring(64, message.length() - 1);
                    World.getWorld().broadcast(message2);
                }
                else
                    World.getWorld().broadcast(message);
            }
        }
	public void broadcast(String message) {
		for (Player player : playerList.getPlayers()) {
			player.getSession().getActionSender().sendChatMessage(message);
		}
	}
	
}
