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
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.avankziar.btm.general.assistance.ChatApi;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.velocity.BTM;
import me.avankziar.btm.velocity.listener.PluginMessageListener;

public class PortalHandler
{
	private BTM plugin;
	
	public PortalHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.PORTAL_PLAYERTOPOSITION))
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
        	String portalname = in.readUTF();
        	boolean lava = in.readBoolean();
        	String pterc = in.readUTF();
        	String ptegc = in.readUTF();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.PORTAL);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	teleportPlayer(playerName, location, portalname, lava, pterc, ptegc);
        	return;
        } else if(task.equals(StaticValues.PORTAL_UPDATE))
        {
        	int mysqlID = in.readInt();
        	String additional = in.readUTF();
        	sendUpdate(mysqlID, additional);
        }
        return;
	}
	
	public void teleportPlayer(String playerName, ServerLocation location, String portalname, boolean lava, String pterc, String ptegc)
	{
		Optional<Player> opplayer = plugin.getServer().getPlayer(playerName);
		if(opplayer.isEmpty())
		{
			return;
		}
		Player player = opplayer.get();
		if(location == null)
		{
			player.sendMessage(ChatApi.tl("<dark_red>Error</dark_red> <white>1</white> <red>- Serverlocation is unknown!</red>"));
			return;
		}
		if(!PluginMessageListener.containsServer(location.getServer()))
		{
			player.sendMessage(ChatApi.tl("<dark_red>Error</dark_red> <white>2</white> <red>- Server %server% is unknown!</red>"
					.replace("%server%", location.getServer())));
			return;
		}
		Optional<RegisteredServer> server = plugin.getServer().getServer(location.getServer());
		if(server.isEmpty())
		{
			player.sendMessage(ChatApi.tl("<dark_red>Error</dark_red> <white>3</white> <red>- Server %server% is unknown!</red>"
					.replace("%server%", location.getServer())));
			return;
		}
		Optional<ServerConnection> playerserver = player.getCurrentServer();
		if(playerserver.isEmpty())
		{
			player.sendMessage(ChatApi.tl("<dark_red>Error</dark_red> <white>4</white> <red>- Server where the Player is, is unknown!</red>"));
			return;
		}
		plugin.getServer().getScheduler().buildTask(plugin, () ->
		{
			CompletableFuture<Result> r = null;
			if(!player.getCurrentServer().get().getServerInfo().getName().equals(location.getServer()))
			{
				r = player.createConnectionRequest(server.get()).connect();
			}
			sendPluginMessage(player, server.get(), r, location, portalname, lava, pterc, ptegc);
		}).delay(0, TimeUnit.MILLISECONDS).schedule();
	}
	
	private void sendPluginMessage(Player player, RegisteredServer server, CompletableFuture<Result> result,
			ServerLocation location, String portalname, boolean lava, String pterc, String ptegc)
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
	        	out.writeUTF(StaticValues.PORTAL_PLAYERTOPOSITION);
				out.writeUTF(player.getUsername());
				out.writeUTF(location.getWorldName());
				out.writeDouble(location.getX());
				out.writeDouble(location.getY());
				out.writeDouble(location.getZ());
				out.writeFloat(location.getYaw());
				out.writeFloat(location.getPitch());
				out.writeUTF(portalname);
				out.writeBoolean(lava);
				out.writeUTF(pterc);
				out.writeUTF(ptegc);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        server.sendPluginMessage(BTM.getMCID(StaticValues.PORTAL_TOSPIGOT), streamout.toByteArray());
		}).repeat(20, TimeUnit.MILLISECONDS).schedule();
	}
	
	public void sendUpdate(int mysqlID, String additional)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
        	out.writeUTF(StaticValues.PORTAL_UPDATE);
			out.writeInt(mysqlID);
			out.writeUTF(additional);
		} catch (IOException e) 
        {
			e.printStackTrace();
		}
        for(RegisteredServer si : plugin.getServer().getAllServers())
        {
        	si.sendPluginMessage(BTM.getMCID(StaticValues.PORTAL_TOSPIGOT), streamout.toByteArray());
        }
	}
}