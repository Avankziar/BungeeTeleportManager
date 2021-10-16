package main.java.me.avankziar.general.object;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.Sound;

public class Portal
{
	public enum TargetType
	{
		COMMAND, 
		LOCATION,
		BACK, DEATHBACK,
		FIRSTSPAWN, RESPAWN, 
		HOME, PORTAL, RANDOMTELEPORT, SAVEPOINT, WARP
	}
	private String portalName;
	private String ownerUUID;
	private String permission;
	private ArrayList<String> members;
	private ArrayList<String> blacklist;
	private String category;
	private Material triggerBlock;
	
	private double pricePerUse = 0.0;
	private LinkedHashMap<String, Long> cooldownUse = new LinkedHashMap<>(); //RAM Only
	private double throwback = 0.7;
	private int portalProtectionRadius = 0;
	private Sound portalSound = Sound.ENTITY_ENDERMAN_TELEPORT;
	
	private TargetType targetType;
	private String targetInformation;
	private String accessDenialMessage;
	
	private ServerLocation position1;
	private ServerLocation position2;
	private ServerLocation ownExitPosition;
	
	public Portal(String portalName, String permission, 
			String ownerUUID, ArrayList<String> members, ArrayList<String> blacklist,
			String category, Material triggerBlock,
			double pricePerUse, double throwback, int portalProtectionRadius, Sound portalSound,
			TargetType targetType, String targetInformation, String accessDenialMessage,
			ServerLocation position1, ServerLocation position2, ServerLocation ownExitPosition)
	{
		setPortalName(portalName);
		setPermission(permission);
		setOwnerUUID(ownerUUID);
		setMembers(members);
		setBlacklist(blacklist);
		setCategory(category);
		setTriggerBlock(triggerBlock);
		
		setPricePerUse(pricePerUse);
		setThrowback(throwback);
		setPortalProtectionRadius(portalProtectionRadius);
		setPortalSound(portalSound);
		
		setPosition1(position1);
		setPosition2(position2);
		setOwnExitPosition(ownExitPosition);
	}

	public String getPortalName()
	{
		return portalName;
	}

	public void setPortalName(String portalName)
	{
		this.portalName = portalName;
	}

	public String getOwnerUUID()
	{
		return ownerUUID;
	}

	public void setOwnerUUID(String ownerUUID)
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

	public ArrayList<String> getMembers()
	{
		return members;
	}

	public void setMembers(ArrayList<String> members)
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

	public long hasCooldown(String uuid)
	{
		if(this.cooldownUse.containsKey(uuid))
		{
			return this.cooldownUse.get(uuid);
		} else
		{
			return -1;
		}
	}
	
	public void addCooldown(String uuid, long additionCooldown)
	{
		if(this.cooldownUse.containsKey(uuid))
		{
			this.cooldownUse.replace(uuid, System.currentTimeMillis()+additionCooldown);
		} else
		{
			this.cooldownUse.put(uuid, System.currentTimeMillis()+additionCooldown);
		}
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

	public String getAccessDenialMessage()
	{
		return accessDenialMessage;
	}

	public void setAccessDenialMessage(String accessDenialMessage)
	{
		this.accessDenialMessage = accessDenialMessage;
	}

}
