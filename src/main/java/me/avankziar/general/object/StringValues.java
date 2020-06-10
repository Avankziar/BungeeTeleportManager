package main.java.me.avankziar.general.object;

public class StringValues
{
	final public static String
	
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
	
	//Back Cmd
	CMD_BACK = "back",
	CMD_DEATHBACK = "deathback",
	
	//Back Perm 
	PERM_BACK = "btm.cmd.back",
	PERM_DEATHBACK = "btm.cmd.deathback",
	
	//Back Suggest
	SUGGEST_BACK = "/back",
	SUGGEST_DEATHBACK = "/deathback",
	
	//Home Sending
	HOME_TOBUNGEE = "btm:hometobungee",
	HOME_TOSPIGOT = "btm:hometospigot",
	HOME_PLAYERTOPOSITION = "home-playertoposition",
	
	//Home Cmd
	CMD_HOME_CREATE = "homecreate",
	CMD_HOME_REMOVE = "homeremove",
	CMD_HOME_HOMES = "homes",
	CMD_HOME_HOME = "home",
	CMD_HOME_LIST = "homelist",			
	
	//Home Perm
	PERM_HOME_CREATE = "btm.cmd.homecreate",
	PERM_HOME_REMOVE = "btm.cmd.homeremove",
	PERM_HOME_HOMES = "btm.cmd.homes",
	PERM_HOME_HOME = "btm.cmd.home",
	PERM_HOME_LIST = "btm.cmd.homelist",
	PERM_HOME_COUNTHOMES_WORLD = "btm.count.home.world.",
	PERM_HOME_COUNTHOMES_SERVER = "btm.count.home.server.",
	PERM_HOME_COUNTHOMES_GLOBAL = "btm.count.home.global.",
	
	//Home Suggest
	SUGGEST_HOMECREATE = "/homecreate <homename>",
	SUGGEST_HOMEREMOVE = "/homeremove <homename>",
	SUGGEST_HOMES = "/homes",
	SUGGEST_HOME = "/home <homename>",
	SUGGEST_HOMELIST = "/homelist [pagenumber]",
	
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
	TP_SENDLIST = "tp-sendlist",
	TP_TOGGLED = "tp-toggled",
	
	//Teleport cmd
	CMD_TELEPORT_TPA = "tpa",
	CMD_TELEPORT_TPAHERE = "tpahere",
	CMD_TELEPORT_TPACCEPT = "tpaccept",
	CMD_TELEPORT_TPDENY = "tpdeny",
	CMD_TELEPORT_TPCANCEL = "tpcancel",
	CMD_TELEPORT_TPTOGGLE = "tptoggle",
	CMD_TELEPORT_TP = "tp",
	CMD_TELEPORT_TPHERE = "tphere",
	CMD_TELEPORT_TPALL = "tpall",
	CMD_TELEPORT_TPPOS = "tppos",
	
	//Teleport Perm
	PERM_TELEPORT_TPA = "btm.cmd.tpa",
	PERM_TELEPORT_TPAHERE = "btm.cmd.tpahere",
	PERM_TELEPORT_TPACCEPT = "btm.cmd.tpaccept",
	PERM_TELEPORT_TPDENY = "btm.cmd.tpdeny",
	PERM_TELEPORT_TPCANCEL = "btm.cmd.tpcancel",
	PERM_TELEPORT_TPTOGGLE = "btm.cmd.tptoggle",
	PERM_TELEPORT_TP = "btm.cmd.tp",
	PERM_TELEPORT_TPHERE = "btm.cmd.tphere",
	PERM_TELEPORT_TPALL = "btm.cmd.tpall",
	PERM_TELEPORT_TPPOS = "btm.cmd.tppos",
	
	//Teleport Suggestion
	SUGGEST_TPA = "/tpa <player>",
	SUGGEST_TPAHERE = "/tpahere <player>",
	SUGGEST_TPACCEPT = "/tpaccept <player>",
	SUGGEST_TPDENY = "/tpdeny <player>",
	SUGGEST_TPCANCEL = "/tpcancel",
	SUGGEST_TPTOGGLE = "/tptoggle",
	SUGGEST_TP = "/tp",
	SUGGEST_TPHERE = "/tphere",
	SUGGEST_TPALL = "/tpall",
	SUGGEST_TPPOS = "/tppos",
	
	//WARP Sending
	WARP_TOBUNGEE = "btm:warptobungee",
	WARP_TOSPIGOT = "btm:warptospigot",
	WARP_PLAYERTOPOSITION = "warp-playertoposition",
	
	//Warp command
	CMD_WARP_CREATE = "warpcreate",
	CMD_WARP_REMOVE = "warpremove",
	CMD_WARP_LIST = "warplist",
	CMD_WARP_TO = "warp",
	CMD_WARP_INFO = "warpinfo",
	CMD_WARP_SETNAME = "warpsetname",
	CMD_WARP_SETPOSITION = "warpsetposition",
	CMD_WARP_SETOWNER = "warpsetowner",
	CMD_WARP_SETPERMISSION = "warpsetpermission",
	CMD_WARP_SETPASSWORD = "warpsetpassword",
	CMD_WARP_SETPRICE = "warpsetprice",
	CMD_WARP_HIDDEN = "warphidden",
	CMD_WARP_COMMON = "warpcommon",
	CMD_WARP_ADDMEMBER = "warpaddmember",
	CMD_WARP_REMOVEMEMBER = "warpremovemember",
	
	//Warp Permission
	PERM_WARP_CREATE = "btm.cmd.warpcreate",
	PERM_WARP_REMOVE = "btm.cmd.warpremove",
	PERM_WARP_LIST = "btm.cmd.warplist",
	PERM_WARP_TO = "btm.cmd.warp",
	PERM_WARP_INFO = "btm.cmd.warpinfo",
	PERM_WARP_SETNAME = "btm.cmd.warpsetname",
	PERM_WARP_SETPOSITION = "btm.cmd.warpsetposition",
	PERM_WARP_SETOWNER = "btm.cmd.warpsetowner",
	PERM_WARP_SETPERMISSION = "btm.cmd.warpsetpermission",
	PERM_WARP_SETPASSWORD = "btm.cmd.warppassword",
	PERM_WARP_SETPRICE = "btm.cmd.warpprice",
	PERM_WARP_HIDDEN = "btm.cmd.warphidden",
	PERM_WARP_COMMON = "btm.cmd.warpcommon",
	PERM_WARP_ADDMEMBER = "btm.cmd.warpaddmember",
	PERM_WARP_REMOVEMEMBER = "btm.cmd.warpremovemember",
	PERM_WARP_COUNTHOMES_WORLD = "btm.count.warp.world.",
	PERM_WARP_COUNTWARPS_SERVER = "btm.count.warp.server.",
	PERM_WARP_COUNTWARPS_GLOBAL = "btm.count.warp.global.",
	
	//warp suggest
	SUGGEST_WARP_CREATE = "",
	SUGGEST_WARP_REMOVE = "",
	SUGGEST_WARP_LIST = "",
	SUGGEST_WARP_TO = "",
	SUGGEST_WARP_INFO = "",
	SUGGEST_WARP_SETNAME = "",
	SUGGEST_WARP_SETPOSITION = "",
	SUGGEST_WARP_SETOWNER = "",
	SUGGEST_WARP_SETPERMISSION = "",
	SUGGEST_WARP_SETPASSWORT = "",
	SUGGEST_WARP_SETPRICE = "",
	SUGGEST_WARP_HIDDEN = "",
	SUGGEST_WARP_COMMON = "",
	SUGGEST_WARP_ADDMEMBER = "",
	SUGGEST_WARP_REMOVEMEMBER = "",
	
	//Bypass Perm
	PERM_BYPASS_BACK = "btm.bypass.cost.back",
	PERM_BYPASS_HOME = "btm.bypass.homeadmin",
	PERM_BYPASS_HOME_VIP = "btm.bypass.homevip",
	PERM_BYPASS_HOME_COST = "btm.bypass.cost.home",
	PERM_BYPASS_TELEPORT = "btm.bypass.cost.teleport",
	PERM_BYPASS_WARP = "btm.bypass.warpadmin",
	PERM_BYPASS_WARP_VIP = "btm.bypass.warpvip",
	PERM_BYPASS_WARP_COST = "btm.bypass.cost.warp"
	;
}
