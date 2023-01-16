package main.java.me.avankziar.general.object;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

import main.java.me.avankziar.general.objecthandler.ServerLocationHandler;

public class Portal
{
	public enum TargetType
	{
		CONFIGPREDEFINE,
		COMMAND, 
		LOCATION,
		BACK, DEATHBACK,
		FIRSTSPAWN, RESPAWN, 
		HOME, PORTAL, RANDOMTELEPORT, SAVEPOINT, WARP
	}
	
	public enum AccessType
	{
		CLOSED, OPEN
	}
	
	public enum PostTeleportExecuterCommand
	{
		PLAYER, CONSOLE;
	}
	
	private int id;
	private String portalName;
	private String ownerUUID;
	private String permission;
	private AccessType accessType;
	private ArrayList<String> members;
	private ArrayList<String> blacklist;
	private String category;
	private Material triggerBlock;
	
	private double pricePerUse = 0.0;
	private double throwback = 0.7;
	private int portalProtectionRadius = 0;
	private Sound portalSound = Sound.ENTITY_ENDERMAN_TELEPORT;
	private SoundCategory portalSoundCategory = SoundCategory.AMBIENT;
	
	private long cooldown;
	
	private TargetType targetType;
	private String targetInformation;
	private String postTeleportMessage;
	private String accessDenialMessage;
	private PostTeleportExecuterCommand postTeleportExecuterCommand;
	private String postTeleportExecutingCommand;
	
	private ServerLocation position1;
	private ServerLocation position2;
	private ServerLocation ownExitPosition;
	
	public Portal(int id, String portalName, String permission, 
			String ownerUUID, AccessType accessType, ArrayList<String> members, ArrayList<String> blacklist,
			String category, Material triggerBlock,
			double pricePerUse, double throwback, int portalProtectionRadius, long cooldown, Sound portalSound,
			SoundCategory portalSoundCategory,
			TargetType targetType, String targetInformation, String postTeleportMessage, String accessDenialMessage,
			String postTeleportExecutingCommand, PostTeleportExecuterCommand postTeleportExecuterCommand,
			ServerLocation position1, ServerLocation position2, ServerLocation ownExitPosition)
	{
		setId(id);
		setName(portalName);
		setPermission(permission);
		setOwner(ownerUUID);
		setAccessType(accessType);
		setMember(members);
		setBlacklist(blacklist);
		setCategory(category);
		setTriggerBlock(triggerBlock);
		
		setPricePerUse(pricePerUse);
		setThrowback(throwback);
		setPortalProtectionRadius(portalProtectionRadius);
		setPortalSound(portalSound);
		setPortalSoundCategory(portalSoundCategory);
		
		setCooldown(cooldown);
		
		setTargetType(targetType);
		setTargetInformation(targetInformation);
		setPostTeleportMessage(postTeleportMessage);
		setAccessDenialMessage(accessDenialMessage);
		setPostTeleportExecutingCommand(postTeleportExecutingCommand);
		setPostTeleportExecuterCommand(postTeleportExecuterCommand);
		
		setPosition1(position1);
		setPosition2(position2);
		setOwnExitPosition(ownExitPosition);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return portalName;
	}

	public void setName(String portalName)
	{
		this.portalName = portalName;
	}

	public String getOwner()
	{
		return ownerUUID;
	}

	public void setOwner(String ownerUUID)
	{
		this.ownerUUID = ownerUUID;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	/**
	 * @return the accessType
	 */
	public AccessType getAccessType()
	{
		return accessType;
	}

	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(AccessType accessType)
	{
		this.accessType = accessType;
	}

	public ArrayList<String> getMember()
	{
		return members;
	}

	public void setMember(ArrayList<String> members)
	{
		this.members = members;
	}

	public ArrayList<String> getBlacklist()
	{
		return blacklist;
	}

	public void setBlacklist(ArrayList<String> blacklist)
	{
		this.blacklist = blacklist;
	}

	public double getPricePerUse()
	{
		return pricePerUse;
	}

	public void setPricePerUse(double pricePerUse)
	{
		this.pricePerUse = pricePerUse;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public Material getTriggerBlock()
	{
		return triggerBlock;
	}

	public void setTriggerBlock(Material triggerBlock)
	{
		this.triggerBlock = triggerBlock;
	}

	public ServerLocation getPosition1()
	{
		return position1;
	}

	public void setPosition1(ServerLocation position1)
	{
		this.position1 = position1;
	}

	public ServerLocation getPosition2()
	{
		return position2;
	}

	public void setPosition2(ServerLocation position2)
	{
		this.position2 = position2;
	}

	public ServerLocation getOwnExitPosition()
	{
		return ownExitPosition;
	}

	public void setOwnExitPosition(ServerLocation ownExitPosition)
	{
		this.ownExitPosition = ownExitPosition;
	}

	public double getThrowback()
	{
		return throwback;
	}

	public void setThrowback(double throwback)
	{
		this.throwback = throwback;
	}

	public int getPortalProtectionRadius()
	{
		return portalProtectionRadius;
	}

	public void setPortalProtectionRadius(int portalProtectionRadius)
	{
		this.portalProtectionRadius = portalProtectionRadius;
	}

	public Sound getPortalSound()
	{
		return portalSound;
	}

	public void setPortalSound(Sound portalSound)
	{
		this.portalSound = portalSound;
	}

	public SoundCategory getPortalSoundCategory()
	{
		return portalSoundCategory;
	}

	public void setPortalSoundCategory(SoundCategory portalSoundCategory)
	{
		this.portalSoundCategory = portalSoundCategory;
	}

	public long getCooldown()
	{
		return cooldown;
	}

	public void setCooldown(long cooldown)
	{
		this.cooldown = cooldown;
	}

	public TargetType getTargetType()
	{
		return targetType;
	}

	public void setTargetType(TargetType targetType)
	{
		this.targetType = targetType;
	}

	public String getTargetInformation()
	{
		return targetInformation;
	}

	public void setTargetInformation(String targetInformation)
	{
		this.targetInformation = targetInformation;
	}
	/**
	 * Known replacer possible:<br>
	 * %portalname% - the portalname<br>
	 * %player% - the executed player
	 * @return
	 */
	public String getAccessDenialMessage()
	{
		return accessDenialMessage;
	}

	public void setAccessDenialMessage(String accessDenialMessage)
	{
		this.accessDenialMessage = accessDenialMessage;
	}

	public String getPostTeleportMessage()
	{
		return postTeleportMessage;
	}

	public void setPostTeleportMessage(String postTeleportMessage)
	{
		this.postTeleportMessage = postTeleportMessage;
	}
	
	public String getPostTeleportExecutingCommand()
	{
		return postTeleportExecutingCommand;
	}

	public void setPostTeleportExecutingCommand(String postTeleportExecutingCommand)
	{
		this.postTeleportExecutingCommand = postTeleportExecutingCommand;
	}

	public PostTeleportExecuterCommand getPostTeleportExecuterCommand()
	{
		return postTeleportExecuterCommand;
	}

	public void setPostTeleportExecuterCommand(PostTeleportExecuterCommand postTeleportExecuterCommand)
	{
		this.postTeleportExecuterCommand = postTeleportExecuterCommand;
	}

	@Override
	public String toString()
	{
		String s = 
				this.portalName+", "+
				(this.ownerUUID != null ? this.ownerUUID : "/")+", "+
				this.category+", "+
				this.triggerBlock.toString()+", "+
				this.pricePerUse+", "+
				this.throwback+", "+
				this.portalProtectionRadius+", "+
				this.portalSound.toString()+", "+
				this.portalSoundCategory.toString()+", "+
				this.targetType+", "+
				this.targetInformation+", "+
				this.postTeleportMessage+", "+
				this.accessDenialMessage+", "+
				this.postTeleportExecutingCommand+", "+
				this.postTeleportExecuterCommand.toString()+", "+
				ServerLocationHandler.serialised(this.position1)+", "+
				ServerLocationHandler.serialised(this.position2)+", "+
				ServerLocationHandler.serialised(this.ownExitPosition);
		if(!this.members.isEmpty())
		{
			for(String m : this.members)
			{
				s += ", "+m;
			}
		}
		if(!this.blacklist.isEmpty())
		{
			for(String b : this.blacklist)
			{
				s += ", "+b;
			}
		}
		return s;
	}
}