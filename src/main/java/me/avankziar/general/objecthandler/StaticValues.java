package main.java.me.avankziar.general.objecthandler;

public class StaticValues
{
	final public static String
		//General
		GENERAL_TOBUNGEE = "btm:generaltobungee",
		GENERAL_TOSPIGOT = "btm:generaltospigot",
		GENERAL_SENDLIST = "general-sendlist",
		GENERAL_SENDSETTING = "general-sendsetting",
		//Custom
		CUSTOM_TOBUNGEE = "btm:customtobungee",
		CUSTOM_TOSPIGOT = "btm:customtospigot",
		CUSTOM_PLAYERTOPLAYER = "custom-playertoplayer",
		CUSTOM_PLAYERTOPOSITION = "custom-playertoposition",
		
		//Back Sending
		BACK_TOBUNGEE = "btm:backtobungee",
		BACK_TOSPIGOT = "btm:backtospigot",
		BACK_SENDOBJECT = "back-object",
		BACK_SENDDEATHOBJECT = "back-deathobject",
		BACK_SENDJOINOBJECT = "back-joinobject",
		BACK_SENDPLAYERBACK = "back-playertopos",
		BACK_SENDPLAYERDEATHBACK = "back-playertoposdeath",
		BACK_REQUESTNEWBACK = "back-requestnewback",
		BACK_NODEATHBACK = "back-nodeathback",
		
		//Home Sending
		HOME_TOBUNGEE = "btm:hometobungee",
		HOME_TOSPIGOT = "btm:hometospigot",
		HOME_PLAYERTOPOSITION = "home-playertoposition",
		
		//RandomTeleport
		RANDOMTELEPORT_TOBUNGEE = "btm:randomteleporttobungee",
		RANDOMTELEPORT_TOSPIGOT = "btm:randomteleporttospigot",
		RANDOMTELEPORT_PLAYERTOPOSITION = "randomteleport-playertoposition",
		
		//SavePoint Sending
		SAVEPOINT_TOBUNGEE = "btm:savepointtobungee",
		SAVEPOINT_TOSPIGOT = "btm:savepointtospigot",
		SAVEPOINT_PLAYERTOPOSITION = "savepoint-playertoposition",
		
		//Teleport Sending Bungee/Spigot
		TP_TOBUNGEE = "btm:teleporttobungee",
		TP_TOSPIGOT = "btm:teleporttospigot",
		TP_SENDMESSAGE = "tp-sendmessage",
		TP_SENDTEXTCOMPONENT = "tp-sendtextcomponent",
		TP_EXISTPENDING = "tp-existpending",
		TP_OBJECT = "tp-object",
		TP_CANCELINVITE = "tp-cancelinvite",
		TP_ACCEPT = "tp-accept",
		TP_DENY = "tp-deny",
		TP_CANCEL = "tp-cancel",
		TP_FORCE = "tp-force",
		TP_ALL = "tp-all",
		TP_POS = "tp-pos",
		TP_SENDWORLD = "tp-sendworld",
		TP_FREE = "tp-free",
		TP_OCCUPIED = "tp-occupied",
		TP_SERVERQUITMESSAGE = "tp-serverquitmessage",
		TP_PLAYERTOPLAYER = "tp-playertoplayer",
		TP_PLAYERTOPOSITION = "tp-playertoposition",
		TP_FORBIDDENSERVER = "tp-forbiddenserver",
		TP_FORBIDDENWORLD = "tp-forbiddenworld",
		TP_TOGGLED = "tp-toggled",
		
		//WARP Sending
		WARP_TOBUNGEE = "btm:warptobungee",
		WARP_TOSPIGOT = "btm:warptospigot",
		WARP_PLAYERTOPOSITION = "warp-playertoposition"
		;
	
	public static String
		
		//Back
		PERM_BYPASS_BACK_COST = "btm.bypass.back.cost",
		PERM_BYPASS_BACK_DELAY = "btm.bypass.back.delay",
		
		//Deahback
		PERM_BYPASS_DEATHBACK_DELAY = "btm.bypass.deathback.delay",
		
		//Costum
		PERM_BYPASS_CUSTOM_DELAY = "btm.bypass.custom.delay",
		
		//Home
		PERM_HOME_OTHER = "btm.cmd.staff.home.home.other",
		PERM_HOMES_OTHER = "btm.cmd.staff.home.homes.other",
		
		PERM_HOME_COUNTHOMES_WORLD = "btm.count.home.world.",
		PERM_HOME_COUNTHOMES_SERVER = "btm.count.home.server.",
		PERM_HOME_COUNTHOMES_GLOBAL = "btm.count.home.global.",
		
		PERM_BYPASS_HOME = "btm.bypass.home.admin",
		PERM_BYPASS_HOME_TOOMANY = "btm.bypass.home.toomany",
		PERM_BYPASS_HOME_COST = "btm.bypass.home.cost",
		PERM_BYPASS_HOME_FORBIDDEN = "btm.bypass.home.forbidden",
		PERM_BYPASS_HOME_DELAY = "btm.bypass.home.delay",
		
		//RandomTeleport
		PERM_BYPASS_RANDOMTELEPORT = "btm.bypass.randomteleport.admin",
		PERM_BYPASS_RANDOMTELEPORT_DELAY = "btm.bypass.randomteleport.delay",
		PERM_BYPASS_RANDOMTELEPORT_COST = "btm.bypass.randomteleport.cost",
		
		//SavePoint Bypass
		PERM_BYPASS_SAVEPOINT_OTHER = "btm.bypass.savepoint.other",
		PERM_BYPASS_SAVEPOINT_DELAY = "btm.bypass.savepoint.delay",
	
		//Teleport
		PERM_BYPASS_TELEPORT_TPATOGGLE = "btm.bypass.tp.tpatoggle",
		PERM_BYPASS_TELEPORT_COST = "btm.bypass.tp.cost",
		PERM_BYPASS_TELEPORT_FORBIDDEN = "btm.bypass.tp.forbidden",
		PERM_BYPASS_TELEPORT_SILENT = "btm.bypass.tp.silent",
		PERM_BYPASS_TELEPORT_DELAY = "btm.bypass.tp.delay",
		
		
		
		//Warp
		PERM_WARP_OTHER = "btm.bypass.warp.warp.other",
		PERM_WARPS_OTHER = "btm.bypass.warp.warps.other",
		
		PERM_WARP_COUNTWARPS_WORLD = "btm.count.warp.world.",
		PERM_WARP_COUNTWARPS_SERVER = "btm.count.warp.server.",
		PERM_WARP_COUNTWARPS_GLOBAL = "btm.count.warp.global.",
				
		PERM_BYPASS_WARP = "btm.bypass.warp.admin",
		PERM_BYPASS_WARP_BLACKLIST = "btm.bypass.warp.blacklist",
		PERM_BYPASS_WARP_TOOMANY = "btm.bypass.warp.toomany",
		PERM_BYPASS_WARP_COST = "btm.bypass.warp.cost",
		PERM_BYPASS_WARP_FORBIDDEN = "btm.bypass.warp.forbidden",
		PERM_BYPASS_WARP_DELAY = "btm.bypass.warp.delay";
}
