package org.bluecraft.task.impl;

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
import org.bluecraft.Configuration;
import org.bluecraft.Constants;
import org.bluecraft.heartbeat.HeartbeatManager;
import org.bluecraft.model.World;
import org.bluecraft.task.ScheduledTask;

/**
 * A task which sends a heartbeat periodically to the master server.
 * @author Graham Edgecombe
 */
public class HeartbeatTask extends ScheduledTask {

    /**
     * The delay.
     */
    private static final long DELAY = 30000;

    /**
     * Creates the heartbeat task with a 45s delay.
     */
    public HeartbeatTask() {
            super(0);
    }

    @Override
    public void execute() {
        if (getDelay() == 0) {
                setDelay(DELAY);
        }
        new Thread(new Runnable()
        {
            public void run()
            {
                final Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("name", Configuration.getConfiguration().getName());
                parameters.put("users", String.valueOf(World.getWorld().getPlayerList().size()));
                parameters.put("max", String.valueOf(Configuration.getConfiguration().getMaximumPlayers()));
                parameters.put("public", String.valueOf(Configuration.getConfiguration().isPublicServer()));
                parameters.put("port", String.valueOf(Constants.PORT));
                parameters.put("salt", String.valueOf(HeartbeatManager.getHeartbeatManager().getSalt()));
                parameters.put("version", String.valueOf(Constants.PROTOCOL_VERSION));
                String url = HeartbeatManager.getHeartbeatManager().sendHeartbeat(parameters);
                /*URL fList = null;
                try {
                    fList = new URL("http://list.fragmer.net/announce.php");
                } catch (MalformedURLException ex) {
                }
                try {
                    HttpURLConnection c = (HttpURLConnection) fList.openConnection();
                    c.setRequestMethod("POST");
                    c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    c.setRequestProperty("Content-Length", "" + Integer.toString(data.length()));
                    c.setRequestProperty("Content-Language", "en-US");
                    c.setUseCaches(false);
                    c.setDoInput(true);
                    c.setDoOutput(true);
                    //c.connect();
                    c.getOutputStream().write(data.getBytes());
                    BufferedReader rd = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    String r = rd.readLine();
                } catch (IOException ex) {
                   ex.printStackTrace();
                }*/
            }
        }).start();
    }
}
