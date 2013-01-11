package org.bluecraft.net.packet.handler.impl;

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

import org.bluecraft.game.impl.CTFGameMode;
import org.bluecraft.model.Player;
import org.bluecraft.model.PlayerUntagger;
import org.bluecraft.model.Position;
import org.bluecraft.model.Rotation;
import org.bluecraft.model.World;
import org.bluecraft.net.MinecraftSession;
import org.bluecraft.net.packet.Packet;
import org.bluecraft.net.packet.handler.PacketHandler;

/**
* A packet handler which handles movement packets.
* @author Graham Edgecombe
*/
public class MovementPacketHandler implements PacketHandler<MinecraftSession> {
   Position lastPosition = null;
        long lastMoveTime = 0;
   @Override
   public void handlePacket(MinecraftSession session, Packet packet) {
      if (!session.isAuthenticated()) {
         return;
      }
      final Player player = session.getPlayer();
                Position oldPosition = player.getPosition();
                Rotation oldRotation = player.getOldRotation();
                final int oldX = oldPosition.getX();
                final int oldY = oldPosition.getY();
                final int oldZ = oldPosition.getZ();
      final int x = packet.getNumericField("x").intValue();
      final int y = packet.getNumericField("y").intValue();
      final int z = packet.getNumericField("z").intValue();
                Position blockPosition = new Position((x - 16)/32, (y - 16)/32, (z - 16)/32);
                if(lastPosition == null || (Math.abs(blockPosition.getX() - lastPosition.getX()) > 2 || Math.abs(blockPosition.getY() - lastPosition.getY()) > 2 || Math.abs(blockPosition.getZ() - lastPosition.getZ()) > 2))
                {
                    lastPosition = blockPosition;
                    lastMoveTime = System.currentTimeMillis();
                }
                else if(System.currentTimeMillis() - lastMoveTime > 2 * 60 * 1000)
                {
                }
                int dx = Math.abs(x - oldX);
                int dy = Math.abs(y - oldY);
                int dz = Math.abs(z - oldZ);
                if((dx > 400 || dy > 400 || dz > 400))
                {
                    if(player.team > -1 && !blockPosition.equals(player.teleportPosition) && !(player.isVIP() || player.isOp()))
                    {
                            player.getActionSender().sendTeleport(player.getPosition(), player.getRotation());
                            player.getActionSender().sendChatMessage("&cRespawning is not allowed on this server");
                    }
                }
                if((z - 16)/32 < World.getWorld().getLevel().floor && !player.safe)
                {
                    player.safe = true;
                    new Thread(new PlayerUntagger(player)).start();
                    World.getWorld().broadcast(player.parseName()+" &4died");
                    player.sendToTeamSpawn();
                    if(player.hasFlag)
                    {
                        CTFGameMode ctf = (CTFGameMode) World.getWorld().getGameMode();
                        if(player.team == 0)
                        {
                            ctf.blueFlagTaken = false;
                            ctf.placeBlueFlag();
                        }
                        else
                        {
                            ctf.redFlagTaken = false;
                            ctf.placeRedFlag();
                        }
                        ctf.antiStalemate = false;
                        player.hasFlag = false;
                        World.getWorld().broadcast(player.parseName()+" &edropped the flag!");
                    }
                }
      final int rotation = packet.getNumericField("rotation").intValue();
      final int look = packet.getNumericField("look").intValue();
      player.setPosition(new Position(x, y, z));
      player.setRotation(new Rotation(rotation, look));
   }

}