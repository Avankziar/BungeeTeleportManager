package main.java.me.avankziar.spigot.btm.manager.tpandtpa;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.bungee.btm.assistance.ChatApi;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.listener.PlayerOnCooldownListener;

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
		if(channel.equals(StaticValues.TP_TOSPIGOT)) 
		{
			ConfigHandler cfgh = new ConfigHandler(plugin);
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	BungeeTeleportManager.log.info(task);
            	if(task.equals(StaticValues.TP_FREE))
            	{
            		String fromName = in.readUTF();
            		String toName = in.readUTF();
            		Teleport.Type type = Teleport.Type.valueOf(in.readUTF());
            		boolean isToggled = in.readBoolean();
            		new BukkitRunnable()
					{
						@Override
						public void run()
						{
		            		Player from = plugin.getServer().getPlayer(fromName);
		            		if(from != null)
		            		{
		            			if(!from.hasPermission(StaticValues.BYPASS_COST+Mechanics.TPA_ONLY.getLower())
		            					&& plugin.getEco() != null && cfgh.useVault())
		            			{
		            				double price = cfgh.getCostUse(Mechanics.TPA_ONLY);
		                    		if(price > 0.0)
		                    		{
		                    			if(!plugin.getEco().has(fromName, price))
	                    				{
	                    					from.sendMessage(
	                    							ChatApi.tl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
	                    					return;
	                    				}
	                    				if(!plugin.getEco().withdrawPlayer(fromName, price).transactionSuccess())
	                    				{
	                    					return;
	                    				}
	                    				if(plugin.getAdvancedEconomyHandler() != null)
	                            		{
	                    					String comment = null;
	                    					if(type == Teleport.Type.TPTO)
	                    					{
	                    						comment = plugin.getYamlHandler().getLang().getString("Economy.TComment")
	                                					.replace("%from%", fromName)
	                                					.replace("%to%", toName);
	                    					} else
	                    					{
	                    						comment = plugin.getYamlHandler().getLang().getString("Economy.TComment")
	                        					.replace("%from%", toName)
	                        					.replace("%to%", fromName);
	                    					}
	                            			plugin.getAdvancedEconomyHandler().EconomyLogger(
	                            					Utility.convertNameToUUID(fromName).toString(),
	                            					fromName,
	                            					plugin.getYamlHandler().getLang().getString("Economy.TUUID"),
	                            					plugin.getYamlHandler().getLang().getString("Economy.TName"),
	                            					plugin.getYamlHandler().getLang().getString("Economy.TORDERER"),
	                            					price,
	                            					"TAKEN",
	                            					comment);
	                            			plugin.getAdvancedEconomyHandler().TrendLogger(from, -price);
	                            		}
	                    				if(cfgh.notifyPlayerAfterWithdraw(Mechanics.TPA))
	                    				{
	                    					player.sendMessage(
	                                				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.NotifyAfterWithDraw")
	                                						.replace("%amount%", String.valueOf(price))
	                                						.replace("%currency%", plugin.getEco().currencyNamePlural())));
	                    				}
		                    		}
		            			}
		            		}
		            		if(isToggled)
		            		{
		            			from.sendMessage(
		                				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerHasToggled")));
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
		            		return;
						}
					}.runTaskAsynchronously(plugin);
            	} else if(task.equals(StaticValues.TP_OCCUPIED))
            	{
            		String fromName = in.readUTF();
            		plugin.getServer().getPlayer(fromName).sendMessage(
            				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.HasAlreadyRequest")));
            		return;
            	} else if(task.equals(StaticValues.TP_PLAYERTOPLAYER))
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
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												plugin.getUtility().givesEffect(player, Mechanics.TELEPORT, false, false);
												senders.teleport(targets.getLocation());
											}
										}.runTask(plugin);
										
										if(PlayerOnCooldownListener.playerCooldownlist.containsKey(senders))
										{
											PlayerOnCooldownListener.playerCooldownlist.replace(senders, System.currentTimeMillis());
										}
										if(senders.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_SILENT))
										{
											senders.sendMessage(
													ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
													.replace("%playerfrom%", senders.getName())
													.replace("%playerto%", targets.getName())));
										}										
										if(targets.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_SILENT))
										{
											targets.sendMessage(
													ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
													.replace("%playerfrom%", senders.getName())
													.replace("%playerto%", targets.getName())));
										}
										cancel();
										return;
									}
								}
							}
							i++;
							if(i >= 100)
							{
								cancel();
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 1L, 2L);
            	    return;
            	} else if(task.equals(StaticValues.TP_SILENTPLAYERTOPLAYER))
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
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												if(plugin.getYamlHandler().getConfig().getBoolean("SilentTp.DoVanish", true))
												{
													if(plugin.getVanish() != null)
													{
														if(!plugin.getVanish().isInvisible(senders))
														{
															plugin.getVanish().hidePlayer(senders);
														}
													} else
													{
														Bukkit.dispatchCommand(senders, 
																plugin.getYamlHandler().getConfig().getString("SilentTp.VanishCommand", "vanish"));
													}
												}
												senders.teleport(targets);
												senders.sendMessage(
														ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.SilentPlayerTeleport")
														.replace("%playerto%", targets.getName())));
											}
										}.runTaskLater(plugin, 1);
										cancel();
										return;
									}
								}
							}
							i++;
							if(i >= 100)
							{
								cancel();
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 1L, 2L);
            	    return;
            	} else if(task.equals(StaticValues.TP_SERVERQUITMESSAGE))
            	{
            		String playerName = in.readUTF();
            		String otherPlayerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).sendMessage(
            				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ServerQuitMessage")
            				.replace("%player%", otherPlayerName)));
            		return;
            	} else if(task.equals(StaticValues.TP_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		String serverName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	if(Bukkit.getWorld(worldName) == null)
					{
						player.sendMessage(
								ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
										.replace("%world%", worldName)));
						return;
					}
                	new BukkitRunnable()
					{
            			int i = 0;
            			Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
						@Override
						public void run()
						{
							if(plugin.getServer().getPlayer(playerName) != null)
							{
								if(plugin.getServer().getPlayer(playerName).isOnline())
								{
									if(loc != null) //Eventuell prüfen ob loc sicher ist.
									{
										Player player = plugin.getServer().getPlayer(playerName);
										if(PlayerOnCooldownListener.playerCooldownlist.containsKey(player))
										{
											PlayerOnCooldownListener.playerCooldownlist.replace(player, System.currentTimeMillis());
										}
										if(Bukkit.getWorld(worldName) == null)
										{
											player.sendMessage(
													ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
															.replace("%world%", worldName)));
											cancel();
										}
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												plugin.getUtility().givesEffect(player, Mechanics.TELEPORT, false, false);
												player.teleport(loc);
											}
										}.runTask(plugin);
										
										player.sendMessage(
												ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.PositionTeleport")
												.replace("%server%", serverName)
												.replace("%world%", worldName)
												.replace("%coords%", x+" "+y+" "+z+" | "+yaw+" "+pitch)));
										cancel();
										return;
									}
								}
							}
							i++;
							if(i >= 100)
							{
								cancel();
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 1L, 2L);
            		return;
            	} else if(task.equals(StaticValues.TP_FORBIDDENSERVER))
            	{
            		String playerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).sendMessage(
            				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenServer")));
            		return;
            	} else if(task.equals(StaticValues.TP_FORBIDDENWORLD))
            	{
            		String playerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).sendMessage(
            				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenWorld")));
            		return;
            	} else if(task.equals(StaticValues.TP_TOGGLED))
            	{
            		String playerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).sendMessage(
            				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerIsToggle")));
            		return;
            	}
            } catch (IOException e) 
            {
    			e.printStackTrace();
    		}
		}
	}
}