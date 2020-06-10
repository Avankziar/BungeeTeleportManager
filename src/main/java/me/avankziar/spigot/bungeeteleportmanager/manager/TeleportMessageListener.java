package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;

public class TeleportMessageListener implements PluginMessageListener
{
	private BungeeTeleportManager plugin;
	
	public TeleportMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StringValues.TP_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StringValues.TP_FREE))
            	{
            		String fromName = in.readUTF();
            		String toName = in.readUTF();
            		Teleport.Type type = Teleport.Type.valueOf(in.readUTF());
            		Player from = plugin.getServer().getPlayer(fromName);
            		if(from != null)
            		{
            			if(!from.hasPermission(StringValues.PERM_BYPASS_TELEPORT))
            			{
            				double price = plugin.getYamlHandler().get().getDouble("CostPerTeleportRequest", 0.0);
                    		if(price > 0.0)
                    		{
                    			if(plugin.getEco() != null)
                        		{
                    				if(!plugin.getEco().has(fromName, price))
                    				{
                    					from.sendMessage(
                                				plugin.getYamlHandler().getL().getString("Economy.NoEnoughBalance"));
                    					return;
                    				}
                    				if(!plugin.getEco().withdrawPlayer(fromName, price).transactionSuccess())
                    				{
                    					return;
                    				}
                    				if(plugin.getAdvanceEconomyHandler() != null)
                            		{
                    					String comment = null;
                    					if(type == Teleport.Type.TPTO)
                    					{
                    						comment = plugin.getYamlHandler().getL().getString("Economy.TComment")
                                					.replace("%from%", fromName)
                                					.replace("%to%", toName);
                    					} else
                    					{
                    						comment = plugin.getYamlHandler().getL().getString("Economy.TComment")
                        					.replace("%from%", toName)
                        					.replace("%to%", fromName);
                    					}
                            			plugin.getAdvanceEconomyHandler().EconomyLogger(
                            					Utility.convertNameToUUID(fromName).toString(),
                            					fromName,
                            					plugin.getYamlHandler().getL().getString("Economy.TUUID"),
                            					plugin.getYamlHandler().getL().getString("Economy.TName"),
                            					plugin.getYamlHandler().getL().getString("Economy.TORDERER"),
                            					price,
                            					"TAKEN",
                            					comment);
                            			plugin.getAdvanceEconomyHandler().TrendLogger(from, -price);
                            		}
                        		}
                    		}
            			}
            		}
            		if(type == Teleport.Type.TPTO)
            		{
            			Teleport teleport = new Teleport(
            					Utility.convertNameToUUID(fromName), fromName, 
            					Utility.convertNameToUUID(toName), toName,
            					type);
            			plugin.getTeleportHandler().tpSendInvite(player, teleport);
            		} else if(type == Teleport.Type.TPHERE)
            		{
            			Teleport teleport = new Teleport(
            					Utility.convertNameToUUID(fromName), fromName, 
            					Utility.convertNameToUUID(toName), toName,
            					type);
            			plugin.getTeleportHandler().tpSendInvite(player, teleport);
            		}
            	} else if(task.equals(StringValues.TP_OCCUPIED))
            	{
            		String fromName = in.readUTF();
            		plugin.getServer().getPlayer(fromName).sendMessage(
            				plugin.getYamlHandler().getL().getString("CmdTp.HasAlreadyRequest"));
            		return;
            	} else if(task.equals(StringValues.TP_PLAYERTOPLAYER))
            	{
            		String sender = in.readUTF();
            		String target = in.readUTF();
            		new BukkitRunnable()
					{
            			int i = 0;
						@Override
						public void run()
						{
							if(plugin.getServer().getPlayer(sender) != null)
							{
								if(plugin.getServer().getPlayer(sender).isOnline())
								{
									Player senders = plugin.getServer().getPlayer(sender);
									Player targets = plugin.getServer().getPlayer(target);
									if(targets != null)
									{
										senders.teleport(targets.getLocation());
										senders.sendMessage(plugin.getYamlHandler().getL().getString("CmdTp.PlayerTeleport")
												.replace("%fromplayer%", senders.getName())
												.replace("%toplayer%", targets.getName()));
										targets.sendMessage(plugin.getYamlHandler().getL().getString("CmdTp.PlayerTeleport")
												.replace("%fromplayer%", senders.getName())
												.replace("%toplayer%", targets.getName()));
										cancel();
									}
								}
							}
							i++;
							if(i >= 100)
							{
								cancel();
							}
						}
					}.runTaskTimer(plugin, 1L, 2L);
            	    return;
            	} else if(task.equals(StringValues.TP_SERVERQUITMESSAGE))
            	{
            		String playerName = in.readUTF();
            		String otherPlayerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).sendMessage(
            				plugin.getYamlHandler().getL().getString("CmdTp.ServerQuitMessage")
            				.replace("%player%", otherPlayerName));
            		return;
            	} else if(task.equals(StringValues.TP_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		String serverName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	new BukkitRunnable()
					{
            			int i = 0;
						@Override
						public void run()
						{
							if(plugin.getServer().getPlayer(playerName) != null)
							{
								if(plugin.getServer().getPlayer(playerName).isOnline())
								{
									Player player = plugin.getServer().getPlayer(playerName);
									if(Bukkit.getWorld(worldName) == null)
									{
										cancel();
									}
									player.teleport(new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch));
									player.sendMessage(plugin.getYamlHandler().getL().getString("CmdTp.PositionTeleport")
											.replace("%server%", serverName)
											.replace("%world%", worldName)
											.replace("%coords%", x+" "+y+" "+z+" | "+yaw+" "+pitch));
									cancel();
								}
							}
							i++;
							if(i >= 100)
							{
								cancel();
							}
						}
					}.runTaskTimer(plugin, 1L, 2L);
            		return;
            	} else if(task.equals(StringValues.TP_FORBIDDENSERVER))
            	{
            		String playerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).sendMessage(
            				plugin.getYamlHandler().getL().getString("CmdTp.ForbiddenServer"));
            		return;
            	} else if(task.equals(StringValues.TP_FORBIDDENWORLD))
            	{
            		String playerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).sendMessage(
            				plugin.getYamlHandler().getL().getString("CmdTp.ForbiddenWorld"));
            		return;
            	} else if(task.equals(StringValues.TP_TOGGLED))
            	{
            		String playerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).sendMessage(
            				plugin.getYamlHandler().getL().getString("CmdTp.PlayerIsToggle"));
            		return;
            	}
            } catch (IOException e) 
            {
    			e.printStackTrace();
    		}
		}
	}
}
