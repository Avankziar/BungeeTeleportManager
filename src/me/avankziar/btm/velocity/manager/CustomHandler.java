package me.avankziar.btm.velocity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
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
import me.avankziar.btm.general.object.Teleport;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.velocity.BTM;
import me.avankziar.btm.velocity.listener.PluginMessageListener;

public class CustomHandler
{
	private BTM plugin;
	
	public CustomHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        if(task.equals(StaticValues.CUSTOM_PLAYERTOPLAYER))
        {
        	String fromUUID = in.readUTF();
        	String fromName = in.readUTF();
        	String toUUID = in.readUTF();
        	String toName = in.readUTF();
        	String type = in.readUTF();
        	String errormessage = in.readUTF();
        	int delayed = in.readInt();
        	boolean overrideBack = in.readBoolean();
        	BackHandler.getBackWithChoosing(in, fromUUID, fromName, Mechanics.CUSTOM, overrideBack);
        	preTeleportPlayerToPlayerForceUse(
        			new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)),
        			errormessage, delayed);
        	return;
        } else if(task.equals(StaticValues.CUSTOM_PLAYERTOPOSITION))
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
        	String errorServerNotFound = in.readUTF();
        	int delayed = in.readInt();
        	boolean messagenull = in.readBoolean();
        	String message = null;
        	if(!messagenull)
        	{
        		message = in.readUTF();
        	}
        	boolean overrideBack = in.readBoolean();
        	BackHandler.getBackWithChoosing(in, uuid, playerName, Mechanics.CUSTOM, overrideBack);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	teleportPlayerToPosition(playerName, location, errorServerNotFound, message, delayed);
        	return;
        }
        return;
	}
	
	public void preTeleportPlayerToPlayerForceUse(Teleport teleport, String errormessage, int delay)
	{
		Optional<Player> from = plugin.getServer().getPlayer(teleport.getFromName());
		Optional<Player> to = plugin.getServer().getPlayer(teleport.getToName());
		if(from.isEmpty())
		{
			return;
		}
		if(to.isEmpty())
		{
			from.get().sendMessage(ChatApi.tl(errormessage));
			return;
		}
		if(teleport.getType() == Teleport.Type.TPTO)
		{
			teleportPlayer(from.get(), to.get(), delay);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			teleportPlayer(to.get(), from.get(), delay);
		}
	}
	
	public void teleportPlayer(Player sender, Player target, int delay)
	{
		plugin.getServer().getScheduler().buildTask(plugin, () ->
		{
			if(sender == null || (sender != null && !sender.isActive()))
			{
				return;
			}
			if(target == null || (target != null && !target.isActive()))
			{
				return;
			}
			CompletableFuture<Result> r = null;
			if(!sender.getCurrentServer().get().getServerInfo().getName().equals(target.getCurrentServer().get().getServerInfo().getName()))
			{
				Optional<ServerConnection> server = target.getCurrentServer();
				if(server.isEmpty())
				{
					return;
				}
				r = sender.createConnectionRequest(server.get().getServer()).connect();
			}
			sendPluginMessage(sender, target, r);
		}).delay(delay, TimeUnit.MILLISECONDS).schedule();
	}
	
	private void sendPluginMessage(Player sender, Player target, CompletableFuture<Result> result)
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
				out.writeUTF(StaticValues.CUSTOM_PLAYERTOPLAYER);
				out.writeUTF(sender.getUsername());
				out.writeUTF(target.getUsername());
			} catch (IOException e) {
				e.printStackTrace();
			}
		    target.getCurrentServer().ifPresent(y -> y.sendPluginMessage(BTM.getMCID(StaticValues.CUSTOM_TOSPIGOT), streamout.toByteArray()));
		}).repeat(20, TimeUnit.MILLISECONDS).schedule();
	}
	
	public void teleportPlayerToPosition(String playerName, ServerLocation location, String errorServerNotFound,
			String message, int delay)
	{
		Optional<Player> opplayer = plugin.getServer().getPlayer(playerName);
		if(opplayer.isEmpty())
		{
			return;
		}
		Player player = plugin.getServer().getPlayer(playerName).get();
		if(plugin.getServer().getServer(location.getServer()).isEmpty())
		{
			player.sendMessage(ChatApi.tl(errorServerNotFound));
			return;
		}
		teleportPlayerToPositionPost(player, location, message, delay);
	}
	
	public void teleportPlayerToPositionPost(final Player player, final ServerLocation location, String message, int delay)
	{
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
			if(player == null || location == null)
			{
				return;
			}
			if(!PluginMessageListener.containsServer(location.getServer()))
			{
				player.sendMessage(ChatApi.tl("<dark_red>Error</dark_red> <white>1</white> <red>- Server %server% is unknown!</red>"
						.replace("%server%", location.getServer())));
				return;
			}
			Optional<RegisteredServer> server = plugin.getServer().getServer(location.getServer());
			if(server.isEmpty())
			{
				player.sendMessage(ChatApi.tl("<dark_red>Error</dark_red> <white>2</white> <red>- Server %server% is unknown!</red>"
						.replace("%server%", location.getServer())));
				return;
			}
			CompletableFuture<Result> r = null;
			if(!player.getCurrentServer().get().getServerInfo().getName().equals(location.getServer()))
			{
				r = player.createConnectionRequest(server.get()).connect();
			}
			sendPluginMessagePosition(player, server.get(), location, message, r);
		}).delay(delay, TimeUnit.MILLISECONDS).schedule();
	}
	
	private void sendPluginMessagePosition(Player player, RegisteredServer server, ServerLocation location, String message, CompletableFuture<Result> result)
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
				out.writeUTF(StaticValues.CUSTOM_PLAYERTOPOSITION);
				out.writeUTF(player.getUsername());
				out.writeUTF(location.getServer());
				out.writeUTF(location.getWorldName());
				out.writeDouble(location.getX());
				out.writeDouble(location.getY());
				out.writeDouble(location.getZ());
				out.writeFloat(location.getYaw());
				out.writeFloat(location.getPitch());
				out.writeBoolean(message == null);
				if(message != null)
				{
					out.writeUTF(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	        server.sendPluginMessage(BTM.getMCID(StaticValues.CUSTOM_TOSPIGOT), streamout.toByteArray());
		}).repeat(20, TimeUnit.MILLISECONDS).schedule();
	}
}