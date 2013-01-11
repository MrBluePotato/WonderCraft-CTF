/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bluecraft.cmd.impl;

import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.model.Player;

/**
 *
 * @author qmarchi
 */
public class WhoCommand implements Command{
    private static final WhoCommand INSTANCE = new WhoCommand();
        public static WhoCommand getCommand() {
            return INSTANCE;
    }
    @Override
  public void execute(Player player, CommandParameters params) {
      player.wrapText("Press &8[TAB]&e For Player List!");
  }  
}
