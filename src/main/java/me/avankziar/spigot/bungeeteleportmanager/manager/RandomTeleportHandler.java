package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.RandomTeleport;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ForbiddenHandler.Mechanics;

public class RandomTeleportHandler
{
	private BungeeTeleportManager plugin;
	
	public RandomTeleportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendPlayerToRT(Player player, RandomTeleport rt, String playername, String uuid)
	{
		if(rt.getPoint1().getServer().equals(plugin.getYamlHandler().getConfig().getString("ServerName")))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.RandomTeleport", 2000);
			int delay = 1;
			if(!player.hasPermission(StaticValues.PERM_BYPASS_RANDOMTELEPORT_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			Location loc = getRandomTeleport(rt);
			if(loc == null)
			{
				player.sendMessage(ChatApi.tl("&cERROR!"));
				return;
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					plugin.getUtility().givesEffect(player, Mechanics.RANDOMTELEPORT, false, false);
					player.teleport(loc);
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdRandomTeleport.WarpTo")
							.replace("%server%", rt.getPoint1().getServer())
							.replace("%world%", rt.getPoint1().getWordName())));
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.RANDOMTELEPORT_PLAYERTOPOSITION);
				out.writeUTF(uuid);
				out.writeUTF(playername);
				out.writeBoolean(rt.isArea());
				out.writeUTF(rt.getPoint1().getServer());
				out.writeUTF(rt.getPoint1().getWordName());
				out.writeDouble(rt.getPoint1().getX());
				out.writeDouble(rt.getPoint1().getY());
				out.writeDouble(rt.getPoint1().getZ());
				if(rt.isArea())
				{
					out.writeDouble(rt.getPoint2().getX());
					out.writeDouble(rt.getPoint2().getY());
					out.writeDouble(rt.getPoint2().getZ());
				} else
				{
					out.writeInt(rt.getRadius());
				}
				if(!player.hasPermission(StaticValues.PERM_BYPASS_RANDOMTELEPORT_DELAY))
				{
					out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.RandomTeleport", 2000));
				} else
				{
					out.writeInt(25);
				}
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.RANDOMTELEPORT_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
	
	public Location getRandomTeleport(RandomTeleport rt)
	{
		Location loc = null;
		while(loc == null)
		{
			if(rt.isArea())
			{
				double x = Math.min(rt.getPoint1().getX(), rt.getPoint2().getX()) + 
						getRandom(new Random(), 0,
							(int) Math.max(getPositiveInt(rt.getPoint1().getX()), getPositiveInt(rt.getPoint2().getX())) -
							(int) Math.min(getPositiveInt(rt.getPoint1().getX()), getPositiveInt(rt.getPoint2().getX())));
				double y = getRandom(new Random(),
						(int) Math.min(rt.getPoint1().getY(), rt.getPoint2().getY()),
						(int) Math.max(rt.getPoint1().getY(), rt.getPoint2().getY()));
				double minY = Math.min(rt.getPoint1().getY(), rt.getPoint2().getY());
				double z = Math.min(rt.getPoint1().getZ(), rt.getPoint2().getZ()) + 
						getRandom(new Random(), 0,
							(int) Math.max(getPositiveInt(rt.getPoint1().getZ()), getPositiveInt(rt.getPoint2().getZ())) -
							(int) Math.min(getPositiveInt(rt.getPoint1().getZ()), getPositiveInt(rt.getPoint2().getZ())));
				loc = isSafe(new Location(Bukkit.getWorld(rt.getPoint1().getWordName()), x, y, z), minY);
			} else
			{
				double x = rt.getPoint1().getX() + getRoll()*getRandom(new Random(), 0, rt.getRadius());
				double y = rt.getPoint1().getY() + rt.getRadius();
				double minY = rt.getPoint1().getY() - rt.getRadius();
				if(minY <= 0) {minY = 1;}
				double z = rt.getPoint1().getZ() + getRoll()*getRandom(new Random(), 0, rt.getRadius());
				loc = isSafe(new Location(Bukkit.getWorld(rt.getPoint1().getWordName()), x, y, z), minY);
			}
		}
		BungeeTeleportManager.log.info("l: | "+loc.getX()+" | "+loc.getY()+" | "+loc.getZ());
		return loc;
	}
	
	private Location isSafe(Location loc, double minY)
	{
		Location l = loc;
		Location up = new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ());
		Location down = new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ());
		Location bottom = new Location(l.getWorld(), l.getX(), l.getY() - 2, l.getZ());
		int count = 0;
		while(l.getY() > minY)
		{
			Block bl = l.getBlock();
			Block bup = up.getBlock();
			Block bdown = down.getBlock();
			Block bbottom = bottom.getBlock();
			if(bbottom.getType().isSolid() && isTransparant(bdown) && isTransparant(bl))
			{
				return new Location(loc.getWorld(), bottom.getX(), down.getY(), bottom.getZ());
			} else if(bdown.getType().isSolid() && isTransparant(bl) && isTransparant(bup))
			{
				return new Location(loc.getWorld(), down.getX(), l.getY(), down.getZ());
			} else if(count >= 25)
			{
				return null;
			}
			l = new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ());
			up = new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ());
			down = new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ());
			bottom = new Location(l.getWorld(), l.getX(), l.getY() - 2, l.getZ());
		}
		return null;
	}
	
	private boolean isTransparant(Block block) 
	{
		Material type = block.getType();
		switch (type) 
		{
			case AIR:
				return true;
				
			case ACACIA_BUTTON:
				return true;
			case OAK_BUTTON:
				return true;
			case DARK_OAK_BUTTON:
				return true;
			case BIRCH_BUTTON:
				return true;
			case SPRUCE_BUTTON:
				return true;
			case JUNGLE_BUTTON:
				return true;
			case CRIMSON_BUTTON:
				return true;
			case WARPED_BUTTON:
				return true;
			case STONE_BUTTON:
				return true;
				
			case ACACIA_TRAPDOOR:
				return true;
			case OAK_TRAPDOOR:
				return true;
			case DARK_OAK_TRAPDOOR:
				return true;
			case BIRCH_TRAPDOOR:
				return true;
			case SPRUCE_TRAPDOOR:
				return true;
			case JUNGLE_TRAPDOOR:
				return true;
			case CRIMSON_TRAPDOOR:
				return true;
			case WARPED_TRAPDOOR:
				return true;
			case IRON_TRAPDOOR:
				return true;
				
			case ACACIA_LEAVES:
				return false;
			case BIRCH_LEAVES:
				return false;
			case SPRUCE_LEAVES:
				return false;
			case OAK_LEAVES:
				return false;
			case DARK_OAK_LEAVES:
				return false;
			case JUNGLE_LEAVES:
				return false;
				
			case OAK_PRESSURE_PLATE:
				return true;
			case DARK_OAK_PRESSURE_PLATE:
				return true;
			case ACACIA_PRESSURE_PLATE:
				return true;
			case BIRCH_PRESSURE_PLATE:
				return true;
			case SPRUCE_PRESSURE_PLATE:
				return true;
			case JUNGLE_PRESSURE_PLATE:
				return true;
			case CRIMSON_PRESSURE_PLATE:
				return true;
			case WARPED_PRESSURE_PLATE:
				return true;
			case STONE_PRESSURE_PLATE:
				return true;
			case LIGHT_WEIGHTED_PRESSURE_PLATE:
				return true;
			case HEAVY_WEIGHTED_PRESSURE_PLATE:
				return true;
				
			case ACACIA_SIGN:
				return true;
			case BIRCH_SIGN:
				return true;
			case SPRUCE_SIGN:
				return true;
			case OAK_SIGN:
				return true;
			case DARK_OAK_SIGN:
				return true;
			case JUNGLE_SIGN:
				return true;
			case CRIMSON_SIGN:
				return true;
			case WARPED_SIGN:
				return true;
				
			case ACACIA_WALL_SIGN:
				return true;
			case BIRCH_WALL_SIGN:
				return true;
			case SPRUCE_WALL_SIGN:
				return true;
			case OAK_WALL_SIGN:
				return true;
			case DARK_OAK_WALL_SIGN:
				return true;
			case JUNGLE_WALL_SIGN:
				return true;
			case CRIMSON_WALL_SIGN:
				return true;
			case WARPED_WALL_SIGN:
				return true;
			case POWERED_RAIL:
				return true;
			case BLACK_CARPET:
				return true;
			case BLUE_CARPET:
				return true;
			case BROWN_CARPET:
				return true;
			case CYAN_CARPET:
				return true;
			case GRAY_CARPET:
				return true;
			case GREEN_CARPET:
				return true;
			case LIGHT_BLUE_CARPET:
				return true;
			case LIGHT_GRAY_CARPET:
				return true;
			case LIME_CARPET:
				return true;
			case MAGENTA_CARPET:
				return true;
			case ORANGE_CARPET:
				return true;
			case PINK_CARPET:
				return true;
			case PURPLE_CARPET:
				return true;
			case RED_CARPET:
				return true;
			case WHITE_CARPET:
				return true;
			
			case ACTIVATOR_RAIL:
				return true;
			case ALLIUM:
				return true;
			case ARMOR_STAND:
				return true;
			case AZURE_BLUET:
				return true;
			case BLUE_ORCHID:
				return true;
			case BRAIN_CORAL:
				return true;
			case BRAIN_CORAL_FAN:
				return true;
			case BREWING_STAND:
				return true;
			case BUBBLE_CORAL:
				return true;
			case BUBBLE_CORAL_FAN:
				return true;
			case COBWEB:
				return true;
			case DAYLIGHT_DETECTOR:
				return true;
			case DEAD_BUSH:
				return true;
			case DETECTOR_RAIL:
				return true;
			case END_ROD:
				return true;
			case FERN:
				return true;
			case FIRE_CORAL:
				return true;
			case FIRE_CORAL_FAN:
				return true;
			case GRASS:
				return true;
			
			case HORN_CORAL:
				return true;
			case HORN_CORAL_FAN:
				return true;
			
			case LADDER:
				return true;
			case KELP:
				return true;
			case LARGE_FERN:
				return true;
			case LEVER:
				return true;
			case LILAC:
				return true;
			case LILY_PAD:
				return true;
			case ORANGE_TULIP:
				return true;
			case OXEYE_DAISY:
				return true;
			case POPPY:
				return true;
			case RAIL:
				return true;
			case RED_MUSHROOM:
				return true;
			case RED_TULIP:
				return true;
			case REDSTONE:
				return true;
			case REDSTONE_TORCH:
				return true;
			case REPEATER:
				return true;
			case ROSE_BUSH:
				return true;
			case SEA_PICKLE:
				return true;
			case SEAGRASS:
				return true;
			case SNOW:
				return true;
			case STRING:
				return true;
			case TALL_GRASS:
				return true;
			case TORCH:
				return true;
			case TRIPWIRE_HOOK:
				return true;
			case TUBE_CORAL:
				return true;
			case TUBE_CORAL_FAN:
				return true;
			case VINE:
				return true;
			case WHITE_TULIP:
				return true;
			case REDSTONE_WALL_TORCH:
				return true;
			case WALL_TORCH:
				return true;
			case REDSTONE_WIRE:
				return true;
			default:
				return false;
		}
	}
	
	private int getRoll()
	{
		int i = getRandom(new Random(), 0, 100);
		if(i <= 50)
		{
			return -1;
		} else
		{
			return 1;
		}
	}
	
	public int getRandom(Random rnd, int start, int end)
	{
	    int random = start + rnd.nextInt(end - start + 1);
		return random;
	}
	
	private double getPositiveInt(double number)
	{
		if(number > 0)
		{
			return number;
		} else
		{
			double n = number * -1;
			return n;
		}
	}

}
