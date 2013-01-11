package org.bluecraft;

/*
 * Insert REALLY LONG Opencraft License here.
 */

/**
 * Holds global constants for the OpenCraft server.
 * @author Graham Edgecombe
 */
public final class Constants {
	
	/**
	 * The default Minecraft port.
	 */
	public static int PORT = 25565;

        /* set from config */
        public static boolean classic = false;
	
	/**
	 * The protocol version of Minecraft that this version of OpenCraft is
	 * compatible with.
	 */
	public static final int PROTOCOL_VERSION = 7;
	
	/**
	 * The heartbeat server.
	 */
	public static final String HEARTBEAT_SERVER = "http://minecraft.net/";

        public static final String ROOT_PATH = ".";

        public static final boolean tournament = false;
	
}
