package me.avankziar.btm.bungee.manager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import me.avankziar.btm.bungee.BTM;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class FirstSpawnMessageListener implements Listener	
{
	private BTM plugin;
	
	public FirstSpawnMessageListener(BTM plugin)
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
       if (!event.getTag().equalsIgnoreCase(StaticValues.FIRSTSPAWN_TOBUNGEE)) 
       {
           return;
       }
       DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
       String task = in.readUTF();
       
       if(task.equals(StaticValues.FIRSTSPAWN_PLAYERTOPOSITION))
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
		   int delay = in.readInt();
		   BackHandler.getBack(in, uuid, playerName, Mechanics.FIRSTSPAWN);
		   ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
		   FirstSpawnHandler fsh = new 	FirstSpawnHandler(plugin);
		   fsh.teleportPlayerToFirstSpawn(playerName, location, delay);
		   return;
       } else if(task.equals(StaticValues.FIRSTSPAWN_DOCOMMANDS))
       {
    	   String uuid = in.readUTF();
    	   ProxiedPlayer player = BTM.getPlugin().getProxy().getPlayer(UUID.fromString(uuid));
    	   if(player == null)
    	   {
    		   return;
    	   }
    	   int size = in.readInt();
    	   for(int i = 0; i < size; i++)
    	   {
    		   String cmd = in.readUTF();
    		   if(cmd.equalsIgnoreCase("dummy"))
    		   {
    			   continue;
    		   }
    		   BTM.getPlugin().getProxy().getPluginManager().dispatchCommand(player, cmd.replace("%player%", player.getName()));
    	   }
    	   int sizeII = in.readInt();
    	   for(int i = 0; i < sizeII; i++)
    	   {
    		   String cmd = in.readUTF();
    		   if(cmd.equalsIgnoreCase("dummy"))
    		   {
    			   continue;
    		   }
    		   BTM.getPlugin().getProxy().getPluginManager().dispatchCommand(BTM.getPlugin().getProxy().getConsole(),
    				   cmd.replace("%player%", player.getName()));
    	   }
       }
       return;
	}
}