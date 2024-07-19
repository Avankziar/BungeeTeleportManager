package me.avankziar.btm.spigot.ifh;

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
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.manager.back.BackHandler;
import me.avankziar.ifh.spigot.teleport.Teleport;

public class TeleportProvider implements Teleport, PluginMessageListener
{
	private BTM plugin;
	private static boolean isBack = true;
	
	public TeleportProvider(BTM plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean isBackAsDefaultActive()
	{
		return isBack;
	}

	@Override
	public void setBackAsDefault(boolean active)
	{
		isBack = active;
	}

	@Override
	public boolean teleport(Player player, String server, String worldname, double x, double y, double z, float yaw, float pitch)
	{
		teleport(player, server, worldname, x, y, z, yaw, pitch, new ArrayList<>(), new ArrayList<>(), "", isBack);
		return false;
	}

	@Override
	public boolean teleport(Player player, String server, String worldname, double x, double y, double z, float yaw, float pitch, 
			ArrayList<String> preTeleportMessage)
	{
		return teleport(player, server, worldname, x, y, z, yaw, pitch, preTeleportMessage, new ArrayList<>(), "", isBack);
	}

	@Override
	public boolean teleport(Player player, String server, String worldname, double x, double y, double z, float yaw, float pitch, 
			ArrayList<String> preTeleportMessage, ArrayList<String> postTeleportMessage)
	{
		return teleport(player, server, worldname, x, y, z, yaw, pitch, preTeleportMessage, postTeleportMessage, "", isBack);
	}
	
	@Override
	public boolean teleport(Player player, String server, String worldname, double x, double y, double z, float yaw, float pitch, 
			ArrayList<String> preTeleportMessage, ArrayList<String> postTeleportMessage, String errormessage)
	{
		return teleport(player, server, worldname, x, y, z, yaw, pitch, preTeleportMessage, postTeleportMessage, errormessage, isBack);
	}

	@Override
	public boolean teleport(Player player, String server, String worldname, double x, double y, double z, float yaw, float pitch, 
			ArrayList<String> preTeleportMessage, ArrayList<String> postTeleportMessage, String errormessage,
			boolean createBack)
	{
		sendPlayerTo(player, server, worldname, x, y, z, yaw, pitch, preTeleportMessage, postTeleportMessage,
				errormessage, createBack);
		return true;
	}
	
	private void sendPlayerTo(Player player, String server, String worldname, double x, double y, double z, float yaw, float pitch,
			ArrayList<String> preTeleportMessage, ArrayList<String> postTeleportMessage,
			String errormessage, boolean createBack)
	{
		if(!preTeleportMessage.isEmpty())
		{
			for(String s : preTeleportMessage)
			player.spigot().sendMessage(ChatApiOld.tctl(s));
		}	
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(server.equals(cfgh.getServer()) && player != null)
		{
			if(createBack)
			{
				BackHandler bh = new BackHandler(plugin);
				bh.sendBackObject(player, bh.getNewBack(player), false);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if(Bukkit.getWorld(worldname) == null)
					{
						if(errormessage != null)
						{
							player.spigot().sendMessage(ChatApiOld.tctl(errormessage));
						}
						return;
					}
					player.teleport(new Location(Bukkit.getWorld(worldname), x, y, z, yaw, pitch));
					if(!postTeleportMessage.isEmpty())
					{
						for(String s : postTeleportMessage)
						player.spigot().sendMessage(ChatApiOld.tctl(s));
					}
				}
			}.runTaskLater(plugin, 1);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.IFH_PLAYERTOPOSITION);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(server);
				out.writeUTF(worldname);
				out.writeDouble(x);
				out.writeDouble(y);
				out.writeDouble(z);
				out.writeFloat(yaw);
				out.writeFloat(pitch);
				out.writeInt(postTeleportMessage.size());
				for(String s : postTeleportMessage)
				{
					out.writeUTF(s);
				}
				out.writeUTF(errormessage != null ? errormessage : "");
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
                	String errormessage = in.readUTF();
                	if(Bukkit.getWorld(worldName) == null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(errormessage));
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
											player.spigot().sendMessage(ChatApiOld.tctl(errormessage));
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
													player.spigot().sendMessage(ChatApiOld.tctl(errormessage));
												}
											}
										}.runTask(plugin);
										if(!post.isEmpty())
										{
											for(String s : post)
											{
												player.spigot().sendMessage(ChatApiOld.tctl(s));
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
