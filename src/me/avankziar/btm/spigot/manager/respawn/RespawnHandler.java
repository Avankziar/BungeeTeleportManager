package me.avankziar.btm.spigot.manager.respawn;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Deathzone;
import me.avankziar.btm.general.object.FirstSpawn;
import me.avankziar.btm.general.object.Respawn;
import me.avankziar.btm.general.object.SavePoint;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.database.MysqlHandler.Type;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.manager.firstspawn.FirstSpawnHandler;
import me.avankziar.btm.spigot.manager.savepoint.SavePointHandler;
import net.md_5.bungee.api.chat.BaseComponent;

public class RespawnHandler
{
	private BTM plugin;
	private static LinkedHashMap<UUID, ServerLocation> deathpoint = new LinkedHashMap<>();
	public static ArrayList<UUID> deathzoneCreateMode = new ArrayList<>();
	private LinkedHashMap<UUID, DeathzonePosition> deathzoneposition = new LinkedHashMap<>();
	
	public RespawnHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public static void setDeathpoint(Player player)
	{
		if(deathpoint.containsKey(player.getUniqueId()))
		{
			deathpoint.replace(player.getUniqueId(), Utility.getLocation(player.getLocation()));
		} else
		{
			deathpoint.put(player.getUniqueId(), Utility.getLocation(player.getLocation()));
		}
	}
	
	private double mathSub(double one, double two)
	{
		return Math.max(one, two)-Math.min(one, two);
	}
	
	public void sendPlayerToRespawn(Player player, Respawn respawn)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(respawn.getLocation().getServer().equals(cfgh.getServer()) && player != null)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(respawn.getLocation()));
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.WarpTo")
							.replace("%respawn%", respawn.getDisplayname())));
				}
			}.runTaskLater(plugin, 1);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.RESPAWN_PLAYERTOPOSITION);
				out.writeUTF(player.getName());
				out.writeUTF(respawn.getDisplayname());
				out.writeUTF(respawn.getLocation().getServer());
				out.writeUTF(respawn.getLocation().getWorldName());
				out.writeDouble(respawn.getLocation().getX());
				out.writeDouble(respawn.getLocation().getY());
				out.writeDouble(respawn.getLocation().getZ());
				out.writeFloat(respawn.getLocation().getYaw());
				out.writeFloat(respawn.getLocation().getPitch());
			} catch (IOException e) 
	        {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.RESPAWN_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
	
	public void revivePlayer(Player player, boolean simulate)
	{
		simulate(player, simulate, "CmdDeathzone.Simulate.CheckIfVanillaRespawn");
		if(!plugin.getYamlHandler().getRespawn().getBoolean("Use.BTMRespawn", true))
		{
			simulate(player, simulate, "CmdDeathzone.Simulate.IsVanillaRespawn");
			simulate(player, simulate, "CmdDeathzone.Simulate.End");
			return;
		}
		simulate(player, simulate, "CmdDeathzone.Simulate.Simple.IsUseSimpleRespawn");
		if(plugin.getYamlHandler().getRespawn().getBoolean("Use.PreferSimpleRespawnMechanic", true))
		{
			simpleRevive(player, simulate);
		} else
		{
			simulate(player, simulate, "CmdDeathzone.Simulate.DeathFlowChart.IsInUse");
			deathflowchartrevive(player, simulate);
		}
	}
	
	private void simulate(Player player, boolean simulate, String path, String... replacer)
	{
		if(simulate)
		{
			String s = plugin.getYamlHandler().getLang().getString(path);
			for(int i = 0; i < replacer.length; i++)
			{
				if(i+1 >= replacer.length)
				{
					break;
				}
				s = s.replace(replacer[i], replacer[i+1]);
			}
			player.spigot().sendMessage(ChatApiOld.tctl(s));
		}
	}
	
	private void simpleRevive(Player player, boolean simulate)
	{
		simulate(player, simulate, "CmdDeathzone.Simulate.Simple.RespawnSearch");
		String simple = plugin.getYamlHandler().getRespawn().getString("Use.SimpleRespawnMechanic.RespawnAt", "FIRSTSPAWN");
		simulate(player, simulate, "CmdDeathzone.Simulate.Simple.RespawnIs", "%value%", simple);
		sendToRevive(player, simple, simulate);
		simulate(player, simulate, "CmdDeathzone.Simulate.End");
	}
	
	private void deathflowchartrevive(Player player, boolean simulate)
	{
		ServerLocation deathloc = deathpoint.get(player.getUniqueId());
		if(deathloc == null)
		{
			if(simulate)
			{
				deathloc = Utility.getLocation(player.getLocation());
				simulate(player, simulate, "CmdDeathzone.Simulate.DeathFlowChart.CheckForDeathLocation");
			} else
			{
				simulate(player, simulate, "CmdDeathzone.Simulate.DeathFlowChart.DeathzoneOrDeathzonePathNotExist");
				simpleRevive(player, simulate);
				return;
			}
		}
		final String server = deathloc.getServer();
		final String world = deathloc.getWorldName();
		final int x = (int) deathloc.getX();
		final int y = (int) deathloc.getY();
		final int z = (int) deathloc.getZ();
		simulate(player, simulate, "CmdDeathzone.Simulate.DeathFlowChart.CheckForDeathzone");
		Deathzone dz = (Deathzone) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEATHZONE,
				  "`pos_one_server` = ? AND `pos_one_world` = ? AND `pos_two_server` = ? AND `pos_two_world` = ? AND "
				+ "`pos_one_x` <= ? AND `pos_two_x` >= ? AND "
				+ "`pos_one_y` <= ? AND `pos_two_y` >= ? AND "
				+ "`pos_one_z` <= ? AND `pos_two_z` >= ? "
				+ "ORDER BY `priority` DESC, `id` ASC",
				server, world, server, world,
				x, x, y, y, z, z);
		if(dz == null
				|| plugin.getYamlHandler().getRespawn().get("DeathFlowChart."+dz.getDeathzonepath()) == null)
		{
			simulate(player, simulate, "CmdDeathzone.Simulate.DeathFlowChart.DeathzoneOrDeathzonePathNotExist");
			simpleRevive(player, simulate);
			return;
		}
		simulate(player, simulate, "CmdDeathzone.Simulate.DeathFlowChart.DeathzonepathFound", "%value%", dz.getDeathzonepath());
		for(String s : plugin.getYamlHandler().getRespawn().getStringList("DeathFlowChart."+dz.getDeathzonepath()))
		{
			if(sendToRevive(player, s, simulate))
			{
				break;
			}
		}
		simulate(player, simulate, "CmdDeathzone.Simulate.End");
	}
	
	private boolean sendToRevive(Player player, String simple, boolean simulate)
	{
		simulate(player, simulate, "CmdDeathzone.Simulate.Revive.ChecksType", "%value%", simple);
		if(simple.startsWith("FIRSTSPAWN"))
		{
			FirstSpawn v = null;
			if(simple.contains(":"))
			{
				String[] split = simple.split(":");
				if(split.length != 2)
				{
					simulate(player, simulate, "CmdDeathzone.Simulate.Revive.CouldNotBeResolved", "%value%", simple);
					return false;
				}
				simulate(player, simulate, "CmdDeathzone.Simulate.Revive.ChecksType", "%value%", simple);
				v = (FirstSpawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", split[1]);
			} else
			{
				v = (FirstSpawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", new ConfigHandler(plugin).getServer());
			}
			if(v == null)
			{
				simulate(player, simulate, "CmdDeathzone.Simulate.Revive.CouldNotBeResolved", "%value%", simple);
				return false;
			}
			simulate(player, simulate, "CmdDeathzone.Simulate.Revive.FoundedAndSendTo", "%value%", simple, "%name%", v.getServer());
			new FirstSpawnHandler(plugin).sendPlayerToFirstSpawn(player, v, false);
		} else if(simple.startsWith("RESPAWN"))
		{
			Respawn r = null;
			if(simple.contains(":"))
			{
				String[] split = simple.split(":");
				if(split.length != 2)
				{
					simulate(player, simulate, "CmdDeathzone.Simulate.Revive.CouldNotBeResolved", "%value%", simple);
					return false;
				}
				String s = split[1];
				r = (Respawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.RESPAWN, "`displayname` = ?", s);
			} else
			{
				if(simple.equalsIgnoreCase("RESPAWN_CLOSEST") || simple.equalsIgnoreCase("RESPAWN_FARTHEST"))
				{
					ArrayList<Respawn> list = ConvertHandler.convertListIV(
							plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.RESPAWN, "`priority`", true,
							"`server` = ? AND `world` = ?", new ConfigHandler(plugin).getServer(), player.getLocation().getWorld().getName()));
					double x = player.getLocation().getX();
					double y = player.getLocation().getY();
					double z = player.getLocation().getZ();
					for(Respawn v : list)
					{
						if(r == null)
						{
							r = v;
						} else
						{
							double one = mathSub(r.getLocation().getX(), x)+mathSub(r.getLocation().getY(), y)+mathSub(r.getLocation().getZ(), z);
							double two = mathSub(v.getLocation().getX(), x)+mathSub(v.getLocation().getY(), y)+mathSub(v.getLocation().getZ(), z);
							if(simple.equalsIgnoreCase("RESPAWN_CLOSEST"))
							{
								if(one > two)
								{
									r = v;
								}
							} else if(simple.equalsIgnoreCase("RESPAWN_FARTHEST"))
							{
								if(one < two)
								{
									r = v;
								}
							}
						}
					}
				} else if(simple.equalsIgnoreCase("RESPAWN_HIGHSTPRIO"))
				{
					r = (Respawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.RESPAWN, "1 ORDER BY `priority` DESC");
				} else if(simple.equalsIgnoreCase("RESPAWN_LOWESTPRIO"))
				{
					r = (Respawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.RESPAWN, "1 ORDER BY `priority` ASC");
				}
			}
			if(r == null)
			{
				simulate(player, simulate, "CmdDeathzone.Simulate.Revive.CouldNotBeResolved", "%value%", simple);
				return false;
			}
			simulate(player, simulate, "CmdDeathzone.Simulate.Revive.FoundedAndSendTo", "%value%", simple, "%name%", r.getDisplayname());
			sendPlayerToRespawn(player, r);
		} else if(simple.startsWith("SAVEPOINT"))
		{
			SavePoint v = null;
			if(simple.contains(":"))
			{
				String[] split = simple.split(":");
				if(split.length != 2)
				{
					simulate(player, simulate, "CmdDeathzone.Simulate.Revive.CouldNotBeResolved", "%value%", simple);
					return false;
				}
				String s = split[1];
				v = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
						player.getUniqueId().toString(), s);
			} else
			{
				v = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? ORDER BY `id` DESC",
						player.getUniqueId().toString());
			}
			if(v == null)
			{
				simulate(player, simulate, "CmdDeathzone.Simulate.Revive.CouldNotBeResolved", "%value%", simple);
				return false;
			}
			simulate(player, simulate, "CmdDeathzone.Simulate.Revive.FoundedAndSendTo", "%value%", simple, "%name%", v.getSavePointName());
			new SavePointHandler(plugin).sendPlayerToSavePointPost(player, v, player.getName(), player.getUniqueId().toString(), false);
		} else 
		{
			return false;
		}
		return true;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> mapping(
			Respawn r,
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map,
			BaseComponent bct)
	{
		if(map.containsKey(r.getLocation().getServer()))
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(r.getLocation().getServer());
			if(mapmap.containsKey(r.getLocation().getWorldName()))
			{
				ArrayList<BaseComponent> bc = mapmap.get(r.getLocation().getWorldName());
				bc.add(bct);
				mapmap.replace(r.getLocation().getWorldName(), bc);
				map.replace(r.getLocation().getServer(), mapmap);
				return map;
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				bc.add(ChatApiOld.tctl("  &e"+r.getLocation().getWorldName()+": "));
				bc.add(bct);
				mapmap.put(r.getLocation().getWorldName(), bc);
				map.replace(r.getLocation().getServer(), mapmap);
				return map;
			}
		} else
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = new LinkedHashMap<String, ArrayList<BaseComponent>>();
			ArrayList<BaseComponent> bc = new ArrayList<>();
			bc.add(ChatApiOld.tctl("  &e"+r.getLocation().getWorldName()+": "));
			bc.add(bct);
			mapmap.put(r.getLocation().getWorldName(), bc);
			map.put(r.getLocation().getServer(), mapmap);
			return map;
		}
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> mapping(
			Deathzone r,
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map,
			BaseComponent bct)
	{
		if(map.containsKey(r.getPosition1().getServer()))
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(r.getPosition1().getServer());
			if(mapmap.containsKey(r.getPosition1().getWorldName()))
			{
				ArrayList<BaseComponent> bc = mapmap.get(r.getPosition1().getWorldName());
				bc.add(bct);
				mapmap.replace(r.getPosition1().getWorldName(), bc);
				map.replace(r.getPosition1().getServer(), mapmap);
				return map;
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				bc.add(ChatApiOld.tctl("  &e"+r.getPosition1().getWorldName()+": "));
				bc.add(bct);
				mapmap.put(r.getPosition1().getWorldName(), bc);
				map.replace(r.getPosition1().getServer(), mapmap);
				return map;
			}
		} else
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = new LinkedHashMap<String, ArrayList<BaseComponent>>();
			ArrayList<BaseComponent> bc = new ArrayList<>();
			bc.add(ChatApiOld.tctl("  &e"+r.getPosition1().getWorldName()+": "));
			bc.add(bct);
			mapmap.put(r.getPosition1().getWorldName(), bc);
			map.put(r.getPosition1().getServer(), mapmap);
			return map;
		}
	}
	
	public class DeathzonePosition
	{
		public ServerLocation pos1;
		public ServerLocation pos2;
		
		public Location getLocation1()
		{
			return new Location(Bukkit.getWorld(pos1.getWorldName()), pos1.getX(), pos1.getY(), pos1.getX(), pos1.getYaw(), pos1.getPitch());
		}
		
		public Location getLocation2()
		{
			return new Location(Bukkit.getWorld(pos2.getWorldName()), pos2.getX(), pos2.getY(), pos2.getX(), pos2.getYaw(), pos2.getPitch());
		}
	}
	
	public void addDeathzonePosition(UUID uuid, boolean pos1, ServerLocation loc)
	{
		if(deathzoneposition.containsKey(uuid))
		{
			DeathzonePosition pp = deathzoneposition.get(uuid);
			if(pos1)
			{
				pp.pos1 = loc;
			} else
			{
				pp.pos2 = loc;
			}
			deathzoneposition.replace(uuid, pp);
		} else
		{
			DeathzonePosition pp = new DeathzonePosition();
			if(pos1)
			{
				pp.pos1 = loc;
			} else
			{
				pp.pos2 = loc;
			}
			deathzoneposition.put(uuid, pp);
		}
	}
	
	public DeathzonePosition getDeathzonePosition(UUID uuid)
	{
		return deathzoneposition.get(uuid);
	}
	
	public void removeDeathzonePosition(UUID uuid)
	{
		deathzoneposition.remove(uuid);
	}
}