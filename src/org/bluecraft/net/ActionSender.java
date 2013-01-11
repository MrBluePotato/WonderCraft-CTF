package org.bluecraft.net;

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


import org.bluecraft.model.Entity;
import org.bluecraft.model.Level;
import org.bluecraft.model.Player;
import org.bluecraft.model.Position;
import org.bluecraft.model.Rotation;
import org.bluecraft.model.World;
import org.bluecraft.net.packet.PacketBuilder;
import org.bluecraft.net.packet.PacketManager;

import org.bluecraft.persistence.LoadPersistenceRequest;
import org.bluecraft.persistence.SavedGameManager;
import org.bluecraft.task.Task;
import org.bluecraft.task.TaskQueue;


/**
 * A utility class for sending packets.
 * @author Graham Edgecombe
 */
public class ActionSender {
	
	/**
	 * The session.
	 */
	private MinecraftSession session;
	
	/**
	 * Creates the action sender.
	 * @param session The session.
	 */
	public ActionSender(MinecraftSession session) {
		this.session = session;
	}
	
	/**
	 * Sends a login response.
	 * @param protocolVersion The protocol version.
	 * @param name The server name.
	 * @param message The server message of the day.
	 * @param op Operator flag.
	 */
	public void sendLoginResponse(int protocolVersion, String name, String message, boolean op) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(0));
		bldr.putByte("protocol_version", protocolVersion);
		bldr.putString("server_name", name);
		bldr.putString("server_message", message);
		bldr.putByte("user_type", op ? 100 : 0);
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a login failure.
	 * @param message The message to send to the client.
	 */
	public void sendLoginFailure(String message) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(14));
		bldr.putString("reason", message);
		session.send(bldr.toPacket());
		session.close();
	}
	
	/**
	 * Sends the level init packet.
	 */
	public void sendLevelInit() {
		session.setAuthenticated();
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(2));
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a level block/chunk.
	 * @param len The length of the chunk.
	 * @param chunk The chunk data.
	 * @param percent The percentage.
	 */
	public void sendLevelBlock(int len, byte[] chunk, int percent) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(3));
		bldr.putShort("chunk_length", len);
		bldr.putByteArray("chunk_data", chunk);
		bldr.putByte("percent", percent);
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends the level finish packet.
	 */
	public void sendLevelFinish() {
		TaskQueue.getTaskQueue().push(new Task() {
			public void execute() {
				// for thread safety
				final Level level = World.getWorld().getLevel();
				PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(4));
				bldr.putShort("width", level.getWidth());
				bldr.putShort("height", level.getHeight());
				bldr.putShort("depth", level.getDepth());
				session.send(bldr.toPacket());
                                Position spawn = level.getSpawnPosition();
                                Rotation r = level.getSpawnRotation();
                                sendSpawn((byte) -1, session.getPlayer().getName(), spawn.getX(), spawn.getY(), spawn.getZ(), (byte) r.getRotation(), (byte)r.getLook());
				// now load the player's game (TODO in the future do this in parallel with loading the level)
				SavedGameManager.getSavedGameManager().queuePersistenceRequest(new LoadPersistenceRequest(session.getPlayer()));
                                session.setReady();
                                World.getWorld().completeRegistration(session);
                        }
		});
	}
	
	/**
	 * Sends a teleport.
	 * @param position The new position.
	 * @param rotation The new rotation.
	 */
	public void sendTeleport(Position position, Rotation rotation) {
                session.getPlayer().teleportPosition = position.toBlockPos();
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(8));
		bldr.putByte("id", -1);
		bldr.putShort("x", position.getX());
		bldr.putShort("y", position.getY());
		bldr.putShort("z", position.getZ());
		bldr.putByte("rotation", rotation.getRotation());
		bldr.putByte("look", rotation.getLook());
		session.send(bldr.toPacket());
                session.getPlayer().teleporting = true;
	}
	
	/**
	 * Sends the add entity packet.
	 * @param entity The entity being added.
	 */
	public void sendAddEntity(Entity entity) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(7));
		bldr.putByte("id", entity.getId());
		bldr.putString("name", entity.getColoredName());
		bldr.putShort("x", entity.getPosition().getX());
		bldr.putShort("y", entity.getPosition().getY());
		bldr.putShort("z", entity.getPosition().getZ());
		bldr.putByte("rotation", entity.getRotation().getRotation());
		bldr.putByte("look", entity.getRotation().getLook());
		session.send(bldr.toPacket());
	}

	public void sendSpawn(byte id, String name, int x, int y, int z, byte rotation, byte look) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(7));
		bldr.putByte("id", id);
		bldr.putString("name", name);
		bldr.putShort("x", x);
		bldr.putShort("y", y);
		bldr.putShort("z", z);
		bldr.putByte("rotation", rotation);
		bldr.putByte("look", look);
		session.send(bldr.toPacket());
	}


	/**
	 * Sends the update entity packet.
	 * @param entity The entity being updated.
	 */
	public void sendUpdateEntity(Entity entity) {
		final Position oldPosition = entity.getOldPosition();
		final Position position = entity.getPosition();
		
		final Rotation oldRotation = entity.getOldRotation();
		final Rotation rotation = entity.getRotation();
		
		final int deltaX = -oldPosition.getX() - position.getX();
		final int deltaY = -oldPosition.getY() - position.getY();
		final int deltaZ = -oldPosition.getZ() - position.getZ();
		
		final int deltaRotation = -oldRotation.getRotation() - rotation.getRotation();
		final int deltaLook = -oldRotation.getLook() - rotation.getLook();
		if(deltaX != 0 || deltaY != 0 || deltaZ != 0)
                {
                    if (deltaX > Byte.MAX_VALUE || deltaX < Byte.MIN_VALUE || deltaY > Byte.MAX_VALUE || deltaY < Byte.MIN_VALUE || deltaZ > Byte.MAX_VALUE || deltaZ < Byte.MIN_VALUE || deltaRotation > Byte.MAX_VALUE || deltaRotation < Byte.MIN_VALUE || deltaLook > Byte.MAX_VALUE || deltaLook < Byte.MIN_VALUE) {
                            // teleport
                            PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(8));
                            bldr.putByte("id", entity.getId());
                            bldr.putShort("x", position.getX());
                            bldr.putShort("y", position.getY());
                            bldr.putShort("z", position.getZ());
                            bldr.putByte("rotation", rotation.getRotation());
                            bldr.putByte("look", rotation.getLook());
                            session.send(bldr.toPacket());
                    } else {
                            // send move and rotate packet
                            PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(9));
                            bldr.putByte("id", entity.getId());
                            bldr.putByte("delta_x", deltaX);
                            bldr.putByte("delta_y", deltaY);
                            bldr.putByte("delta_z", deltaZ);
                            bldr.putByte("delta_rotation", deltaRotation);
                            bldr.putByte("delta_look", deltaLook);
                            session.send(bldr.toPacket());
                    }
                }
	}
	
	/**
	 * Sends the remove entity packet.
	 * @param entity The entity being removed.
	 */
	public void sendRemoveEntity(Entity entity) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(12));
		bldr.putByte("id", entity.getOldId());
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a chat message.
	 * @param message The message.
	 */
	public void sendChatMessage(String message) {
			sendChatMessage((byte) 0, message);
	}
	
	/**
	 * Sends a block.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param type BlockDefinition type.
	 */
	public void sendBlock(int x, int y, int z, byte type) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(6));
		bldr.putShort("x", x);
		bldr.putShort("y", y);
		bldr.putShort("z", z);
		bldr.putByte("type", type);
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a chat message.
	 * @param id The source player id.
	 * @param message The message.
	 */
	public void sendChatMessage(int id, String message) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(13));
                String message2 = "";
                if(message.length() > 64)
                {
                    message2 = message.substring(64);
                    int idx = message.lastIndexOf("&");
                    if(idx != -1)
                        message2 = "&"+message.charAt(idx + 1)+message2;
                    message = message.substring(0, 64);
                }
                bldr.putByte("id", id);
		bldr.putString("message", message);
                session.send(bldr.toPacket());
                if(!message2.equals(""))
                    sendChatMessage(id, "> "+message2);
	}
	
}
