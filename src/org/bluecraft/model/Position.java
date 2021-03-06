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

/**
 * Represents a location in the game world.
 * @author Graham Edgecombe
 */
public final class Position {
	
	/**
	 * X position.
	 */
	private final int x;
	
	/**
	 * Y position.
	 */
	private final int y;
	
	/**
	 * Z position.
	 */
	private final int z;
	
	/**
	 * Creates a new position.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets the x coordinate.
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y coordinate.
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the z coordinate.
	 * @return The z coordinate.
	 */
	public int getZ() {
		return z;
	}
        
        public Position toBlockPos()
        {
            return new Position((x - 16)/32, (y - 16)/32, (z - 16)/32);
        }
        
    @Override
        public boolean equals(Object obj)
        {
            if(!(obj instanceof Position))
                return false;
            Position p = (Position) obj;
            return p.x == x && p.y == y && p.z == z;
        }
}
