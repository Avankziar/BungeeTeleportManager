package me.avankziar.btm.velocity.listener;

import java.io.IOException;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.velocity.BTM;
import me.avankziar.btm.velocity.manager.BackHandler;
import me.avankziar.btm.velocity.manager.CustomHandler;
import me.avankziar.btm.velocity.manager.EntityTransportHandler;
import me.avankziar.btm.velocity.manager.FirstSpawnHandler;
import me.avankziar.btm.velocity.manager.HomeHandler;
import me.avankziar.btm.velocity.manager.IFHHandler;
import me.avankziar.btm.velocity.manager.PortalHandler;
import me.avankziar.btm.velocity.manager.RandomTeleportHandler;
import me.avankziar.btm.velocity.manager.RespawnHandler;
import me.avankziar.btm.velocity.manager.SafeHandler;
import me.avankziar.btm.velocity.manager.SavePointHandler;
import me.avankziar.btm.velocity.manager.TeleportHandler;
import me.avankziar.btm.velocity.manager.WarpHandler;

public class PluginMessageListener
{
	private BTM plugin;
	
	public PluginMessageListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@Subscribe
    public void onPluginMessageFromBackend(PluginMessageEvent event) 
    {
        if (!(event.getSource() instanceof ServerConnection)) 
        {
            return;
        }
        //ServerConnection backend = (ServerConnection) event.getSource(); At the Moment not needed
        try
		{
        	switch(event.getIdentifier().getId())
        	{
        	case StaticValues.GENERAL_TOBUNGEE: break;
        	case StaticValues.BACK_TOBUNGEE:
        		new BackHandler(plugin).pluginMessage(event); return;
        	case StaticValues.CUSTOM_TOBUNGEE:
        		new CustomHandler(plugin).pluginMessage(event);	return;
        	case StaticValues.ENTITYTRANSPORT_TOBUNGEE:
        		new EntityTransportHandler(plugin).pluginMessage(event); return;
        	case StaticValues.FIRSTSPAWN_TOBUNGEE:
        		new FirstSpawnHandler(plugin).pluginMessage(event); return;
        	case StaticValues.HOME_TOBUNGEE:
        		new HomeHandler(plugin).pluginMessage(event); return;
        	case StaticValues.IFH_TOBUNGEE:
        		new IFHHandler(plugin).pluginMessage(event); return;
        	case StaticValues.PORTAL_TOBUNGEE:
        		new PortalHandler(plugin).pluginMessage(event); return;
        	case StaticValues.RANDOMTELEPORT_TOBUNGEE:
        		new RandomTeleportHandler(plugin).pluginMessage(event); return;
        	case StaticValues.RESPAWN_TOBUNGEE:
        		new RespawnHandler(plugin).pluginMessage(event); return;
        	case StaticValues.SAFE_TOBUNGEE:
        		new SafeHandler(plugin).pluginMessage(event); return;
        	case StaticValues.SAVEPOINT_TOBUNGEE:
        		new SavePointHandler(plugin).pluginMessage(event); return;
        	case StaticValues.TP_TOBUNGEE:
        		new TeleportHandler(plugin).pluginMessage(event); return;
        	case StaticValues.WARP_TOBUNGEE:
        		new WarpHandler(plugin).pluginMessage(event); return;
        	}
		} catch (IOException e)
		{
			e.printStackTrace();
		}   
    }
	
	public static boolean containsServer(String server)
	{
		for(RegisteredServer rs : BTM.getPlugin().getServer().getAllServers())
		{
			if(rs.getServerInfo().getName().equals(server))
			{
				return true;
			}
		}
		return false;
	}
}