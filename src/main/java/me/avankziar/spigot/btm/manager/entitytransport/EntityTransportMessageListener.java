package main.java.me.avankziar.spigot.btm.manager.entitytransport;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.object.serialization.LivingEntitySerialization;

public class EntityTransportMessageListener implements PluginMessageListener
{	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.ENTITYTRANSPORT_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.ENTITYTRANSPORT_ENTITYTOPOSITION))
            	{
            		String data = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	if(Bukkit.getWorld(worldName) == null)
					{
						player.spigot().sendMessage(ChatApi.tctl(BungeeTeleportManager.getPlugin().getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
										.replace("%world%", worldName)));
						return;
					}
                	Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
					LivingEntitySerialization.spawnEntity(loc, data);
            		return;
            	} else if(task.equals(StaticValues.ENTITYTRANSPORT_ENTITYTOPLAYER))
            	{
            		String data = in.readUTF();
                	String uuid = in.readUTF();
                	Player target = Bukkit.getPlayer(UUID.fromString(uuid));
                	if(player != null)
                	{
                		LivingEntitySerialization.spawnEntity(target.getLocation(), data);
                	}
                	return;
            	}
            } catch (IOException e) 
            {
    			e.printStackTrace();
    		}
		}
	}
}