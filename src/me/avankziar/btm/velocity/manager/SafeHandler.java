package me.avankziar.btm.velocity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.avankziar.btm.general.assistance.ChatApi;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.velocity.BTM;

public class SafeHandler
{
	private BTM plugin;
	
	public SafeHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.SAFE_CHECKPATH))
        {
        	String uuid = in.readUTF();
        	String playername = in.readUTF();
        	Mechanics mechanics = Mechanics.valueOf(in.readUTF());
        	String servers = in.readUTF();
        	String worldname = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	Optional<Player> player = plugin.getServer().getPlayer(UUID.fromString(uuid));
    		if(player.isEmpty())
    		{
    			return;
    		}
        	if(!serverIsInhabitant(servers))
        	{
        		//Server isnt inhabitant, checking if safe, is impossible
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
		        player.get().getCurrentServer().get().getServer().sendPluginMessage(BTM.getMCID(StaticValues.SAFE_TOSPIGOT), streamout.toByteArray());
				return;
        	}
        	Optional<RegisteredServer> server = plugin.getServer().getServer(servers);
    		if(server.isEmpty())
    		{
    			player.get().sendMessage(ChatApi.tl("<dark_red>Error</dark_red> <white>3</white> <red>- Server %server% is unknown!</red>"
    					.replace("%server%", servers)));
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
					out.writeUTF(servers);
					out.writeUTF(worldname);
					out.writeDouble(x);
					out.writeDouble(y);
					out.writeDouble(z);
					out.writeFloat(yaw);
					out.writeFloat(pitch);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        server.get().sendPluginMessage(BTM.getMCID(StaticValues.SAFE_TOSPIGOT), streamout.toByteArray());
        	}
        } else if(task.equals(StaticValues.SAFE_CHECKEDPATH))
        {
        	String uuid = in.readUTF();
        	String playername = in.readUTF();
        	boolean isSafe = in.readBoolean();
        	Optional<Player> player = plugin.getServer().getPlayer(UUID.fromString(uuid));
    		if(player.isEmpty())
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
	        player.get().getCurrentServer().ifPresent(y -> y.sendPluginMessage(BTM.getMCID(StaticValues.SAFE_TOSPIGOT), streamout.toByteArray()));
	        	;
        }
	}
	
	private boolean serverIsInhabitant(String server)
	{
		return plugin.getServer().getServer(server).get().getPlayersConnected().size() > 0;
	}
}