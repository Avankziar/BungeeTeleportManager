package me.avankziar.btm.spigot.ifh;

import java.util.UUID;

import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.ifh.spigot.position.LastKnownPosition;
import me.avankziar.ifh.spigot.position.ServerLocation;

public class LastKnownPositionProvider implements LastKnownPosition
{
	private BTM plugin;
	
	public LastKnownPositionProvider(BTM plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public ServerLocation getLastKnownPosition(UUID uuid)
	{
		Back back = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
				"`player_uuid` = ?",  uuid.toString());
		return new ServerLocation(back.getLocation().getServer(), back.getLocation().getWorldName(),
				back.getLocation().getX(), back.getLocation().getY(), back.getLocation().getZ(),
				back.getLocation().getYaw(), back.getLocation().getPitch());
	}

}
