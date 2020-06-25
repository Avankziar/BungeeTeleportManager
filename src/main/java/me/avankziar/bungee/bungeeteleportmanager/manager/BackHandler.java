package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.StringValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BackHandler
{
	private HashMap<String,Back> backLocations; //Playername welche anfragt
	private HashMap<String,Back> deathBackLocations;
	
	public BackHandler()
	{
		backLocations = new HashMap<>();
		deathBackLocations = new HashMap<>();
	}

	public HashMap<String,Back> getBackLocations()
	{
		return backLocations;
	}
	
	public HashMap<String,Back> getDeathBackLocations()
	{
		return deathBackLocations;
	}
	
	public void requestNewBack(ProxiedPlayer player)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
			out.writeUTF(StringValues.BACK_REQUESTNEWBACK);
			out.writeUTF(player.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	    player.getServer().sendData(StringValues.BACK_TOSPIGOT, streamout.toByteArray());
	}	
}
