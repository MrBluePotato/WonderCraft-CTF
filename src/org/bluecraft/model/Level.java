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

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.bluecraft.Constants;
import org.bluecraft.Server;

/**
 * Represents the actual level.
 * @author Graham Edgecombe
 * @author Brett Russell
 */
public final class Level implements Cloneable{

	/**
	 * The level width.
	 */
	private int width;
	
	/**
	 * The level height.
	 */
	private int height;
	
	/**
	 * The level depth.
	 */
	private int depth;
	
	/**
	 * The blocks.
	 */
	private byte[][][] blocks;
        private byte[] blocks1D;
	
	/**
	 * Light depth array.
	 */
	private short[][] lightDepths;
	
	/**
	 * The spawn rotation.
	 */
	private Rotation spawnRotation;
	
	/**
	 * The spawn position.
	 */
	private Position spawnPosition;
	
	/**
	 * The active "thinking" blocks on the map.
	 */
	private Map<Integer, ArrayDeque<Position>> activeBlocks = new HashMap<Integer, ArrayDeque<Position>>();
	
	/**
	 * The timers for the active "thinking" blocks on the map.
	 */
	private Map<Integer, Long> activeTimers = new HashMap<Integer, Long>();
	
	/**
	 * A queue of positions to update at the next tick.
	 */
	private Queue<Position> updateQueue = new ArrayDeque<Position>();

        public Properties props;
        private com.mojang.minecraft.level.Level l;
        public int divider;
        public int ceiling;
        public int floor;

        public String filename;
        public String id;
        public int votes = 0;

	
	/**
	 * Generates a level.
	 */
	public Level() {
		this.width = 256;
		this.height = 256;
		this.depth = 64;
		this.blocks = new byte[width][height][depth];
		this.lightDepths = new short[width][height];
		this.spawnRotation = new Rotation(0, 0);
		for (int i = 0; i < 256; i++) {
			BlockDefinition b = BlockManager.getBlockManager().getBlock(i);
			if (b != null && b.doesThink()) {
				activeBlocks.put(i, new ArrayDeque<Position>());
				activeTimers.put(i, System.currentTimeMillis());
			}
		}
		for(int z = 0; z < depth / 2; z++) {
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					int type = z == (depth / 2 - 1) ? BlockConstants.GRASS : BlockConstants.DIRT;
					if((x == 0 || y == 0 || x == width - 1 || y == height - 1) && (z == (depth / 2 - 1) || z == (depth / 2 - 2))) {
						type = BlockConstants.WATER;
					}
					this.blocks[x][y][z] = (byte) type;
				}
			}
		}
		/*Random random = new Random();
		int[][] heights = new int[width][height];
		int maxHeight = 1;
		for(int i = 0; i < 100000; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int radius = random.nextInt(10) + 4;
			for(int j = 0; j < width; j++) {
				for(int k = 0; k < height; k++) {
					int mod = (radius * radius) - (k - x) * (k - x) - (j - y) * (j - y);
					if(mod > 0) {
						heights[j][k] += mod;
						if(heights[j][k] > maxHeight) {
							maxHeight = heights[j][k];
						}
					}
				}
			}
		}
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int h = (depth / 2) + (heights[x][y] * (depth / 2) / maxHeight);
				int d = random.nextInt(8) - 4;
				for(int z = 0; z < h; z++) {
					int type = BlockConstants.DIRT;
					if(z == (h - 1)) {
						type = BlockConstants.GRASS;
					} else if(z <= (depth / 2 + d)) {
						type = BlockConstants.STONE;
					}
					blocks[x][y][z] = (byte) type;
				}
			}
		}*/

		recalculateAllLightDepths();
	}

        public Level getCopy()
        {
            Level copy = new Level();
            copy.load(filename, id);
            return copy;
        }

        public Level load(String filename, String id)
        {
            this.filename = filename;
            this.id = id;
            FileInputStream fis = null;
            GZIPInputStream gzis = null;
            ObjectInputStream in = null;
            DataInputStream inputstream = null;
            try {
                    fis = new FileInputStream(filename);
                    gzis = new GZIPInputStream(fis);
                    inputstream = new DataInputStream(gzis);
                    if((inputstream.readInt()) != 0x271bb788) {
                            return null;
                    }
                    if((inputstream.readByte()) > 2) {
                            System.out.println("Error: Level version > 2, this is unexpected!");
                            return null;
                    }
                    in = new ObjectInputStream(gzis);
                    l = (com.mojang.minecraft.level.Level)in.readObject();
                    inputstream.close();
                    in.close();
                    gzis.close();
                    fis.close();
            } catch(IOException ex) {
                    ex.printStackTrace();
            } catch(ClassNotFoundException ex) {
                    ex.printStackTrace();
            }
            l.initTransient();
            byte[] tmpBlocks = l.blocks;
            width = (short) l.width;
            height = (short) l.height;
            depth = (short) l.depth;
            blocks = new byte[width][height][depth];
            blocks1D = tmpBlocks;
            this.setSpawnPosition(new Position(l.xSpawn * 32 + 16, l.zSpawn * 32 + 16, l.ySpawn * 32 + 16));
            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    for(int z = 0; z < depth; z++)
                    {
                        int type = tmpBlocks[(z * this.height + y) * this.width + x];
                        blocks[x][y][z] = (byte) type;
                    }
                }
            }
            props = new Properties();
            try {
                FileInputStream ps = new FileInputStream(filename.replace(id+".dat", "")+id + ".properties");
                props.load(ps);
                ps.close();
            } catch (Exception ex) {
                Server.log("Could not load props for "+id+": "+ex);
            }
            System.err.println("Loading map: "+id);
            divider = Integer.parseInt(props.getProperty("divider"));
            ceiling = Integer.parseInt(props.getProperty("buildCeiling"));
            floor = Integer.parseInt(props.getProperty("buildFloor", "-8"));
            return this;
        }

        public Object getProp(String p)
        {
            return props.get(p);
        }

        public Position getTeamSpawn(String team)
        {
            if(team.equals("spec"))
                return new Position(getSpawnPosition().getX(), getSpawnPosition().getY(), getSpawnPosition().getZ() - 32 * 5);
            else
                return new Position(Integer.parseInt((String) getProp(team+"SpawnX")) * 32 + 16, Integer.parseInt((String) getProp(team+"SpawnZ")) * 32 + 16, Integer.parseInt((String) getProp(team+"SpawnY")) * 32 + 16);
        }

	/**
	 * Recalculates all light depths. WARNING: this is a costly function and
	 * should only be used when it really is necessary.
	 */
	public void recalculateAllLightDepths() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				recalculateLightDepth(x, y);
			}
		}
	}
	
	/**
	 * Recalculates the light depth of the specified coordinates.
	 * @param x The x coordinates.
	 * @param y The y coordinates.
	 */
	public void recalculateLightDepth(int x, int y) {
		for (int z = depth - 1; z >= 0; z--) {
			if (BlockManager.getBlockManager().getBlock(blocks[x][y][z]).doesBlockLight()) {
				lightDepths[x][y] = (short) z;
				return;
			}
		}
		lightDepths[x][y] = (short) -1;
	}
	
	/**
	 * Manually assign a light depth to a given Cartesian coordinate.
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
	 * @param depth The lowest-lit block.
	 */
	public void assignLightDepth(int x, int y, int depth) {
		if (depth > this.height)
			return;
		lightDepths[x][y] = (short) depth;
	}
	
	/**
	 * Gets the light depth at the specific coordinate.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The light depth.
	 */
	public int getLightDepth(int x, int y) {
		return lightDepths[x][y];
	}
	
	/**
	 * Performs physics updates on queued blocks.
	 */
	public void applyBlockBehaviour() {
		Queue<Position> currentQueue = new ArrayDeque<Position>(updateQueue);
		updateQueue.clear();
		for (Position pos : currentQueue) {
			BlockManager.getBlockManager().getBlock(this.getBlock(pos.getX(), pos.getY(), pos.getZ())).behavePassive(this, pos.getX(), pos.getY(), pos.getZ());
		}
		// we only process up to 20 of each type of thinking block every tick,
		// or we'd probably be here all day.
		for (int type = 0; type < 256; type++) {
			if (activeBlocks.containsKey(type)) {
				if (System.currentTimeMillis() - activeTimers.get(type) > BlockManager.getBlockManager().getBlock(type).getTimer()) {
					int cyclesThisTick = (activeBlocks.get(type).size() > 20 ? 20 : activeBlocks.get(type).size());
					for (int i = 0; i < cyclesThisTick; i++) {
						Position pos = activeBlocks.get(type).poll();
						if (pos == null)
							break;
						// the block that occupies this space might have
						// changed.
						if (this.getBlock(pos.getX(), pos.getY(), pos.getZ()) == type) {
							// World.getWorld().broadcast("Processing thinker at ("+pos.getX()+","+pos.getY()+","+pos.getZ()+")");
							BlockManager.getBlockManager().getBlock(type).behaveSchedule(this, pos.getX(), pos.getY(), pos.getZ());
						}
					}
					activeTimers.put(type, System.currentTimeMillis());
				}
			}
		}
	}
	
	/**
	 * Gets all of the blocks.
	 * @return All of the blocks.
	 */
	public byte[][][] getBlocks() {
		return blocks;
	}

        public byte[] getBlocks1D()
        {
            return blocks1D;
        }
	
	/**
	 * Gets the width of the level.
	 * @return The width of the level.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of the level.
	 * @return The height of the level.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the depth of the level.
	 * @return The depth of the level.
	 */
	public int getDepth() {
		return depth;
	}
	
	/**
	 * Sets a block and updates the neighbours.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param type The type id.
	 */
	public void setBlock(int x, int y, int z, int type) {
		setBlock(x, y, z, type, true);
	}
	
	/**
	 * Sets a block.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param type The type id.
	 * @param updateSelf Update self flag.
	 */
	public void setBlock(int x, int y, int z, int type, boolean updateSelf) {
		if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
			return;
		}
                blocks1D[(z * this.height + y) * this.width + x] = (byte) type;
		byte formerBlock = this.getBlock(x, y, z);
		blocks[x][y][z] = (byte) type;
                if(type != formerBlock)
                    for (Player player : World.getWorld().getPlayerList().getPlayers()) {
                            player.getSession().getActionSender().sendBlock(x, y, z, (byte) type);
                    }
		if (updateSelf) {
			queueTileUpdate(x, y, z);
		}
		if (type == 0) {
			BlockManager.getBlockManager().getBlock(formerBlock).behaveDestruct(this, x, y, z);
			updateNeighboursAt(x, y, z);
			if (this.getLightDepth(x, y) == z) {
				this.recalculateLightDepth(x, y);
				this.scheduleZPlantThink(x, y, z);
			}
		}
		if (BlockManager.getBlockManager().getBlock(type).doesThink()) {
			activeBlocks.get(type).add(new Position(x, y, z));
		}
		if (BlockManager.getBlockManager().getBlock(type).doesBlockLight()) {
			this.assignLightDepth(x, y, z);
			this.scheduleZPlantThink(x, y, z);
		}
		
	}
	
	/**
	 * Schedules plants to think in a Z coordinate if a block above them
	 * changed.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	public void scheduleZPlantThink(int x, int y, int z) {
		for (int i = z - 1; i > 0; i--) {
			if (BlockManager.getBlockManager().getBlock(this.getBlock(x, y, i)).isPlant()) {
				queueActiveBlockUpdate(x, y, i);
			}
			if (BlockManager.getBlockManager().getBlock(this.getBlock(x, y, i)).doesBlockLight()) {
				return;
			}
		}
	}
	
	/**
	 * Updates neighbours at the specified coordinate.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	private void updateNeighboursAt(int x, int y, int z) {
		queueTileUpdate(x - 1, y, z);
		queueTileUpdate(x, y - 1, z);
		queueTileUpdate(x + 1, y, z);
		queueTileUpdate(x, y + 1, z);
		queueTileUpdate(x, y, z - 1);
		queueTileUpdate(x, y, z + 1);
		recalculateLightDepth(x, y);
	}
	
	/**
	 * Queues a tile update.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	private void queueTileUpdate(int x, int y, int z) {
		if (x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < depth) {
			Position pos = new Position(x, y, z);
			if (!updateQueue.contains(pos)) {
				updateQueue.add(pos);
			}
		}
	}
	
	/**
	 * Forces a tile update to be queued. Use with caution.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	public void queueActiveBlockUpdate(int x, int y, int z) {
		if (x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < depth) {
			int blockAt = this.getBlock(x, y, z);
			if (BlockManager.getBlockManager().getBlock(blockAt).doesThink()) {
				activeBlocks.get(blockAt).add(new Position(x, y, z));
			}
		}
	}
	
	/**
	 * Gets a block.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @return The type id.
	 */
	public byte getBlock(int x, int y, int z) {
		if (x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < depth) {
			return blocks[x][y][z];
		} else {
			return 0;
		}
	}
	
	/**
	 * Set the rotation of the character when spawned.
	 * @param spawnRotation The rotation.
	 */
	public void setSpawnRotation(Rotation spawnRotation) {
		this.spawnRotation = spawnRotation;
	}
	
	/**
	 * Get the spawning rotation.
	 * @return The spawning rotation.
	 */
	public Rotation getSpawnRotation() {
		return spawnRotation;
	}
	
	/**
	 * Set the spawn position.
	 * @param spawnPosition The spawn position.
	 */
	public void setSpawnPosition(Position spawnPosition) {
		this.spawnPosition = spawnPosition;
	}
	
	/**
	 * Get the spawn position.
	 * @return The spawn position.
	 */
	public Position getSpawnPosition() {
		return spawnPosition;
	}

	
}
