package me.avankziar.btm.spigot.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;

import me.avankziar.btm.bungee.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Home;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.SavePoint;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.object.Warp;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.manager.home.HomeHandler;

public class SafeLocationHandler
{
	private BTM plugin;
	
	public LinkedHashMap<String, Object> pending = new LinkedHashMap<>(); //String == UUID!playername
	
	public SafeLocationHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	/*
	 * Start methode, to test, if a safe path exist
	 * If the teleport is only INNER server, than use direct isSafeDestination
	 */
	public void safeLocationNetworkPending(Player player, String uuid, String playername, Object teleportObject)
	{
		final String key = uuid+"!"+playername;
		if(pending.containsKey(key))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("AlreadyPendingTeleport")));
			return;
		}
		if(teleportObject instanceof Home)
		{
			Home home = (Home) teleportObject;
			sendData(player, Mechanics.HOME, uuid, playername, home.getLocation());
		} else if(teleportObject instanceof SavePoint)
		{
			SavePoint sp = (SavePoint) teleportObject;
			sendData(player, Mechanics.SAVEPOINT, uuid, playername, sp.getLocation());
		} else if(teleportObject instanceof Warp)
		{
			Warp warp = (Warp) teleportObject;
			sendData(player, Mechanics.WARP, uuid, playername, warp.getLocation());
		}
	}
	
	private void sendData(Player player, Mechanics mechanics, String uuid, String playername, ServerLocation loc)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.SAFE_CHECKPATH);
			out.writeUTF(uuid);
			out.writeUTF(playername);
			out.writeUTF(mechanics.toString());
			out.writeUTF(loc.getServer());
			out.writeUTF(loc.getWorldName());
			out.writeDouble(loc.getX());
			out.writeDouble(loc.getY());
			out.writeDouble(loc.getZ());
			out.writeFloat(loc.getYaw());
			out.writeFloat(loc.getPitch());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.SAFE_TOBUNGEE, stream.toByteArray());
	}
	
	/*
	 * Endmethode, to send the player to their target location
	 */
	public void safeLocationNetworkSending(String uuid, String playername)
	{
		final String key = uuid+"!"+playername;
		if(!pending.containsKey(key))
		{
			return;
		}
		final Object o = pending.get(key);
		Player other = Bukkit.getPlayer(UUID.fromString(uuid));
		if(other == null)
		{
			pending.remove(key);
			return;
		}
		if(o instanceof Home)
		{
			final Home home = (Home) pending.get(key);
			new HomeHandler(plugin).sendPlayerToHomePost(other, home);
		} else if(o instanceof SavePoint)
		{
			final SavePoint sp = (SavePoint) pending.get(key);
			plugin.getSavePointHandler().sendPlayerToSavePointPost(other, sp, playername, uuid, false);
		} else if(o instanceof Warp)
		{
			final Warp warp = (Warp) pending.get(key);
			plugin.getWarpHandler().sendPlayerToWarpPost(other, warp, playername, uuid);
		}		
		pending.remove(key);
	}
	
	public boolean isSafeDestination(final ServerLocation sl)
	{
		final Location loc = new Location(Bukkit.getWorld(sl.getWorldName()), sl.getX(), sl.getY(), sl.getZ(), sl.getYaw(), sl.getPitch());
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        if (isBlockOutsideWorldBorder(world, x, z)
        		|| !isBlockAboveAir(world, x, y, z)
        		|| !isBlockAboveAir(world, x, y+1, z)
        		|| !isBlockAboveAir(world, x, y+2, z)
        		|| isBlockUnsafe(world, x, y, z)
        		|| isBlockUnsafe(world, x, y-1, z))
        {
            return false;
        }
        return true;
    }
	
	/**
	 * Origin https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/main/java/com/earth2me/essentials/utils/LocationUtil.java
	 * Thanks for letting me know^^
	 */
	/*public static boolean getSafeDestination(final Location loc)
	{
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        if (isBlockOutsideWorldBorder(world, x, z)) 
        {
            x = getXInsideWorldBorder(world, x);
            z = getZInsideWorldBorder(world, z);
        }
        final int origX = x;
        final int origY = y;
        final int origZ = z;
        while (isBlockAboveAir(world, x, y, z)) {
            y -= 1;
            if (y < 0) {
                y = origY;
                break;
            }
        }
        if (isBlockUnsafe(world, x, y, z)) {
            x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
            z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
        }
        int i = 0;
        while (isBlockUnsafe(world, x, y, z)) {
            i++;
            if (i >= VOLUME.length) {
                x = origX;
                y = origY + RADIUS;
                z = origZ;
                break;
            }
            x = origX + VOLUME[i].getBlockX();
            y = origY + VOLUME[i].getBlockY();
            z = origZ + VOLUME[i].getBlockZ();
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y += 1;
            if (y >= world.getMaxHeight()) {
                x += 1;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y -= 1;
            if (y <= 1) {
                x += 1;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > loc.getBlockX()) 
                {
                    return false;
                }
            }
        }
        return true;
    }*/
	
    // Water types used for TRANSPARENT_MATERIALS and is-water-safe config option
    //private static final Set<Material> WATER_TYPES =  new HashSet<>(Arrays.asList(Material.WATER));
    // Types checked by isBlockDamaging
    private static final Set<Material> DAMAGING_TYPES = new HashSet<>(Arrays.asList(Material.CACTUS, Material.CAMPFIRE,
    		Material.FIRE, Material.MAGMA_BLOCK, Material.SOUL_CAMPFIRE, Material.SOUL_FIRE,
    		Material.SWEET_BERRY_BUSH, Material.WITHER_ROSE));
    private static final Set<Material> LAVA_TYPES = new HashSet<>(Arrays.asList(Material.LAVA));
    private static final Set<Material> PORTAL = new HashSet<>(Arrays.asList(Material.END_PORTAL, Material.NETHER_PORTAL));
    private static final Set<Material> BED = new HashSet<>(Arrays.asList(Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED,
    		Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED,
    		Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED,
    		Material.RED_BED, Material.WHITE_BED, Material.YELLOW_BED));
    private static final Set<Material> HOLLOW_MATERIALS = new HashSet<>();

    static {
    	HOLLOW_MATERIALS.add(Material.AIR);
        for (final Material mat : Material.values()) 
        {
        	BlockData block = null;
        	try
        	{
        		block = Bukkit.createBlockData(mat);
        	} catch(Exception e) 
        	{
        		continue;
        	}
        	
        	if(block instanceof Ageable 
        			|| block instanceof Door
        			|| block instanceof Farmland
        			|| block instanceof Fence
        			|| block instanceof Gate
        			|| block instanceof Leaves
        			|| block instanceof Rail
        			|| block instanceof RedstoneRail
        			|| block instanceof Snow)
        	{
        		HOLLOW_MATERIALS.add(mat);
        	}
        }
    }

    private static boolean isBlockOutsideWorldBorder(final World world, final int x, final int z) 
    {
        final Location center = world.getWorldBorder().getCenter();
        final int radius = (int) world.getWorldBorder().getSize() / 2;
        final int x1 = center.getBlockX() - radius, x2 = center.getBlockX() + radius;
        final int z1 = center.getBlockZ() - radius, z2 = center.getBlockZ() + radius;
        return x < x1 || x > x2 || z < z1 || z > z2;
    }

    /*private static int getXInsideWorldBorder(final World world, final int x) 
    {
        final Location center = world.getWorldBorder().getCenter();
        final int radius = (int) world.getWorldBorder().getSize() / 2;
        final int x1 = center.getBlockX() - radius, x2 = center.getBlockX() + radius;
        if (x < x1) 
        {
            return x1;
        } else if (x > x2) 
        {
            return x2;
        }
        return x;
    }

    private static int getZInsideWorldBorder(final World world, final int z) 
    {
        final Location center = world.getWorldBorder().getCenter();
        final int radius = (int) world.getWorldBorder().getSize() / 2;
        final int z1 = center.getBlockZ() - radius, z2 = center.getBlockZ() + radius;
        if (z < z1) 
        {
            return z1;
        } else if (z > z2)
        {
            return z2;
        }
        return z;
    }*/
	
    private static boolean isBlockUnsafe(final World world, final int x, final int y, final int z) 
    {
        return isBlockDamaging(world, x, y, z) || isBlockAboveAir(world, x, y, z);
    }
    
    private static boolean isBlockAboveAir(final World world, final int x, final int y, final int z) 
	{
        return y > world.getMaxHeight() || HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType());
    }

    private static boolean isBlockDamaging(final World world, final int x, final int y, final int z) 
    {
        final Material block = world.getBlockAt(x, y, z).getType();
        final Material below = world.getBlockAt(x, y - 1, z).getType();
        final Material above = world.getBlockAt(x, y + 1, z).getType();

        if (DAMAGING_TYPES.contains(below) || LAVA_TYPES.contains(below) || BED.contains(below)) 
        {
            return true;
        }

        if (PORTAL.contains(block)) 
        {
            return true;
        }

        return !HOLLOW_MATERIALS.contains(block) || !HOLLOW_MATERIALS.contains(above);
    }
}