package org.bluecraft.persistence;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bluecraft.model.Player;

import com.thoughtworks.xstream.XStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.bluecraft.Configuration;
import org.bluecraft.Constants;
import org.bluecraft.Server;

/**
 * A persistence request which saves the specified player.
 * @author Graham Edgecombe
 *
 */
public class SavePersistenceRequest extends PersistenceRequest {
	
	/**
	 * Creates the save request.
	 * @param player The player to save.
	 */
	public SavePersistenceRequest(Player player) {
		super(player);
	}

	@Override
	public void perform() throws IOException {
		final SavedGameManager mgr = SavedGameManager.getSavedGameManager();
		final Player player = getPlayer();
		final XStream xs = mgr.getXStream();
		final File file = new File(mgr.getPath(player));
		try {
			xs.toXML(player.getAttributes(), new FileOutputStream(file));
		} catch (RuntimeException ex) {
			throw new IOException(ex);
		}
                String data = null;
                data = URLEncoder.encode("username", "UTF-8") + "=" + player.getName() + "&";
                data += URLEncoder.encode("tags", "UTF-8") + "=" + player.getAttribute("tags") + "&";
                data += URLEncoder.encode("wins", "UTF-8") + "=" + player.getAttribute("wins") + "&";
                data += URLEncoder.encode("mines", "UTF-8") + "=" + player.getAttribute("mines") + "&";
                data += URLEncoder.encode("explodes", "UTF-8") + "=" + player.getAttribute("explodes") + "&";
                data += URLEncoder.encode("captures", "UTF-8") + "=" + player.getAttribute("captures") + "&";
                data += URLEncoder.encode("games", "UTF-8") + "=" + player.getAttribute("games");
                URL url = null;
                String statsURL;
                if(Constants.tournament)
                    statsURL = "http://buildism.net/mc/postStats2.php";
                else
                    statsURL = Configuration.getConfiguration().getStatsPostURL();
                if(!statsURL.equals("null"))
                {
                    System.err.println(data);
                    url = new URL(Configuration.getConfiguration().getStatsPostURL());
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("POST");
                    c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    c.setRequestProperty("Content-Length", "" + Integer.toString(data.length()));
                    c.setRequestProperty("Content-Language", "en-US");
                    c.setUseCaches(false);
                    c.setDoInput(true);
                    c.setDoOutput(true);
                    c.connect();
                    c.getOutputStream().write(data.getBytes());
                    BufferedReader rd = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    c.disconnect();
                }
                /*while(true)
                {
                    String r = rd.readLine();
                    Server.log(r);
                    if(r == null)
                        break;
                }*/
	}
	
}
