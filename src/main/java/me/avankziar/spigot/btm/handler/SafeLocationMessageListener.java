package main.java.me.avankziar.spigot.btm.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;

public class SafeLocationMessageListener implements PluginMessageListener
{
	private BungeeTeleportManager plugin;
	
	public SafeLocationMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.SAFE_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.SAFE_CHECKPATH))
            	{
            		String uuid = in.readUTF();
                	String playername = in.readUTF();
                	String server = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	boolean isSafe = plugin.getSafeLocationHandler().isSafeDestination(
                			new ServerLocation(server, worldName, x, y, z, yaw, pitch));
                	sendOut(uuid, playername, isSafe);
            	} else if(task.equals(StaticValues.SAFE_CHECKEDPATH))
            	{
            		String uuid = in.readUTF();
            		String playername = in.readUTF();
            		boolean isSafe = in.readBoolean();
            		if(!isSafe)
            		{
            			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotSafeLocation")));
            			plugin.getSafeLocationHandler().pending.remove(uuid+"!"+playername);
    					return;
            		}
            		plugin.getSafeLocationHandler().safeLocationNetworkSending(uuid, playername);
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
	
	private void sendOut(String uuid, String playername, boolean isSafe)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.SAFE_CHECKEDPATH);
			out.writeUTF(uuid);
			out.writeUTF(playername);
			out.writeBoolean(isSafe);
		} catch (IOException e) {
			e.printStackTrace();
		}
        for(Player player : plugin.getServer().getOnlinePlayers())
        {
        	if(player.isOnline())
        	{
        		player.sendPluginMessage(plugin, StaticValues.SAFE_TOBUNGEE, stream.toByteArray());
        		return;
        	}
        }
	}
}