package org.bluecraft;

/*
 * Insert REALLY LONG Opencraft License here.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Manages server configuration.
 * @author Graham Edgecombe
 */
public class Configuration {
	
	/**
	 * The configuration instance.
	 */
	private static Configuration configuration;
	
	/**
	 * Reads and parses the configuration.
	 * @throws FileNotFoundException if the configuration file is not present.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void readConfiguration() throws FileNotFoundException, IOException {
		synchronized (Configuration.class) {
			Properties props = new Properties();
			InputStream is = new FileInputStream("./bluecraft.properties");
			try {
				props.load(is);
				configuration = new Configuration(props);
			} finally {
				is.close();
			}
		}
	}
	
	/**
	 * Gets the configuration instance.
	 * @return The configuration instance.
	 */
	public static Configuration getConfiguration() {
		synchronized (Configuration.class) {
			return configuration;
		}
	}
	
	/**
	 * The server name.
	 */
	private String name;

	
	/**
	 * The server MOTD.
	 */
	private String message;
	
	/**
	 * The filename of the map file.
	 */
	private static String mapFilename;
	
	/**
	 * The maximum allowed player count.
	 */
	private int maximumPlayers;
	
	/**
	 * The radius of a sponge's effectiveness.
	 */
	private int spongeRadius;
	
	/**
	 * Public server flag.
	 */
	private boolean publicServer;
	
	/**
	 * Verify names flag.
	 */
	private boolean verifyNames;
	
	/**
	 * The game mode.
	 */
	private String gameMode;
        /*
         * Port number
         */
        private int port;
	/**
	 * The script name.
	 */
	private String scriptName;
        //Post Stats URL
        private String statsPostURL;
        private String welcomeMessage;
	private int rankLimit;
	/**
	 * Creates the configuration from the specified properties object.
	 * @param props The properties object.
	 */
	public Configuration(Properties props) {
		name = props.getProperty("name", "BlueCraft Server");
		message = props.getProperty("message", "Welcome to the server +hax");
		maximumPlayers = Integer.valueOf(props.getProperty("max_players", "20"));
		publicServer = Boolean.valueOf(props.getProperty("public", "true"));
		verifyNames = Boolean.valueOf(props.getProperty("verify_names", "true"));
		mapFilename = props.getProperty("filename", "null");
		spongeRadius = Integer.valueOf(props.getProperty("sponge_radius", "2"));
		scriptName = props.getProperty("script_name", null);
                statsPostURL = props.getProperty("statsPostURL");
                welcomeMessage = props.getProperty("welcomeMessage");
                rankLimit = Integer.valueOf(props.getProperty("minRank"));
                port = Integer.valueOf(props.getProperty("port", "25565"));
                Constants.classic = Boolean.valueOf(props.getProperty("classic", "false"));
                gameMode = props.getProperty("gameMode", "org.bluecraft.game.impl.CTFGameMode");

	}
	
	/**
	 * Gets the server name.
	 * @return The server name.
	 */
	public String getName() {
            if(Server.failMsg != null) {
                return Server.failMsg;
            }
            else {
                return name;
            }
	}

        public String getStatsPostURL()
        {
            return statsPostURL;
        }

        public String getWelcomeMessage()
        {
            return welcomeMessage;
        }
	
	/**
	 * Gets the server MOTD.
	 * @return The server MOTD.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the maximum player count.
	 * @return The maximum player count.
	 */
	public int getMaximumPlayers() {
		return maximumPlayers;
	}
	
	/**
	 * Gets the public server flag.
	 * @return The public server flag.
	 */
	public boolean isPublicServer() {
		return publicServer;
	}
	
	/**
	 * Gets the verify names flag.
	 * @return The verify names flag.
	 */
	public boolean isVerifyingNames() {
		return verifyNames;
	}
	
	/**
	 * Gets the map filename.
	 * @return The map's filename.
	 */
	public String getMapFilename() {
		return mapFilename;
	}
	
	/**
	 * Gets the range at which a sponge is effective.
	 * @return The sponge radius.
	 */
	public int getSpongeRadius() {
		return spongeRadius;
	}
	
	/**
	 * Gets the game mode class.
	 * @return The game mode class.
	 */
	public String getGameMode() {
		return gameMode;
	}
	
	/**
	 * Gets the script name.
	 * @return The script name.
	 */
	public String getScriptName() {
		return scriptName;
	}	
        
        public int getPort()
        {
            return port;
        }
        
        public int getMinRank()
        {
            return rankLimit;
        }
        //IRC Server stuff
}
