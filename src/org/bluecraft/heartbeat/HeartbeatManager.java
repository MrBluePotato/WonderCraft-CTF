package org.bluecraft.heartbeat;

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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bluecraft.Server;
import org.bluecraft.model.World;


/**
 * A class which manages heartbeats.
 * @author Graham Edgecombe
 */
public class HeartbeatManager {
	
	/**
	 * The singleton instance.
	 */
	private static final HeartbeatManager INSTANCE = new HeartbeatManager();
	
	/**
	 * Heartbeat server URL.
	 */
	public static final URL URL;
	
	/**
	 * Initializes the heartbeat server URL.
	 */
	static {
		try {
			URL = new URL("http://direct.worldofminecraft.com/hb.php");
		} catch (MalformedURLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(HeartbeatManager.class.getName());
	
	/**
	 * Gets the heartbeat manager instance.
	 * @return The heartbeat manager instance.
	 */
	public static HeartbeatManager getHeartbeatManager() {
		return INSTANCE;
	}
	
	/**
	 * The salt.
	 */
	private final long salt = new Random().nextLong();
	
	/**
	 * An executor service which executes HTTP requests.
	 */
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	/**
	 * Default private constructor.
	 */
	private HeartbeatManager() {
		/* empty */
	}
	
	/**
	 * Sends a heartbeat with the specified parameters. This method does not
	 * block.
	 * @param parameters The parameters.
	 */
	public String sendHeartbeat(final Map<String, String> parameters)
        {
            StringBuilder bldr = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    bldr.append(entry.getKey());
                    bldr.append('=');
                    try {
                            bldr.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                    }
                    bldr.append('&');
            }
            if (bldr.length() > 0) {
                    bldr.deleteCharAt(bldr.length() - 1);
            }
            // send it off
            String url = null;
            try {
                    URL url2 = new URL("https://minecraft.net/heartbeat.jsp?"+bldr.toString());
                    HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
                    byte[] bytes = bldr.toString().getBytes();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
                    // this emulates the minecraft server exactly.. idk why
                    // notch added this personally
                    //conn.setRequestProperty("Content-Language", "en-US");
                    conn.setUseCaches(false);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();
                    try {
                            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                            try {
                                    //os.write(bytes);
                            } finally {
                                    os.close();
                            }
                            BufferedReader rdr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            try {
                                    url = rdr.readLine();
                                    Server.log("\u001b[1;37m To connect to this server, use : \u001b[1;33m" + url + "\u001b[1;37m.");
                            } finally {
                                    rdr.close();
                            }
                    } finally {
                            conn.disconnect();
                    }
                    Server.log("Players: "+World.getWorld().getPlayerList().size());
            } catch (IOException ex) {
                    logger.log(Level.WARNING, "\u001b[1;37mError sending hearbeat.", ex);
            }
            return url;
        }
	
	/**
	 * Gets the salt.
	 * @return The salt.
	 */
	public long getSalt() {
		return salt;
	}
	
}
