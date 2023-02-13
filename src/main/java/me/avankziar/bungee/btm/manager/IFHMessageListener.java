package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.bungee.btm.assistance.ChatApi;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class IFHMessageListener implements Listener	
{
	private BungeeTeleportManager plugin;
	
	public IFHMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onTeleportMessage(PluginMessageEvent event) throws IOException
	{
		if (event.isCancelled()) 
		{
            return;
        }
        if (!( event.getSender() instanceof Server))
        {
        	return;
        }
        if (!event.getTag().equalsIgnoreCase(StaticValues.IFH_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.IFH_PLAYERTOPOSITION))
        {
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	String server = in.readUTF();
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
        	boolean createBack = in.readBoolean();
        	if(createBack)
        	{
        		BackHandler.getBack(in, uuid, playerName, Mechanics.CUSTOM);
        	}
        	teleportPlayerToDestination(playerName, new ServerLocation(server, worldName, x, y, z, yaw, pitch), post, errormessage);
        }
        return;
	}
	
	private void teleportPlayerToDestination(String playerName, ServerLocation location, ArrayList<String> post, String errormessage)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		teleportPlayer(player, location, post, errormessage); //Back wurde schon gemacht
	}
	
	private void teleportPlayer(ProxiedPlayer player, ServerLocation location, ArrayList<String> post, String errormessage)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!plugin.getProxy().getServers().containsKey(location.getServer()))
		{
			player.sendMessage(ChatApi.tctl(errormessage));
			return;
		}
		
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(player == null || location == null || location.getServer() == null
						|| player.getServer() == null || player.getServer().getInfo() == null 
						|| player.getServer().getInfo().getName() == null)
				{
					if(player != null)
					{
						player.sendMessage(ChatApi.tctl(errormessage));
					}
					return;
				}
				if(!player.getServer().getInfo().getName().equals(location.getServer()))
				{
					if(plugin.getProxy().getServerInfo(location.getServer()) == null)
					{
						player.sendMessage(ChatApi.tctl(errormessage));
						return;
					}
					player.connect(plugin.getProxy().getServerInfo(location.getServer()));
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StaticValues.IFH_PLAYERTOPOSITION);
					out.writeUTF(player.getName());
					out.writeUTF(location.getWorldName());
					out.writeDouble(location.getX());
					out.writeDouble(location.getY());
					out.writeDouble(location.getZ());
					out.writeFloat(location.getYaw());
					out.writeFloat(location.getPitch());
					out.writeInt(post.size());
					for(String s : post)
					{
						out.writeUTF(s);
					}
					out.writeUTF(errormessage);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(location.getServer()).sendData(StaticValues.IFH_TOSPIGOT, streamout.toByteArray());
				return;
			}
		}, 0, TimeUnit.MILLISECONDS);
	}
}
