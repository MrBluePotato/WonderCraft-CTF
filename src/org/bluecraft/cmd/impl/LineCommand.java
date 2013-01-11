/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bluecraft.cmd.impl;

import org.bluecraft.Server;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;
import org.bluecraft.model.Position;
import org.bluecraft.model.Rotation;
import org.bluecraft.model.World;

/**
 *
 * @author Jacob Morgan
 */
public class LineCommand implements Command{
    private static final LineCommand INSTANCE = new LineCommand();

    /**
     * Gets the singleton instance of this command.
     * @return The singleton instance of this command.
     */
    public static LineCommand getCommand() {
            return INSTANCE;
    }

    @Override
    public void execute(Player player, CommandParameters params) {
        Position pos = player.getPosition();
        Rotation r = player.getRotation();

        int heading = (int) (Server.getUnsigned(r.getRotation()) * ((float)360/256)) - 90;
        int pitch = 360 - (int) (Server.getUnsigned(r.getLook()) * ((float)360/256));

        double px = (pos.getX()-16) / 32;
        double py = (pos.getY()-16) / 32;
        double pz = ((pos.getZ()-16) / 32);

        double vx = Math.cos(Math.toRadians(heading));
        double vz = Math.tan(Math.toRadians(pitch));
        double vy = Math.sin(Math.toRadians(heading));
        double x = px;
        double y = py;
        double z = pz;
        for(int i = 0; i < 256; i++)
        {
            int bx = (int) Math.round(x);
            int by = (int) Math.round(y);
            int bz = (int) Math.round(z);
            int block = World.getWorld().getLevel().getBlock(bx, by, bz);
            if((block != 0 && block != 20) || bz > World.getWorld().getLevel().ceiling) {
                return;
            }
            else {
                World.getWorld().getLevel().setBlock(bx, by, bz, 20);
            }
            x += vx;
            y += vy;
            z += vz;
            i++;
        }
    }
}
