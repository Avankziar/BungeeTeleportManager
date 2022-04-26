package main.java.me.avankziar.spigot.btm.manager.entitytransport;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.EntityTransport;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.object.serialization.LivingEntitySerialization;

public class EntityTransportHandler
{
	private BungeeTeleportManager plugin;
	public static LinkedHashMap<EntityType, Integer> ticketList = new LinkedHashMap<>();
	
	public EntityTransportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public static void initTicketList()
	{
		ArrayList<String> list = (ArrayList<String>) BungeeTeleportManager.getPlugin().getYamlHandler().getConfig()
				.getStringList("EntityTransport.TicketList");
		if(list == null)
		{
			return;
		}
		for(String s : list)
		{
			if(!s.contains(";"))
			{
				continue;
			}
			String[] split = s.split(";");
			EntityType et = EntityType.AXOLOTL;
			int i = 1;
			try
			{
				et = EntityType.valueOf(split[0]);
				i = Integer.parseInt(split[1]);
			} catch (Exception e)
			{
				continue;
			}
			if(!ticketList.containsKey(et))
			{
				ticketList.put(et, i);
			}
		}
	}
	
	public static boolean canSerialize(Player player, Entity entity)
	{
		if(player.hasPermission(StaticValues.BYPASS_ENTITYTRANSPORT_SERIALIZATION+entity.getType().toString().toLowerCase()))
		{
			return true;
		}
		return false;
	}
	
	public static boolean hasOwner(Player player, LivingEntity entity)
	{
		PersistentDataContainer pdc = entity.getPersistentDataContainer();
		NamespacedKey nowner = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTransportHelper.OWNER);
		if(pdc.has(nowner, PersistentDataType.STRING))
		{
			return true;
		}
		if(entity.getCustomName() != null)
		{
			return true;
		}
		return false;
	}
	
	public static boolean isOwner(Player player, Entity entity)
	{
		PersistentDataContainer pdc = entity.getPersistentDataContainer();
		NamespacedKey nowner = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTransportHelper.OWNER);
		if(!pdc.has(nowner, PersistentDataType.STRING))
		{
			return false;
		}
		String owner = pdc.get(nowner, PersistentDataType.STRING);
		if(!owner.equals(player.getUniqueId().toString()))
		{
			return false;
		}
		return true;
	}
	
	public static boolean isMember(Player player, Entity entity)
	{
		PersistentDataContainer pdc = entity.getPersistentDataContainer();
		NamespacedKey nmembers = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTransportHelper.MEMBERS);
		if(!pdc.has(nmembers, PersistentDataType.STRING))
		{
			return false;
		}
		String mem = pdc.get(nmembers, PersistentDataType.STRING);
		if(mem.contains("?"))
		{
			String[] split = mem.split("?");
			for(String s : split)
			{
				if(s.equals(player.getUniqueId().toString()))
				{
					return true;
				}
			}
		} else
		{
			if(mem.equals(player.getUniqueId().toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasAccess(Player player, String othername)
	{
		UUID otheruuid = Utility.convertNameToUUID(othername);
		if(otheruuid == null)
		{
			return false;
		}
		if(BungeeTeleportManager.getPlugin().getMysqlHandler().exist(
				MysqlHandler.Type.ENTITYTRANSPORT_TARGETACCESS, "`target_uuid` = ? AND `access_uuid` = ?",
				otheruuid, player.getUniqueId().toString()))
		{
			return true;
		}
		return false;
	}
	
	public static boolean usingTicket()
	{
		return BungeeTeleportManager.getPlugin().getYamlHandler().getConfig()
				.getBoolean("Use.EntityTransport.TicketMechanic", false);
	}
	
	public static int getTicket(LivingEntity ley)
	{
		return (ticketList.get(ley.getType()) != null) 
				? ticketList.get(ley.getType()) 
				: BungeeTeleportManager.getPlugin().getYamlHandler().getConfig().getInt("EntityTransport.DefaultTicketPerEntity", 1);
		
	}
	
	public static int getTicket(Player player)
	{
		return ((EntityTransport.Ticket) BungeeTeleportManager.getPlugin().getMysqlHandler()
				.getData(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, "`player_uuid` = ?", player.getUniqueId().toString())).getActualAmount();
		
	}
	
	public static boolean hasTicket(Player player, LivingEntity ley)
	{
		int neededTicketAmount = getTicket(ley);
		if(!BungeeTeleportManager.getPlugin().getMysqlHandler()
				.exist(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, "`player_uuid` = ?", player.getUniqueId().toString()))
		{
			BungeeTeleportManager.getPlugin().getMysqlHandler()
				.create(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, 
					new EntityTransport(). new Ticket(player.getUniqueId().toString(), 0, 0, 0.0));
		}
		int actualAmount = getTicket(player);
		if(actualAmount >= neededTicketAmount)
		{
			return true;
		}
		return false;
	}
	
	public static void addingTickets(CommandSender sender, String targetuuid, String targetname, int amount, boolean mustpay)
	{
		BungeeTeleportManager plugin = BungeeTeleportManager.getPlugin();
		EntityTransport.Ticket ticket = null;
		if(!plugin.getMysqlHandler()
				.exist(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, "`player_uuid` = ?", targetuuid))
		{
			ticket = new EntityTransport(). new Ticket(targetuuid, 0, 0, 0.0);
			plugin.getMysqlHandler().create(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, ticket);
		} else
		{
			ticket = (EntityTransport.Ticket) plugin.getMysqlHandler()
					.getData(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, "`player_uuid` = ?", targetuuid);
		}
		double sum = new ConfigHandler(plugin).getCostUse(Mechanics.ENTITYTRANSPORT)*(double) amount;
		boolean hasPerm = true;
		Player player = Bukkit.getPlayer(UUID.fromString(targetuuid));
		if(player != null)
		{
			hasPerm = player.hasPermission(StaticValues.BYPASS_COST+Mechanics.ENTITYTRANSPORT.getLower());
		}
		if(sender instanceof ConsoleCommandSender)
		{
			hasPerm = true;
		}
		if(sum > 0.0 
				&& (!hasPerm || mustpay)
				&& BungeeTeleportManager.getPlugin().getEco() != null)
		{
			Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
					plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
			if(main == null || main.getBalance() < sum)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
				return;
			}
			String category = plugin.getYamlHandler().getLang().getString("Economy.ETrCategory");
			String comment = plugin.getYamlHandler().getLang().getString("Economy.ETrComment")
					.replace("%ticket%", String.valueOf(amount));
			EconomyAction ea = plugin.getEco().withdraw(main, sum, 
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
			if(!ea.isSuccess())
			{
				player.sendMessage(ChatApi.tl(ea.getDefaultErrorMessage()));
				return;
			}
			ticket.setSpendedMoney(ticket.getSpendedMoney()+sum);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.BuyTickets")
					.replace("%format%", plugin.getEco().format(sum, plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))));			
		}
		ticket.setActualAmount(ticket.getActualAmount()+amount);
		ticket.setTotalBuyedAmount(ticket.getTotalBuyedAmount()+amount);
		plugin.getMysqlHandler()
			.updateData(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, ticket, "`player_uuid` = ?", ticket.getPlayerUUID());		
	}
	
	public static void withdrawTickets(Player player, LivingEntity ley)
	{
		int neededTicketAmount = getTicket(ley);
		EntityTransport.Ticket ticket = (EntityTransport.Ticket) BungeeTeleportManager.getPlugin().getMysqlHandler()
				.getData(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, "`player_uuid` = ?", player.getUniqueId().toString());
		ticket.setActualAmount(ticket.getActualAmount()-neededTicketAmount);
		BungeeTeleportManager.getPlugin().getMysqlHandler()
			.updateData(MysqlHandler.Type.ENTITYTRANSPORT_TICKET, ticket, "`player_uuid` = ?", ticket.getPlayerUUID());
	}
	
	public void sendEntityToPosition(Player player, LivingEntity ley, ServerLocation loc, String shortcut, String target)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(loc.getServer().equals(cfgh.getServer()))
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ley.teleport(ConvertHandler.getLocation(loc));
					switch(shortcut)
					{
					default:
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ToPosition")));
						break;
					case "h":
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ToHome")
								.replace("%target%", target)));
						break;
					case "w":
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ToWarp")
								.replace("%target%", target)));
						break;
					}
				}
			}.runTaskLater(plugin, 1);
		} else
		{
			String data = LivingEntitySerialization.serializeEntityAsString(ley);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.ENTITYTRANSPORT_ENTITYTOPOSITION);
				out.writeUTF(data);
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
	        player.sendPluginMessage(plugin, StaticValues.ENTITYTRANSPORT_TOBUNGEE, stream.toByteArray());
	        ley.remove();
	        switch(shortcut)
			{
			default:
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ToPosition")));
				break;
			case "h":
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ToHome")
						.replace("%target%", target)));
				break;
			case "w":
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ToWarp")
						.replace("%target%", target)));
				break;
			}
		}
        return;
	}
	
	public void sendEntityToPlayer(Player player, LivingEntity ley, UUID uuid, String target)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		Player targetplayer = Bukkit.getPlayer(uuid);
		if(targetplayer != null)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ley.teleport(targetplayer.getLocation());
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ToPlayer")
							.replace("%target%", target)));
				}
			}.runTaskLater(plugin, 1);
		} else
		{
			String data = LivingEntitySerialization.serializeEntityAsString(ley);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.ENTITYTRANSPORT_ENTITYTOPLAYER);
				out.writeUTF(data);
				out.writeUTF(uuid.toString());
				out.writeUTF(cfgh.getServer());
				out.writeUTF(ley.getLocation().getWorld().getName());
				out.writeDouble(ley.getLocation().getX());
				out.writeDouble(ley.getLocation().getY());
				out.writeDouble(ley.getLocation().getZ());
				out.writeFloat(ley.getLocation().getYaw());
				out.writeFloat(ley.getLocation().getPitch());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.ENTITYTRANSPORT_TOBUNGEE, stream.toByteArray());
	        ley.remove();
	        player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ToPlayer")
					.replace("%target%", target)));
		}
        return;
	}
}