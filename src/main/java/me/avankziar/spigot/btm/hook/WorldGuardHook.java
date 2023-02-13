package main.java.me.avankziar.spigot.btm.hook;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import main.java.me.avankziar.general.object.Mechanics;

public class WorldGuardHook
{
	public static StateFlag HOME_CREATE;
	public static StateFlag PORTAL_CREATE;
	public static StateFlag WARP_CREATE;
	
	public static StateFlag BACK_USE;
	public static StateFlag DEATHBACK_USE;
	public static StateFlag HOME_USE;
	public static StateFlag PORTAL_USE;
	public static StateFlag TPA_USE;
	public static StateFlag TPA_ACCEPT_USE;
	public static StateFlag WARP_USE;
	
	public static boolean init()
	{
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		try 
		{
			StateFlag hc = new StateFlag("btm-home-create", true);
	        registry.register(hc);
	        HOME_CREATE = hc;
	        StateFlag pc = new StateFlag("btm-portal-create", true);
	        registry.register(pc);
	        PORTAL_CREATE = pc;
	        StateFlag wc = new StateFlag("btm-warp-create", true);
	        registry.register(wc);
	        WARP_CREATE = wc;
	        StateFlag bu = new StateFlag("btm-back-use", true);
	        registry.register(bu);
	        BACK_USE = bu;
	        StateFlag dbu = new StateFlag("btm-deathback-use", true);
	        registry.register(dbu);
	        DEATHBACK_USE = dbu;
	        StateFlag hu = new StateFlag("btm-home-use", true);
	        registry.register(hu);
	        HOME_USE = hu;
	        StateFlag pu = new StateFlag("btm-portal-use", true);
	        registry.register(pu);
	        PORTAL_USE = pu;
	        StateFlag tpau = new StateFlag("btm-tpa-use", true);
	        registry.register(tpau);
	        TPA_USE = tpau;
	        StateFlag tpaau = new StateFlag("btm-tpa-accept-use", true);
	        registry.register(tpaau);
	        TPA_ACCEPT_USE = tpaau;
	        StateFlag wu = new StateFlag("btm-warp-use", true);
	        registry.register(wu);
	        WARP_USE = wu;
	    } catch (FlagConflictException e) 
		{
	        return false;
	    }
		return true;
	}
	
	public static boolean canCreateHome(Player player)
	{
        return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.HOME.getLower()) 
        		? true 
        		: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
        				BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), HOME_CREATE);
	}
	
	public static boolean canCreatePortal(Player player, Location pointOne, Location pointTwo)
	{
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location loc1 = BukkitAdapter.adapt(pointOne);
        com.sk89q.worldedit.util.Location loc2 = BukkitAdapter.adapt(pointTwo);
        boolean by = player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.PORTAL.getLower());
        boolean bo = query.testState(loc1, WorldGuardPlugin.inst().wrapPlayer(player), PORTAL_CREATE);
        boolean boo = query.testState(loc2, WorldGuardPlugin.inst().wrapPlayer(player), PORTAL_CREATE);
        return by ? true : (bo && boo);
	}
	
	public static boolean canCreateWarp(Player player)
	{
        return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.WARP.getLower()) 
        		? true 
        		: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
        				BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), WARP_CREATE);
	}
	
	public static boolean canUseBack(Player player)
	{
        return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.BACK.getLower())
        		? true 
        		: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
        			BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), BACK_USE);
	}
	
	public static boolean canUseDeathback(Player player)
	{
		 return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.DEATHBACK.getLower())
				 ? true 
					: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
		        			BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), DEATHBACK_USE);
	}
	
	public static boolean canUseHome(Player player)
	{
		return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.HOME.getLower())
	        	? true 
	        	: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
	        			BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), HOME_USE);
	}
	
	public static boolean canUsePortal(Player player)
	{
		return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.PORTAL.getLower())
	        	? true 
	        	: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
	        			BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), PORTAL_USE);
	}
	
	public static boolean canUseTPA(Player player)
	{
		return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TPA_ONLY.getLower())
	        	? true 
	        	: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
	        			BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), WARP_USE);
	}
	
	public static boolean canUseTPAAccept(Player player)
	{
		return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TPA_ONLY.getLower())
	        	? true 
	        	: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
	        			BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), WARP_USE);
	}
	
	public static boolean canUseWarp(Player player)
	{
		return player.hasPermission(main.java.me.avankziar.general.objecthandler.StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.WARP.getLower())
	        	? true 
	        	: WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(
	        			BukkitAdapter.adapt(player.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player), WARP_USE);
	}
}