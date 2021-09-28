package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import main.java.me.avankziar.general.object.EntityTransport;
import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.MatchApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler.Type;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ForbiddenHandlerSpigot;
import main.java.me.avankziar.spigot.bungeeteleportmanager.object.BTMSettings;
import main.java.me.avankziar.spigot.bungeeteleportmanager.object.serialization.LivingEntitySerialization;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class EntityTransportHelper
{
	private BungeeTeleportManager plugin;
	public static final String OWNER = "EntityTransportOwner";
	public static final String MEMBERS = "EntityTransportMembers";
	
	public EntityTransportHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void entityTransportTo(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		if(args[0].equalsIgnoreCase("debug"))
		{
			LivingEntity target = plugin.getUtility().getFocusEntity(player);
			if(target == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NoEntityAtLeashOrFocused")));
				return;
			}
			if(!LivingEntitySerialization.canBeSerialized(target))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.EntityCannotBeSerialized")));
				return;
			}
			String data = LivingEntitySerialization.serializeEntityAsString(target);
			player.sendMessage(ChatApi.tl("&c============================================="));
			player.sendMessage(ChatApi.tl("&6LivingEntity Serialization:"));
			player.sendMessage(data);
			player.sendMessage(ChatApi.tl("&c============================================="));
			return;
		}
		if(!args[0].contains(":"))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NoSeperatorValue")));
			return;
		}
		String[] value = args[0].split(":");
		if(value.length != 2)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ValueLenghtNotRight")));
			return;
		}
		String param = value[0];
		String destination = value[1];
		if(!param.equalsIgnoreCase("h") && !param.equalsIgnoreCase("pl") && !param.equalsIgnoreCase("w"))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ParameterDontExist")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.ENTITYTRANSPORT, null)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.ENTITYTRANSPORT.getLower()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ForbiddenServerUse")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.ENTITYTRANSPORT, player, null)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.ENTITYTRANSPORT.getLower()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ForbiddenWorldUse")));
			return;
		}
		final LivingEntity target = plugin.getUtility().getFocusEntity(player);
		if(EntityTeleportHandler.isEntityTeleport(target))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.EntityIsARegisteredEntityTeleport")));
			return;
		}
		if(!LivingEntitySerialization.canBeSerialized(target))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.EntityCannotBeSerialized")));
			return;
		}
		if(!EntityTransportHandler.canSerialize(player, target))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.HasNoRightToSerializeThatType")
					.replace("%type%", target.getType().toString())));
			return;
		}
		if(EntityTransportHandler.hasOwner(player, target))
		{
			if(!EntityTransportHandler.isOwner(player, target) //&& !EntityTransportHandler.isMember(player, target)
					)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NotOwner")));
				return;
			}
		}
		if(EntityTransportHandler.usingTicket())
		{
			if(!EntityTransportHandler.hasTicket(player, target))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NotEnoughTickets")
						.replace("%actual%", String.valueOf(EntityTransportHandler.getTicket(player)))
						.replace("%needed%", String.valueOf(EntityTransportHandler.getTicket(target)))));
				return;
			}
			EntityTransportHandler.withdrawTickets(player, target);
		}
		if(param.equalsIgnoreCase("h"))
		{
			final Home home = (Home) plugin.getMysqlHandler().getData(Type.HOME, "`player_uuid` = ? AND `home_name` = ?",
					player.getUniqueId().toString(), destination);
			if(home == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.HomeNotExist")));
				return;
			}
			new EntityTransportHandler(plugin).sendEntityToPosition(player, target, home.getLocation(), param, destination);
			return;
		} else if(param.equalsIgnoreCase("pl"))
		{
			UUID targetuuid = Utility.convertNameToUUID(destination);
			if(targetuuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
				return;
			}
			if(!EntityTransportHandler.hasAccess(player, destination))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.HasNoAccess")
						.replace("%player%", destination)));
				return;
			}
			new EntityTransportHandler(plugin).sendEntityToPlayer(player, target, targetuuid, destination);
			return;
		} else if(param.equalsIgnoreCase("w"))
		{
			Warp warp = (Warp) plugin.getMysqlHandler().getData(Type.WARP, "`warpname` = ?", destination);
			String playeruuid = player.getUniqueId().toString();
			if(warp.getBlacklist() != null)
			{
				if(warp.getBlacklist().contains(playeruuid)
						&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_BLACKLIST))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWarp.YouAreOnTheBlacklist")
							.replace("%warpname%", warp.getName())));
					return;
				}
			}
			boolean owner = false;
			if(warp.getOwner() != null)
			{
				owner = warp.getOwner().equals(playeruuid);
			}
			if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP) && !owner)
			{
				if(warp.getPermission() != null)
				{
					if(!player.hasPermission(warp.getPermission()))
					{
						///Du hast dafür keine Rechte!
						player.spigot().sendMessage(ChatApi.tctl(
								plugin.getYamlHandler().getLang().getString("NoPermission")));
						return;
					}
				} else if(warp.isHidden() && !warp.getMember().contains(playeruuid))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotAMember")));
					return;
				}
			}
			new EntityTransportHandler(plugin).sendEntityToPosition(player, target, warp.getLocation(), param, destination);
			return;
		}
	}
	
	public void entityTransportSetAccess(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String otherplayername = args[0];
		UUID otherplayeruuid = Utility.convertNameToUUID(otherplayername);
		if(otherplayeruuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
			return;
		}
		EntityTransport.TargetAccess etta = (EntityTransport.TargetAccess) plugin.getMysqlHandler()
				.getData(MysqlHandler.Type.ENTITYTRANSPORT_TARGETACCESS, "`target_uuid` = ? AND `access_uuid` = ?",
						player.getUniqueId().toString(), otherplayeruuid.toString());
		if(etta == null)
		{
			etta = new EntityTransport(). new TargetAccess(player.getUniqueId().toString(), otherplayeruuid.toString());
			plugin.getMysqlHandler().create(MysqlHandler.Type.ENTITYTRANSPORT_TARGETACCESS, etta);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.AddAccess")
					.replace("%player%", otherplayername)));
		} else
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ENTITYTRANSPORT_TARGETACCESS, 
					"`target_uuid` = ? AND `access_uuid` = ?",
					player.getUniqueId().toString(), otherplayeruuid.toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.AddAccess")
					.replace("%player%", otherplayername)));
		}
	}
	
	public void entityTransportAccessList(Player player, String[] args)
	{
		if(args.length != 1 && args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String playername = player.getName();
		String playeruuid = player.getUniqueId().toString();
		if(args.length >= 1
				&& (player.hasPermission(StaticValues.BYPASS_ENTITYTRANSPORT_ACCESSLIST) || args[1].equals(player.getName())))
		{
			playername = args[1];
			UUID uuid = Utility.convertNameToUUID(args[0]);
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			playeruuid = uuid.toString();
		}
		ArrayList<EntityTransport.TargetAccess> list = new ArrayList<>();
		list = ConvertHandler.convertListVIII(plugin.getMysqlHandler().getList(MysqlHandler.Type.ENTITYTRANSPORT_TARGETACCESS,
						"`id` ASC", 0, 55, "`target_uuid` = ?", playeruuid));
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NoAccessInList")));
			return;
		}
		ArrayList<BaseComponent> bclist = new ArrayList<>();
		for(EntityTransport.TargetAccess etta : list)
		{
			String otherplayer = Utility.convertUUIDToName(etta.getAccessUUID());
			if(otherplayer == null)
			{
				continue;
			}
			bclist.add(ChatApi.apiChat("&e"+otherplayer+"&f, ",
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.ENTITYTRANSPORT_SETACCESS),
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("CmdEntityTransport.AccessListHover")
					.replace("%player%", otherplayer)));
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.AccessListHeadline")
				.replace("%player%", playername)));
		TextComponent tc = new TextComponent();
		tc.setExtra(bclist);
		player.spigot().sendMessage(tc);
	}
	
	public void entityTransportSetOwner(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String playername = args[0];
		UUID uuid = Utility.convertNameToUUID(playername);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		final LivingEntity target = plugin.getUtility().getFocusEntity(player);
		if(EntityTeleportHandler.isEntityTeleport(target))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.EntityIsARegisteredEntityTeleport")));
			return;
		}
		if(!LivingEntitySerialization.canBeSerialized(target))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.EntityCannotBeSerialized")));
			return;
		}
		if(!EntityTransportHandler.canSerialize(player, target))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.HasNoRightToSerializeThatType")
					.replace("%type%", target.getType().toString())));
			return;
		}
		if(!EntityTransportHandler.hasOwner(player, target))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.HasNoOwner")));
			return;
		}
		if(!EntityTransportHandler.isOwner(player, target) //&& !EntityTransportHandler.isMember(player, target)
				)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NotOwner")));
			return;
		}
		PersistentDataContainer pdc = target.getPersistentDataContainer();
		NamespacedKey nowner = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTransportHelper.OWNER);
		pdc.remove(nowner);
		pdc.set(nowner,	PersistentDataType.STRING, uuid.toString());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.SetOwner")
				.replace("%player%", uuid.toString())));
	}
	
	public void entityTransportBuyTickets(CommandSender sender, String[] args)
	{
		if(args.length != 1 && args.length != 2 && args.length != 3)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			sender.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		int amount = 0;
		if(!MatchApi.isInteger(args[0]))
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%arg%", args[0])));
			return;
		}
		amount = Integer.parseInt(args[0]);
		String playername = null;
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			playername = player.getName();
		}
		if(args.length >= 2)
		{
			playername = args[1];
		}
		UUID uuid = Utility.convertNameToUUID(playername);
		if(uuid == null)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		boolean mustpay = false;
		if(args.length >= 3)
		{
			if(args[2].equalsIgnoreCase("true"))
			{
				mustpay = true;
			}
		}
		EntityTransportHandler.addingTickets(sender, uuid.toString(), amount, mustpay);
	}
}