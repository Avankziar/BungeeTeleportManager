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

public class WarpHandler
{
private BTM plugin;
	
	public WarpHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.WARP_PLAYERTOPOSITION))
        {
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	String warpName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	int delay = in.readInt();
        	String pterc = in.readUTF();
        	String ptegc = in.readUTF();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.WARP);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	teleportPlayer(playerName, delay, warpName, location, pterc, ptegc);
        	return;
        }
        return;
	}
	
	public void teleportPlayer(String playerName, int delay, String warpName, final ServerLocation location, String pterc, String ptegc)
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
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
			CompletableFuture<Result> r = null;
			if(!player.getCurrentServer().get().getServerInfo().getName().equals(location.getServer()))
			{
				r = player.createConnectionRequest(server.get()).connect();
			}
			sendPluginMessage(player, server.get(), r, warpName, location, pterc, ptegc);
			return;
		}).delay(delay, TimeUnit.MILLISECONDS).schedule();
	}
	
	private void sendPluginMessage(Player player, RegisteredServer server, CompletableFuture<Result> result,
			String warpName, ServerLocation location, String pterc, String ptegc)
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
	        	out.writeUTF(StaticValues.WARP_PLAYERTOPOSITION);
				out.writeUTF(player.getUsername());
				out.writeUTF(warpName);
				out.writeUTF(location.getWorldName());
				out.writeDouble(location.getX());
				out.writeDouble(location.getY());
				out.writeDouble(location.getZ());
				out.writeFloat(location.getYaw());
				out.writeFloat(location.getPitch());
				out.writeUTF(pterc);
				out.writeUTF(ptegc);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        server.sendPluginMessage(BTM.getMCID(StaticValues.WARP_TOSPIGOT), streamout.toByteArray());
		}).repeat(20, TimeUnit.MILLISECONDS).schedule();
	}
}