package me.avankziar.btm.velocity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future.State;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder.Result;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.avankziar.btm.general.assistance.ChatApi;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.velocity.BTM;

public class RandomTeleportHandler
{
	private BTM plugin;
	
	public RandomTeleportHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.RANDOMTELEPORT_PLAYERTOPOSITION))
        {
        	ServerLocation point2 = null;
        	int radius = 0;
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	boolean isArea = in.readBoolean();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	if(isArea)
        	{
        		double x2 = in.readDouble();
            	double y2 = in.readDouble();
            	double z2 = in.readDouble();
        		point2 = new ServerLocation(server, worldName, x2, y2, z2);
        	} else
        	{
        		radius = in.readInt();
        	}
        	int delay = in.readInt();
        	String rtpPath = in.readUTF();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.RANDOMTELEPORT);
        	ServerLocation point1 = new ServerLocation(server, worldName, x, y, z);
        	teleportPlayer(playerName, delay, point1, point2, radius, isArea, rtpPath);
        	return;
        }
        return;
	}
	
	public void teleportPlayer(String playerName, int delay, ServerLocation point1, ServerLocation point2, int radius, boolean isArea,
			String rtpPath)
	{
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
			Optional<Player> opplayer = plugin.getServer().getPlayer(playerName);
			if(opplayer.isEmpty() || point1 == null)
			{
				return;
			}
			Player player = opplayer.get();
			Optional<RegisteredServer> server = plugin.getServer().getServer(point1.getServer());
			if(server.isEmpty())
			{
				player.sendMessage(ChatApi.tl("<dark_red>Error</dark_red> <white>3</white> <red>- Server %server% is unknown!</red>"
						.replace("%server%", point1.getServer())));
				return;
			}
			if(isArea == true)
			{
				if(point2 == null)
				{
					return;
				}
			}
			CompletableFuture<Result> r = null;
			if(!player.getCurrentServer().get().getServerInfo().getName().equals(point1.getServer()))
			{
				r = player.createConnectionRequest(server.get()).connect();
			}
			sendPluginMessage(player, server.get(), r, point1, point2, radius, isArea, rtpPath);
		}).delay(delay, TimeUnit.MILLISECONDS).schedule();
	}
	
	private void sendPluginMessage(Player player, RegisteredServer server, CompletableFuture<Result> result, 
			ServerLocation point1, ServerLocation point2, int radius, boolean isArea, String rtpPath)
	{
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
			if(result != null)
			{
				if(result.state() == State.RUNNING)
				{
					return;
				} else if(result.state() == State.CANCELLED || result.state() == State.FAILED)
				{
					task.cancel();
					return;
				}
			}
			task.cancel();
			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
	        	out.writeUTF(StaticValues.RANDOMTELEPORT_PLAYERTOPOSITION);
				out.writeUTF(player.getUsername());
				out.writeBoolean(isArea);
				out.writeUTF(point1.getWorldName());
				out.writeDouble(point1.getX());
				out.writeDouble(point1.getY());
				out.writeDouble(point1.getZ());
				if(isArea == true)
				{
					out.writeDouble(point2.getX());
					out.writeDouble(point2.getY());
					out.writeDouble(point2.getZ());
				} else
				{
					out.writeInt(radius);
				}
				out.writeUTF(rtpPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        server.sendPluginMessage(BTM.getMCID(StaticValues.RANDOMTELEPORT_TOSPIGOT), streamout.toByteArray());
		}).repeat(20, TimeUnit.MILLISECONDS).schedule();
	}
}