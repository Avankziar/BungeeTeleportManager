package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SafeLocationMessageListener implements Listener	
{
	private BungeeTeleportManager plugin;
	
	public SafeLocationMessageListener(BungeeTeleportManager plugin)
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
        if (!event.getTag().equalsIgnoreCase(StaticValues.SAFE_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.SAFE_CHECKPATH))
        {
        	String uuid = in.readUTF();
        	String playername = in.readUTF();
        	Mechanics mechanics = Mechanics.valueOf(in.readUTF());
        	String server = in.readUTF();
        	String worldname = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	if(!serverIsInhabitant(server))
        	{
        		//Server isnt inhabitant, checking if safe, is impossible
        		ProxiedPlayer player = plugin.getProxy().getPlayer(UUID.fromString(uuid));
        		if(player == null)
        		{
        			return;
        		}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StaticValues.SAFE_CHECKEDPATH);
		        	out.writeUTF(uuid);
					out.writeUTF(playername);
					out.writeBoolean(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(player.getServer().getInfo().getName())
		        	.sendData(StaticValues.SAFE_TOSPIGOT, streamout.toByteArray());
				return;
        	}
        	switch(mechanics)
        	{
        	default:
        		return;
        	case HOME:
        	case SAVEPOINT:
        	case WARP:
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StaticValues.SAFE_CHECKPATH);
		        	out.writeUTF(uuid);
					out.writeUTF(playername);
					out.writeUTF(server);
					out.writeUTF(worldname);
					out.writeDouble(x);
					out.writeDouble(y);
					out.writeDouble(z);
					out.writeFloat(yaw);
					out.writeFloat(pitch);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(server).sendData(StaticValues.SAFE_TOSPIGOT, streamout.toByteArray());
        	}
        } else if(task.equals(StaticValues.SAFE_CHECKEDPATH))
        {
        	String uuid = in.readUTF();
        	String playername = in.readUTF();
        	boolean isSafe = in.readBoolean();
        	ProxiedPlayer player = plugin.getProxy().getPlayer(UUID.fromString(uuid));
        	if(player == null)
        	{
        		return;
        	}
        	ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
	        	out.writeUTF(StaticValues.SAFE_CHECKEDPATH);
	        	out.writeUTF(uuid);
				out.writeUTF(playername);
				out.writeBoolean(isSafe);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        plugin.getProxy().getServerInfo(player.getServer().getInfo().getName())
	        	.sendData(StaticValues.SAFE_TOSPIGOT, streamout.toByteArray());
        }
	}
	
	private boolean serverIsInhabitant(String server)
	{
		return plugin.getProxy().getServerInfo(server).getPlayers().size() > 0;
	}
}