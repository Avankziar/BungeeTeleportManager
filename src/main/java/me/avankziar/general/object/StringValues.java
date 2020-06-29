package main.java.me.avankziar.general.object;

public class StringValues
{
	final public static String
	//Btm 
	CMD_BTM = "btm",
	CMD_RELOAD = "btm reload",
	
	PERM_BTM = "btm.cmd.btm",
	PERM_RELOAD = "btm.cmd.reload",
	
	SUGGEST_BTM = "/btm",
	SUGGEST_RELOAD = "/btm reload",
	
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
	
	//Back Cmd
	CMD_BACK = "back",
	CMD_DEATHBACK = "deathback",
	
	//Back Perm 
	PERM_BACK = "btm.cmd.back.back",
	PERM_DEATHBACK = "btm.cmd.back.deathback",
	
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
	CMD_HOME_DELETESERVERWORLD = "homesdeleteserverworld",
	CMD_HOMES = "homes",
	CMD_HOME = "home",
	CMD_HOME_LIST = "homelist",			
	
	//Home Perm
	PERM_HOME_CREATE = "btm.cmd.user.home.create",
	PERM_HOME_REMOVE = "btm.cmd.user.home.remove",
	PERM_HOMES_SELF = "btm.cmd.user.home.homes.self",
	PERM_HOMES_OTHER = "btm.cmd.staff.home.homes.other",
	PERM_HOME_SELF = "btm.cmd.user.home.home.self",
	PERM_HOME_OTHER = "btm.cmd.staff.home.home.other",
	PERM_HOME_LIST = "btm.cmd.staff.home.home.list",
	PERM_HOME_HOMESDELETESERVERWOLRD = "btm.cmd.admin.home.homesdeleteserverworld",
	PERM_HOME_COUNTHOMES_WORLD = "btm.count.home.world.",
	PERM_HOME_COUNTHOMES_SERVER = "btm.count.home.server.",
	PERM_HOME_COUNTHOMES_GLOBAL = "btm.count.home.global.",
	
	//Home Suggest
	SUGGEST_HOMECREATE = "/homecreate <homename>",
	SUGGEST_HOMEREMOVE = "/homeremove <homename>",
	SUGGEST_HOMESDELETESERVERWORLD = "/homesdeleteserverworld",
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
	CMD_TELEPORT_TPCANCEL = "tpaquit",
	CMD_TELEPORT_TPTOGGLE = "tpatoggle",
	CMD_TELEPORT_TPAIGNORE = "tpaignore",
	CMD_TELEPORT_TP = "tp",
	CMD_TELEPORT_TPHERE = "tphere",
	CMD_TELEPORT_TPALL = "tpall",
	CMD_TELEPORT_TPPOS = "tppos",
	
	//Teleport Perm
	PERM_TELEPORT_TPA = "btm.cmd.user.tp.tpa",
	PERM_TELEPORT_TPAHERE = "btm.cmd.user.tp.tpahere",
	PERM_TELEPORT_TPACCEPT = "btm.cmd.user.tp.tpaccept",
	PERM_TELEPORT_TPDENY = "btm.cmd.user.tp.tpdeny",
	PERM_TELEPORT_TPCANCEL = "btm.cmd.user.tp.tpaquit",
	PERM_TELEPORT_TPTOGGLE = "btm.cmd.user.tp.tpatoggle",
	PERM_TELEPORT_TPAIGNORE = "btm.cmd.user.tp.tpaignore",
	PERM_TELEPORT_TP = "btm.cmd.staff.tp.tp",
	PERM_TELEPORT_TPHERE = "btm.cmd.staff.tp.tphere",
	PERM_TELEPORT_TPALL = "btm.cmd.admin.tp.tpall",
	PERM_TELEPORT_TPPOS = "btm.cmd.staff.tptppos",
	
	//Teleport Suggestion
	SUGGEST_TPA = "/tpa <player>",
	SUGGEST_TPAHERE = "/tpahere <player>",
	SUGGEST_TPACCEPT = "/tpaccept <player>",
	SUGGEST_TPDENY = "/tpdeny <player>",
	SUGGEST_TPCANCEL = "/tpaquit",
	SUGGEST_TPTOGGLE = "/tpatoggle",
	SUGGEST_TPAIGNORE = "/tpaignore <player>",
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
	CMD_WARP_WARPS = "warps",
	CMD_WARP_INFO = "warpinfo",
	CMD_WARP_SETNAME = "warpsetname",
	CMD_WARP_SETPOSITION = "warpsetposition",
	CMD_WARP_SETOWNER = "warpsetowner",
	CMD_WARP_SETPERMISSION = "warpsetpermission",
	CMD_WARP_SETPASSWORD = "warpsetpassword",
	CMD_WARP_SETPRICE = "warpsetprice",
	CMD_WARP_HIDDEN = "warphidden",
	CMD_WARP_ADDMEMBER = "warpaddmember",
	CMD_WARP_REMOVEMEMBER = "warpremovemember",
	CMD_WARP_ADDBLACKLIST = "warpaddblacklist",
	CMD_WARP_REMOVEBLACKLIST = "warpremoveblacklist",
	
	//Warp Permission
	PERM_WARP_CREATE = "btm.cmd.user.warp.create",
	PERM_WARP_REMOVE = "btm.cmd.user.warp.remove",
	PERM_WARP_LIST = "btm.cmd.user.warp.list",
	PERM_WARP_TO = "btm.cmd.user.warp.warp",
	PERM_WARP_WARPS = "btm.cmd.user.warp.warps",
	PERM_WARP_INFO = "btm.cmd.user.warp.info",
	PERM_WARP_SETNAME = "btm.cmd.user.warp.setname",
	PERM_WARP_SETPOSITION = "btm.cmd.user.warp.setposition",
	PERM_WARP_SETOWNER = "btm.cmd.user.warp.setowner",
	PERM_WARP_SETPERMISSION = "btm.cmd.admin.warp.setpermission",
	PERM_WARP_SETPASSWORD = "btm.cmd.user.warp.setpassword",
	PERM_WARP_SETPRICE = "btm.cmd.user.warp.setprice",
	PERM_WARP_HIDDEN = "btm.cmd.user.warp.hidden",
	PERM_WARP_ADDMEMBER = "btm.cmd.user.warp.addmember",
	PERM_WARP_REMOVEMEMBER = "btm.cmd.user.warp.removemember",
	PERM_WARP_ADDBLACKLIST = "btm.cmd.user.warp.addblacklist",
	PERM_WARP_REMOVEBLACKLIST = "btm.cmd.user.warp.removeblacklist",
	PERM_WARP_COUNTWARPS_WORLD = "btm.count.warp.world.",
	PERM_WARP_COUNTWARPS_SERVER = "btm.count.warp.server.",
	PERM_WARP_COUNTWARPS_GLOBAL = "btm.count.warp.global.",
	
	//warp suggest
	SUGGEST_WARP_CREATE = "/warpcreate <warpname>",
	SUGGEST_WARP_REMOVE = "/warpremove <warpname>",
	SUGGEST_WARP_LIST = "/warplist [pagenumber]",
	SUGGEST_WARP_TO = "/warp <warpname>",
	SUGGEST_WARP_WARPS = "/warps [pagenumber]",
	SUGGEST_WARP_INFO = "/warpinfo <warpname>",
	SUGGEST_WARP_SETNAME = "/warpsetname <warpname> <newwarpname>",
	SUGGEST_WARP_SETPOSITION = "/warpsetposition <warpname>",
	SUGGEST_WARP_SETOWNER = "/warpsetowner <warpname> <playername>",
	SUGGEST_WARP_SETPERMISSION = "/warpsetpermission <warpname> <permission>",
	SUGGEST_WARP_SETPASSWORD = "/warpsetpassword <warpname> <password>",
	SUGGEST_WARP_SETPRICE = "/warpsetprice <warpname> <price>",
	SUGGEST_WARP_HIDDEN = "/warphidden <warpname>",
	SUGGEST_WARP_ADDMEMBER = "/warpaddmember <warpname> <playername>",
	SUGGEST_WARP_REMOVEMEMBER = "/warpremovemember <warpname> <playername>",
	SUGGEST_WARP_ADDBLACKLIST = "/warpaddblacklist <warpname> <playername>",
	SUGGEST_WARP_REMOVEBLACKLIST = "/warpremoveblacklist <warpname> <playername>",
	
	//Bypass Perm
	PERM_BYPASS_BACK_COST = "btm.bypass.back.cost",
	PERM_BYPASS_HOME = "btm.bypass.home.admin",
	PERM_BYPASS_HOME_TOOMANY = "btm.bypass.home.toomany",
	PERM_BYPASS_HOME_COST = "btm.bypass.home.cost",
	PERM_BYPASS_HOME_FORBIDDEN = "btm.bypass.home.forbidden",
	PERM_BYPASS_TELEPORT_TPATOGGLE = "btm.bypass.tp.tpatoggle",
	PERM_BYPASS_TELEPORT_COST = "btm.bypass.tp.cost",
	PERM_BYPASS_TELEPORT_FORBIDDEN = "btm.bypass.tp.forbidden",
	PERM_BYPASS_TELEPORT_SILENT = "btm.bypass.tp.silent",
	PERM_BYPASS_WARP = "btm.bypass.warp.admin",
	PERM_BYPASS_WARP_BLACKLIST = "btm.bypass.warp.blacklist",
	PERM_BYPASS_WARP_TOOMANY = "btm.bypass.warp.toomany",
	PERM_BYPASS_WARP_COST = "btm.bypass.warp.cost",
	PERM_BYPASS_WARP_FORBIDDEN = "btm.bypass.warp.forbidden",
	PERM_BYPASS_WARP_OTHER = "btm.bypass.warp.other"
	;
}
