package main.java.me.avankziar.spigot.btm.ifh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.ifh.spigot.teleport.Teleport;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.manager.back.BackHandler;

public class TeleportProvider implements Teleport, PluginMessageListener
{
	private BungeeTeleportManager plugin;
	private static boolean isBack = true;
	private static boolean isEffect = true;
	private static ArrayList<PotionEffect> effects = new ArrayList<>();
	
	static
	{
		effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 1));
		effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 1));
	}
	
	public TeleportProvider(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean isBackAsDefaultActive()
	{
		return isBack;
	}

	@Override
	public boolean isGiveEffects()
	{
		return isEffect;
	}

	@Override
	public void setBackAsDefault(boolean active)
	{
		isBack = active;
	}

	@Override
	public void setGiveEffects(boolean active, PotionEffect... potionEffect)
	{
		isEffect = active;
		effects.clear();
		for(PotionEffect pe : potionEffect)
		{
			effects.add(pe);
		}
	}

	@Override
	public boolean teleport(Player player, String server, Location location)
	{
		teleport(player, server, location, new ArrayList<>(), new ArrayList<>(), isBack);
		return false;
	}

	@Override
	public boolean teleport(Player player, String server, Location location, 
			ArrayList<String> preTeleportMessage)
	{
		teleport(player, server, location, preTeleportMessage, new ArrayList<>(), isBack);
		return false;
	}

	@Override
	public boolean teleport(Player player, String server, Location location, 
			ArrayList<String> preTeleportMessage, ArrayList<String> postTeleportMessage)
	{
		teleport(player, server, location, preTeleportMessage, postTeleportMessage, isBack);
		return false;
	}

	@Override
	public boolean teleport(Player player, String server, Location location, 
			ArrayList<String> preTeleportMessage, ArrayList<String> postTeleportMessage,
			boolean createBack)
	{
		sendPlayerTo(player, server, location, createBack, preTeleportMessage, postTeleportMessage);
		return true;
	}
	
	private void sendPlayerTo(Player player, String server, Location loc, boolean createBack,
			ArrayList<String> preTeleportMessage, ArrayList<String> postTeleportMessage)
	{
		if(!preTeleportMessage.isEmpty())
		{
			for(String s : preTeleportMessage)
			player.sendMessage(ChatApi.tl(s));
		}
		if(isEffect)
		{
			player.addPotionEffects(effects);
		}		
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(server.equals(cfgh.getServer()) && player != null)
		{
			if(isBack)
			{
				BackHandler bh = new BackHandler(plugin);
				bh.sendBackObject(player, bh.getNewBack(player));
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(loc);
					if(!postTeleportMessage.isEmpty())
					{
						for(String s : postTeleportMessage)
						player.sendMessage(ChatApi.tl(s));
					}
				}
			}.runTaskLater(plugin, 1);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.IFH_PLAYERTOPOSITION);
				out.writeUTF(player.getName());
				out.writeUTF(server);
				out.writeUTF(loc.getWorld().getName());
				out.writeDouble(loc.getX());
				out.writeDouble(loc.getY());
				out.writeDouble(loc.getZ());
				out.writeFloat(loc.getYaw());
				out.writeFloat(loc.getPitch());
				out.writeInt(postTeleportMessage.size());
				for(String s : postTeleportMessage)
				{
					out.writeUTF(s);
				}
				out.writeBoolean(createBack);
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) 
	        {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.IFH_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.IFH_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.IFH_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	int size = in.readInt();
                	ArrayList<String> post = new ArrayList<>();
                	for(int i = 0; i < size; i++)
                	{
                		String s = in.readUTF();
                		post.add(s);
                	}
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
									if(loc != null)
									{
										Player player = plugin.getServer().getPlayer(playerName);
										if(Bukkit.getWorld(worldName) == null)
										{
											player.sendMessage(
													ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
															.replace("%world%", worldName)));
											cancel();
											return;
										}
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												try
												{
													player.teleport(loc);
												} catch(NullPointerException e)
												{
													player.sendMessage(ChatApi.tl("Error! See Console!"));
												}
											}
										}.runTask(plugin);
										if(!post.isEmpty())
										{
											for(String s : post)
											{
												player.sendMessage(ChatApi.tl(s));
											}
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
            	}
            } catch (IOException e) 
            {
    			return;
    		} catch(NullPointerException e)
            {
    			return;
            }
		}
	}
}
