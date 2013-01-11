package org.bluecraft.net.packet.handler.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bluecraft.cmd.Command;
import org.bluecraft.cmd.CommandParameters;
import org.bluecraft.cmd.impl.OpChatCommand;
import org.bluecraft.model.World;
import org.bluecraft.net.MinecraftSession;
import org.bluecraft.net.packet.Packet;
import org.bluecraft.net.packet.handler.PacketHandler;

/**
 * A class which handles message and command packets.
 * @author Quinton Marchi
 */
public class MessagePacketHandler implements PacketHandler<MinecraftSession> {
	
	@Override
	public void handlePacket(MinecraftSession session, Packet packet) {
		if (!session.isAuthenticated()) {
			return;
		}
		String message = packet.getStringField("message");
                if(message.contains("&"))
                {
                    session.getPlayer().kickForHacking();
                    return;
                }

//New Chat Configuration ©2013 MrBluePotato (Michael Cummings) <MrBluePotato@wondercraft.org>
                
                if (message.equals("fuck")){
                    message = message.replace("fuck", "&cCENSOR");
                    World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                    else if (message.equals("bitch")) {
                        message = message.replace("bitch", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("gay")) {
                        message = message.replace("gay", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("whore")) {
                        message = message.replace("whore", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("bastard")) {
                        message = message.replace("bastard", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("prostitute")) {
                        message = message.replace("prostitute", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("nigger")) {
                        message = message.replace("nigger", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("fack")) {
                        message = message.replace("fack", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("nigga")) {
                        message = message.replace("nigga", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("cunt")) {
                        message = message.replace("cunt", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }                
                else if (message.equals("ass")) {
                        message = message.replace("ass", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }                
                else if (message.equals("arse")) {
                        message = message.replace("arse", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }                
                else if (message.equals("@$$hole")) {
                        message = message.replace("@$$hole", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("dick")) {
                        message = message.replace("dick", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }                
                else if (message.equals("ass")) {
                        message = message.replace("ass", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("cock")) {
                        message = message.replace("cock", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                    }
                else if (message.equals("pussy")) {
                        message = message.replace("pussy", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                        
                    }
                else if (message.equals("vagina")) {
                        message = message.replace("vagina", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                //Start ending with a swear
                               else if(message.endsWith("%")) {
                        message = message.replace("%", "&");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                        else if (message.endsWith("fuck")){
                    message = message.replace("fuck", "&cCENSOR");
                    World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                    else if (message.endsWith("bitch")) {
                        message = message.replace("bitch", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("gay")) {
                        message = message.replace("gay", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("whore")) {
                        message = message.replace("whore", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("bastard")) {
                        message = message.replace("bastard", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("prostitute")) {
                        message = message.replace("prostitute", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("nigger")) {
                        message = message.replace("nigger", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("fack")) {
                        message = message.replace("fack", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("nigga")) {
                        message = message.replace("nigga", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("cunt")) {
                        message = message.replace("cunt", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }                
                else if (message.endsWith("ass")) {
                        message = message.replace("ass", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }                
                else if (message.endsWith("arse")) {
                        message = message.replace("arse", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }                
                else if (message.endsWith("@$$hole")) {
                        message = message.replace("@$$hole", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("dick")) {
                        message = message.replace("dick", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }                
                else if (message.endsWith("ass")) {
                        message = message.replace("ass", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("cock")) {
                        message = message.replace("cock", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.endsWith("pussy")) {
                        message = message.replace("pussy", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                        
                }
                else if (message.endsWith("vagina")) {
                        message = message.replace("vagina", "&cCENSOR");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                //Start beginning with a swear
                        else if(message.contains("%")) {
                        message = message.replace("%", "&");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                        else if (message.contains("fuck")){
                    message = message.replace("fuck", "&cCENSOR&f");
                    World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                    else if (message.contains("bitch")) {
                        message = message.replace("bitch", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("gay")) {
                        message = message.replace("gay", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("whore")) {
                        message = message.replace("whore", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("bastard")) {
                        message = message.replace("bastard", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("prostitute")) {
                        message = message.replace("prostitute", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("nigger")) {
                        message = message.replace("nigger", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("fack")) {
                        message = message.replace("fack", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("nigga")) {
                        message = message.replace("nigga", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("cunt")) {
                        message = message.replace("cunt", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }                
                else if (message.contains("ass")) {
                        message = message.replace("ass", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }                
                else if (message.contains("arse")) {
                        message = message.replace("arse", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }                
                else if (message.contains("@$$hole")) {
                        message = message.replace("@$$hole", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("dick")) {
                        message = message.replace("dick", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }                
                else if (message.contains("ass")) {
                        message = message.replace("ass", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("cock")) {
                        message = message.replace("cock", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
                else if (message.contains("pussy")) {
                        message = message.replace("pussy", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                        
                }
                else if (message.contains("vagina")) {
                        message = message.replace("vagina", "&cCENSOR&f");
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }

                else if (message.startsWith("/")) {
			// interpret as command
			String tokens = message.substring(1);
			String[] parts = tokens.split(" ");
			final Map<String, Command> commands = World.getWorld().getGameMode().getCommands();
			Command c = commands.get(parts[0]);
			if (c != null) {
				parts[0] = null;
				List<String> partsList = new ArrayList<String>();
				for (String s : parts) {
					if (s != null) {
						partsList.add(s);
					}
				}
				parts = partsList.toArray(new String[0]);
                        try {
                            c.execute(session.getPlayer(), new CommandParameters(parts));
                        } catch (IOException ex) {
                            Logger.getLogger(MessagePacketHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
			} else {
				session.getActionSender().sendChatMessage("&c"+message+" is not a valid command.");
			}
                        }
                    else if (message.startsWith("#")){
                    String tokens = message.substring(1);
			String[] parts = tokens.split(" ");
			final Map<String, Command> commands = World.getWorld().getGameMode().getCommands();
			Command c = OpChatCommand.getCommand();
			if (c != null) {
                        try {
                            c.execute(session.getPlayer(), new CommandParameters(parts));
                        } catch (IOException ex) {
                            Logger.getLogger(MessagePacketHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }

		}
                    }
                else  {
                        World.getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
                }
        }
}
	
