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

import java.util.Iterator;
import java.util.Set;
import org.bluecraft.game.impl.CTFGameMode;

import org.bluecraft.model.Entity;
import org.bluecraft.model.Player;
import org.bluecraft.model.World;
import org.bluecraft.task.ScheduledTask;

/**
 * Updates the players and game world.
 * @author Graham Edgecombe
 */
public class UpdateTask extends ScheduledTask {
	
	/**
	 * The delay.
	 */
	private static final long DELAY = 150;
	
	/**
	 * Creates the update task with a delay of 100ms.
	 */
	public UpdateTask() {
		super(DELAY);
	}
	
	@Override
	public void execute() {
		final World world = World.getWorld();
		world.getGameMode().tick();
		for (Player player : world.getPlayerList().getPlayers()) {
                        Set le = player.getLocalEntities();
			Object[] localEntities = le.toArray();
                        for(Object object : localEntities)
                        {
				Entity localEntity = (Entity) object;
				if (localEntity.getId() == -1) {
					le.remove(localEntity);
					player.getSession().getActionSender().sendRemoveEntity(localEntity);
				} else {
					player.getSession().getActionSender().sendUpdateEntity(localEntity);
				}
			}
			for (Player otherEntity : world.getPlayerList().getPlayers()) {
				if (!le.contains(otherEntity) && otherEntity != player && otherEntity.isVisible) {
					le.add(otherEntity);
					player.getSession().getActionSender().sendAddEntity(otherEntity);
				}
			}
		}
		for (Player player : world.getPlayerList().getPlayers()) {
			player.resetOldPositionAndRotation();
		}
		world.getLevel().applyBlockBehaviour();
	}
	
}
