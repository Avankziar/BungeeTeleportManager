package main.java.me.avankziar.spigot.btm.ifh;

import java.util.UUID;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.ifh.spigot.position.LastKnownPosition;
import main.java.me.avankziar.ifh.spigot.position.ServerLocation;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;

public class LastKnownPositionProvider implements LastKnownPosition
{
	private BungeeTeleportManager plugin;
	
	public LastKnownPositionProvider(BungeeTeleportManager plugin)
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
