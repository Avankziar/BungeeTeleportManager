package me.avankziar.btm.spigot.manager.tpandtpa;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.bungee.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.Teleport;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.listener.PlayerOnCooldownListener;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;

public class TeleportMessageListener implements PluginMessageListener
{
	private BTM plugin;
	
	public TeleportMessageListener(BTM plugin)
	{
		this.plugin = plugin;
	}

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
            	BTM.logger.info(task);
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
		            					&& plugin.getEco() != null)
		            			{
		            				double price = cfgh.getCostUse(Mechanics.TPA_ONLY);
		                    		if(price > 0.0)
		                    		{
		                    			Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
		        								plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
		        						if(main == null || main.getBalance() < price)
		        						{
		        							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
		        							return;
		        						}
		        						String category = plugin.getYamlHandler().getLang().getString("Economy.TCategory");
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
		        						EconomyAction ea = plugin.getEco().withdraw(main, price, 
	        									OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		        						if(!ea.isSuccess())
		        						{
		        							player.spigot().sendMessage(ChatApiOld.tctl(ea.getDefaultErrorMessage()));
		        							return;
		        						}
		                    		}
		            			}
		            		}
		            		if(isToggled)
		            		{
		            			from.spigot().sendMessage(
		                				ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerHasToggled")));
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
            		plugin.getServer().getPlayer(fromName).spigot().sendMessage(
            				ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.HasAlreadyRequest")));
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
											senders.spigot().sendMessage(
													ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
													.replace("%playerfrom%", senders.getName())
													.replace("%playerto%", targets.getName())));
										}										
										if(targets.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_SILENT))
										{
											targets.spigot().sendMessage(
													ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
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
												senders.spigot().sendMessage(
														ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.SilentPlayerTeleport")
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
            		plugin.getServer().getPlayer(playerName).spigot().sendMessage(
            				ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.ServerQuitMessage")
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
                		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
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
											player.spigot().sendMessage(ChatApiOld.tctl(
													plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
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
										
										player.spigot().sendMessage(ChatApiOld.tctl(
												plugin.getYamlHandler().getLang().getString("CmdTp.PositionTeleport")
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
            		plugin.getServer().getPlayer(playerName).spigot().sendMessage(
            				ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenServer")));
            		return;
            	} else if(task.equals(StaticValues.TP_FORBIDDENWORLD))
            	{
            		String playerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).spigot().sendMessage(
            				ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenWorld")));
            		return;
            	} else if(task.equals(StaticValues.TP_TOGGLED))
            	{
            		String playerName = in.readUTF();
            		plugin.getServer().getPlayer(playerName).spigot().sendMessage(
            				ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerIsToggle")));
            		return;
            	}
            } catch (IOException e) 
            {
    			e.printStackTrace();
    		}
		}
	}
}