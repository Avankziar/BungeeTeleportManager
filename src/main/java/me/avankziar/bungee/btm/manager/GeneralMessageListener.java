package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.general.objecthandler.ForbiddenHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GeneralMessageListener implements Listener	
{
	//private BungeeTeleportManager plugin;	
	
	public GeneralMessageListener(BungeeTeleportManager plugin)
	{
		//this.plugin = plugin;
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
        if (!event.getTag().equalsIgnoreCase(StaticValues.GENERAL_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.GENERAL_SENDLIST))
        {
        	String type = in.readUTF();
        	String mechanics = in.readUTF();
        	int size = in.readInt();
        	ArrayList<String> list = new ArrayList<>();
        	for(int i = 0; i < size; i++)
        	{
        		list.add(in.readUTF());
        	}
        	if(mechanics.equalsIgnoreCase("Home"))
        	{
        		if(type.equalsIgnoreCase("server"))
            	{
            		ForbiddenHandler.getHomeForbiddenServer().clear();
            		ForbiddenHandler.getHomeForbiddenServer().addAll(list);
            	} else if(type.equalsIgnoreCase("world"))
            	{
            		ForbiddenHandler.getHomeForbiddenWorld().clear();
            		ForbiddenHandler.getHomeForbiddenWorld().addAll(list);
            	}
        	} else if(mechanics.equalsIgnoreCase("Teleport"))
        	{
        		if(type.equalsIgnoreCase("server"))
            	{
            		ForbiddenHandler.getTeleportForbiddenServer().clear();
            		ForbiddenHandler.getTeleportForbiddenServer().addAll(list);
            	} else if(type.equalsIgnoreCase("world"))
            	{
            		ForbiddenHandler.getTeleportForbiddenWorld().clear();
            		ForbiddenHandler.getTeleportForbiddenWorld().addAll(list);
            	}
        	} else if(mechanics.equalsIgnoreCase("Warp"))
        	{
        		if(type.equalsIgnoreCase("server"))
            	{
            		ForbiddenHandler.getWarpForbiddenServer().clear();
            		ForbiddenHandler.getWarpForbiddenServer().addAll(list);
            	} else if(type.equalsIgnoreCase("world"))
            	{
            		ForbiddenHandler.getWarpForbiddenWorld().clear();
            		ForbiddenHandler.getWarpForbiddenWorld().addAll(list);
            	}
        	}
        	return;
        } else if(task.equals(StaticValues.GENERAL_SENDSETTING))
        {
        	String setting = in.readUTF();
        	String cast = in.readUTF();
        	Object object = null;
        	switch(cast)
        	{
        	case "STRING":
        		object = (String) in.readUTF();
        		break;
        	case "BOOLEAN":
        		object = in.readBoolean();
        		break;
        	case "INTEGER":
        		object = in.readInt();
        		break;
        	case "LONG":
        		object = in.readLong();
        		break;
        	case "DOUBLE":
        		object = in.readDouble();
        		break;
        	case "FLOAT":
        		object = in.readFloat();
        		break;
        	default: 
        		break;
        	}
        	if(object == null)
        	{
        		return;
        	}
        	if(setting.equalsIgnoreCase("UpdateBackInForbiddenAreas"))
        	{
        		if(!(object instanceof Boolean))
        		{
        			return;
        		}
        		boolean b = (Boolean) object;
        		ForbiddenHandler.updateBackInForbiddenAreas = b;
        	} else if(setting.equalsIgnoreCase("UpdateDeathbackInForbiddenAreas"))
        	{
        		if(!(object instanceof Boolean))
        		{
        			return;
        		}
        		boolean b = (Boolean) object;
        		ForbiddenHandler.updateDeathBackInForbiddenAreas = b;
        	}
        }
	}

}
