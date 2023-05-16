package main.java.me.avankziar.spigot.btm.modifiervalueentry;

import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.btm.cmd.tree.BaseConstructor;

public class ModifierValueEntry
{
	public static boolean hasPermission(Player player, BaseConstructor bc)
	{
		if(BaseConstructor.getPlugin().getValueEntry() != null)
		{
			Boolean ss = BaseConstructor.getPlugin().getValueEntry().getBooleanValueEntry(
					player.getUniqueId(),
					bc.getValueEntryPath(),
					BaseConstructor.getPlugin().getServername(),
					player.getWorld().getName());
			if(ss == null)
			{
				if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
				{
					return false;
				} else
				{
					return player.hasPermission(bc.getPermission());
				}
			}
			if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
			{
				return ss;
			} else
			{
				if(ss || player.hasPermission(bc.getPermission()))
				{
					return true;
				}
			}
			return false;
		}
		return player.hasPermission(bc.getPermission());
	}
	
	public static boolean hasPermission(Player player, Bypass.Permission bypassPermission)
	{
		if(BaseConstructor.getPlugin().getValueEntry() != null)
		{
			Boolean ss = BaseConstructor.getPlugin().getValueEntry().getBooleanValueEntry(
					player.getUniqueId(),
					bypassPermission.getValueLable(),
					BaseConstructor.getPlugin().getServername(),
					player.getWorld().getName());
			if(ss == null)
			{
				if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
				{
					return false;
				} else
				{
					return player.hasPermission(Bypass.get(bypassPermission));
				}
			}
			if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
			{
				return ss;
			} else
			{
				if(ss || player.hasPermission(Bypass.get(bypassPermission)))
				{
					return true;
				}
			}
			return false;
		}
		return player.hasPermission(Bypass.get(bypassPermission));
	}
	
	public static double getResult(UUID uuid, double value, Bypass.Counter countPermission)
	{
		double possibleAmount = value;
		if(BaseConstructor.getPlugin().getModifier() != null)
		{
			return BaseConstructor.getPlugin().getModifier().getResult(
					uuid,
					possibleAmount,
					countPermission.getModification());
		}
		return possibleAmount;
	}
}