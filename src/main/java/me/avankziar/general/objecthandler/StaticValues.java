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
		
		ENTITYTRANSPORT_TOBUNGEE = "btm:entitytransporttobungee",
		ENTITYTRANSPORT_TOSPIGOT = "btm:entitytransporttospigot",
		ENTITYTRANSPORT_ENTITYTOPOSITION = "entitytransport-entitytoposition",
		ENTITYTRANSPORT_ENTITYTOPLAYER = "entitytransport-entitytoplayer",
		
		//FirstSpawn Sending
		FIRSTSPAWN_TOBUNGEE = "btm:firstspawntobungee",
		FIRSTSPAWN_TOSPIGOT = "btm:firstspawntospigot",
		FIRSTSPAWN_PLAYERTOPOSITION = "firstspawn-playertoposition",
		FIRSTSPAWN_DOCOMMANDS = "firstspawn-docommands",
		
		//Home Sending
		HOME_TOBUNGEE = "btm:hometobungee",
		HOME_TOSPIGOT = "btm:hometospigot",
		HOME_PLAYERTOPOSITION = "home-playertoposition",
		
		//IFH
		IFH_TOBUNGEE = "btm:ifhtobungee",
		IFH_TOSPIGOT = "btm:ifhtospigot",
		IFH_PLAYERTOPOSITION = "ifh-playertoposition",
		
		//PORTAL Sending
		PORTAL_TOBUNGEE = "btm:portaltobungee",
		PORTAL_TOSPIGOT = "btm:portaltospigot",
		PORTAL_PLAYERTOPOSITION = "portal-playertoposition",
		PORTAL_SOUND = "portal-sound",
		PORTAL_UPDATE = "portal-update",
		
		//RandomTeleport
		RANDOMTELEPORT_TOBUNGEE = "btm:randomteleporttobungee",
		RANDOMTELEPORT_TOSPIGOT = "btm:randomteleporttospigot",
		RANDOMTELEPORT_PLAYERTOPOSITION = "randomteleport-playertoposition",
		
		//WARP Sending
		RESPAWN_TOBUNGEE = "btm:respawntobungee",
		RESPAWN_TOSPIGOT = "btm:respawntospigot",
		RESPAWN_PLAYERTOPOSITION = "respawn-playertoposition",
		
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
		TP_SILENT = "tp-silent",
		TP_ALL = "tp-all",
		TP_POS = "tp-pos",
		TP_SENDWORLD = "tp-sendworld",
		TP_FREE = "tp-free",
		TP_OCCUPIED = "tp-occupied",
		TP_SERVERQUITMESSAGE = "tp-serverquitmessage",
		TP_PLAYERTOPLAYER = "tp-playertoplayer",
		TP_SILENTPLAYERTOPLAYER = "tp-silentplayertoplayer",
		TP_PLAYERTOPOSITION = "tp-playertoposition",
		TP_FORBIDDENSERVER = "tp-forbiddenserver",
		TP_FORBIDDENWORLD = "tp-forbiddenworld",
		TP_TOGGLED = "tp-toggled",
		
		//WARP Sending
		WARP_TOBUNGEE = "btm:warptobungee",
		WARP_TOSPIGOT = "btm:warptospigot",
		WARP_PLAYERTOPOSITION = "warp-playertoposition",
		
		//SAFE Location Sending
		SAFE_TOBUNGEE = "btm:safetobungee",
		SAFE_TOSPIGOT = "btm:safetospigot",
		SAFE_CHECKPATH = "safe-checkpath",
		SAFE_CHECKEDPATH = "safe-checkedpath"
		;
	
	public static String
	
		BYPASS_COST = "btm.bypass.cost.",
		BYPASS_DELAY = "btm.bypass.delay.",
		BYPASS_FORBIDDEN_CREATE = "btm.bypass.forbidden.create.",
		BYPASS_FORBIDDEN_USE = "btm.bypass.forbidden.use.",
		
		//EntityTransport
		BYPASS_ENTITYTRANSPORT = "btm.bypass.entitytransport.admin",
		BYPASS_ENTITYTRANSPORT_ACCESSLIST = "btm.bypass.entitytransport.accesslist",
		BYPASS_ENTITYTRANSPORT_SERIALIZATION = "btm.bypass.entitytransport.serialization.",
		
		//Home
		PERM_HOME_OTHER = "btm.cmd.staff.home.home.other",
		PERM_HOMES_OTHER = "btm.cmd.staff.home.homes.other",
		
		PERM_HOME_COUNTHOMES_WORLD = "btm.count.home.world.",
		PERM_HOME_COUNTHOMES_SERVER = "btm.count.home.server.",
		PERM_HOME_COUNTHOMES_GLOBAL = "btm.count.home.global.",
		
		PERM_BYPASS_HOME = "btm.bypass.home.admin",
		PERM_BYPASS_HOME_TOOMANY = "btm.bypass.home.toomany",
		
		//Portal
		PERM_BYPASS_PORTAL = "btm.bypass.portal.admin",
		PERM_BYPASS_PORTALPLACER = "btm.bypass.portal.placer",
		PERM_PORTALS_OTHER = "btm.bypass.portal.portals.other",
		PERM_BYPASS_PORTAL_BLACKLIST = "btm.bypass.portal.blacklist",
		PERM_BYPASS_PORTAL_TOOMANY = "btm.bypass.portal.toomany",
		
		PERM_PORTAL_COUNTWARPS_WORLD = "btm.count.portal.world.",
		PERM_PORTAL_COUNTWARPS_SERVER = "btm.count.portal.server.",
		PERM_PORTAL_COUNTWARPS_GLOBAL = "btm.count.portal.global.",
		
		//SavePoint Bypass
		PERM_BYPASS_SAVEPOINT_OTHER = "btm.bypass.savepoint.other",
		PERM_BYPASS_SAVEPOINTS_OTHER = "btm.bypass.savepoint.savepoints.other",
	
		//Teleport
		PERM_BYPASS_TELEPORT_TPATOGGLE = "btm.bypass.tp.tpatoggle",
		PERM_BYPASS_TELEPORT_SILENT = "btm.bypass.tp.silent",
		
		//Warp
		PERM_WARP_OTHER = "btm.bypass.warp.warp.other",
		PERM_WARPS_OTHER = "btm.bypass.warp.warps.other",
		
		PERM_WARP_COUNTWARPS_WORLD = "btm.count.warp.world.",
		PERM_WARP_COUNTWARPS_SERVER = "btm.count.warp.server.",
		PERM_WARP_COUNTWARPS_GLOBAL = "btm.count.warp.global.",
				
		PERM_BYPASS_WARP = "btm.bypass.warp.admin",
		PERM_BYPASS_WARP_BLACKLIST = "btm.bypass.warp.blacklist",
		PERM_BYPASS_WARP_TOOMANY = "btm.bypass.warp.toomany";
}
