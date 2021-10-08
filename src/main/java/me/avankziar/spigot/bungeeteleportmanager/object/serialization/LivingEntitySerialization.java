package main.java.me.avankziar.spigot.bungeeteleportmanager.object.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Cat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mob;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Steerable;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.EntityTransportHelper;

public class LivingEntitySerialization
{
	//Bekannt fehlende dinge:
	/*
	 * Attributable
	 * 
	 */
	private enum TAG
	{
		ABSORPTION("DOUBLE"), AI("BOOLEAN"), ARROWCOOLDOWN("INT"), ARROWINBODY("INT"), 
		CANPICKUPITEMS("BOOLEAN"), COLLIDABLE("BOOLEAN"), CUSTOMNAME("STRING"), CUSTOMNAMEVISIABLE("BOOLEAN"),
		ENTITYTYPE("ENTITYTYPE"),
		FIRETICKS("INT"), FREEZETICKS("INT"),
		GLOWING("BOOLEAN"), GRAVITY("BOOLEAN"),
		ATTRIBUTE_MAXHEALTH("DOUBLE"), //Change to ATTRIBUTE
		HEALTH("DOUBLE"),
		INVISIBLE("BOOLEAN"), INVULNERABLE("BOOLEAN"),
		MAXIMUMAIR("INT"),
		PERSISTENT("BOOLEAN"),
		PERSISTENTDATACONTAINER("STRING"),
		REMAININGAIR("INT"), REMOVEWHENFARAWAY("BOOLEAN"),
		SILENT("BOOLEAN"),
		TICKSLIVED("INT"),
		VISUALFIRE("BOOLEAN"),
		AGEABLE_ADULT("BOOLEAN"), AGEABLE_AGE("INT"),
		AH_DOMESTICATION("INT"), AH_INVENTORY("ITEMSTACKARRAY"), AH_JUMPSTRENGTH("DOUBLE"), AH_MAXDOMESTICATION("INT"), AH_SADDLE("ITEMSTACK"),
		AV_INVENTORY("ITEMSTACKARRAY"),
		AXOLOTL_VARIANT("AXOLOTLVARIANT"),
		BREEDABLE_AGELOCK("BOOLEAN"), BREEDABLE_BREED("BOOLEAN"),
		CAT_TYPE("CATTYPE"), CAT_COLLARCOLOR("DYECOLOR"),
		CHESTEDHORSE_CARRYINGCHEST("BOOLEAN"),
		FOX_FIRSTTRUSTEDPLAYER("ANIMALTAMER"), FOX_TYPE("FOXTYPE"), FOX_SECONDTRUSTEDPLAYER("ANIMALTAMER"),
		HOGLIN_CONVERSIONTIME("INT"), HOGLIN_IMMUNETOZOMBIFICATION("BOOLEAN"), HOGLIN_ISABLETOBEHUNTED("BOOLEAN"),
		HORSE_COLOR("HORSECOLOR"), HORSE_INVENTORY("ITEMSTACK"), HORSE_STYLE("HORSESTYLE"),
		LLAMA_COLOR("LLAMACOLOR"), LLAMA_DECOR("ITEMSTACK"), LLAMA_STRENGHT("INT"),
		MERCHANT("MERCHANT"),
		MOB_AWARE("BOOLEAN"),
		MUSHROOMCOW_VARIANT("MUSHROOMCOWVARIANT"),
		OCELOT_TRUSTING("BOOLEAN"),
		PANDA_MAINGENE("PANDAGENE"), PANDA_HIDDENGENE("PANDAGENE"),
		PARROT_VARIANT("PARROTVARIANT"),
		PHANTOM_SIZE("INT"),
		PIGLINABSTRACT_CONVERSIONTIME("INT"), PIGLINABSTRACT_IMMUNETOZOMBIFICATION("BOOLEAN"),
		PIGZOMBIE_ANGER("INT"), PIGZOMBIE_ANGRY("BOOLEAN"),
		PUFFERFISH_STATE("INT"),
		RABBIT_TYPE("RABBITTYPE"),
		RAIDER_CANJOINRAID("BOOLEAN"), RAIDER_PATROLLEADER("BOOLEAN"),
		SHEEP_SHEARED("BOOLEAN"),
		SLIME_SIZE("INT"),
		SNOWMAN_DERP("BOOLEAN"),
		STEERABLE_BOOSTTICKS("INT"), STEERABLE_CURRENTBOOSTTICKS("INT"), STEERABLE_SADDLE("BOOLEAN"),
		TAMEABLE_OWNER("ANIMALTAMER"), TAMEABLE_TAMED("BOOLEAN"),
		TROPICALFISH_BODYCOLOR("DYECOLOR"), TROPICALFISH_PATTERN("TROPICALFISHPATTERN"), TROPICALFISH_PATTERNCOLOR("DYECOLOR"),
		VILLAGER_PROFESSION("VILLAGERPROFESSION"), VILLAGER_EXPERIENCE("INT"), VILLAGER_TYPE("VILLAGERTYPE"),
		WOLF_ANGRY("BOOLEAN"), WOLF_COLLARCOLOR("DYECOLOR"),
		ZOMBIEVILLAGER_PROFESSION("VILLAGERPROFESSION"), ZOMBIEVILLAGER_TYPE("VILLAGERTYPE");
		
		private TAG(String s)
		{
			this.objectDescription = s;
		}
		
		protected String objectDescription;
		
		public String getObjectDesc()
		{
			return this.objectDescription;
		}
	}
	
	public static boolean canBeSerialized(Entity entity)
	{
		if(entity instanceof LivingEntity)
		{
			if(entity.getType() == EntityType.PLAYER)
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static String serializeEntityAsString(LivingEntity ley) 
	{
		// Wenn ?, dann klären ob sich das lohnt
		LinkedHashMap<TAG, Object> tagmap = new LinkedHashMap<>();
		tagmap.put(TAG.ABSORPTION, ley.getAbsorptionAmount());
		tagmap.put(TAG.AI, ley.hasAI());
		tagmap.put(TAG.ARROWCOOLDOWN, ley.getArrowCooldown());
		tagmap.put(TAG.ARROWINBODY, ley.getArrowsInBody()); //?
		tagmap.put(TAG.CANPICKUPITEMS, ley.getCanPickupItems());
		tagmap.put(TAG.COLLIDABLE, ley.isCollidable());
		if(ley.getCustomName() != null)
		{
			tagmap.put(TAG.CUSTOMNAME, ley.getCustomName());
			tagmap.put(TAG.CUSTOMNAMEVISIABLE, ley.isCustomNameVisible());
		}
		tagmap.put(TAG.ENTITYTYPE, ley.getType());
		tagmap.put(TAG.FIRETICKS, ley.getFireTicks());
		tagmap.put(TAG.FREEZETICKS, ley.getFreezeTicks());
		tagmap.put(TAG.GLOWING, ley.isGlowing());
		tagmap.put(TAG.GRAVITY, ley.hasGravity());
		/*if(ley instanceof Attributable)
		{
			Attributable att = (Attributable) ley;
			List<Attribute> attributeList = new ArrayList<Attribute>(EnumSet.allOf(Attribute.class));
			for(Attribute a : attributeList)
			{
				AttributeInstance ati = att.getAttribute(Attribute.GENERIC_ARMOR);
				if(ati == null)
				{
					continue;
				}
				String s = "";
				s += ati.getBaseValue();
				int i = 0;
				int end = ati.getModifiers().size();
				if(end > 0)
				{
					s += "°";
				}
				for(AttributeModifier am : ati.getModifiers())
				{
					
				}
				switch(a)
				{
				case GENERIC_ARMOR:
					//tagmap.put(TAG.GENERIC_ARMOR, );
					break;
					//...
				}
			}			
		}*/
		tagmap.put(TAG.ATTRIBUTE_MAXHEALTH, ley.getMaxHealth());
		tagmap.put(TAG.HEALTH, ley.getHealth());
		tagmap.put(TAG.INVISIBLE, ley.isInvisible());
		tagmap.put(TAG.INVULNERABLE, ley.isInvulnerable());
		tagmap.put(TAG.MAXIMUMAIR, ley.getMaximumAir());
		tagmap.put(TAG.PERSISTENT, ley.isPersistent());
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		if(!pdc.isEmpty())
		{
			String pdcs = "";
			NamespacedKey nowner = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTransportHelper.OWNER);
			//NamespacedKey nmembers = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTransportHelper.MEMBERS);
			if(pdc.has(nowner, PersistentDataType.STRING))
			{
				pdcs += pdc.get(nowner, PersistentDataType.STRING);
			}
			/* not sure if member is to micromanagement
			if(pdc.has(nmembers, PersistentDataType.STRING))
			{
				pdcs += "!"+pdc.get(nmembers, PersistentDataType.STRING);
			}*/
			tagmap.put(TAG.PERSISTENTDATACONTAINER, pdcs);
		}
		tagmap.put(TAG.REMAININGAIR, ley.getRemainingAir());
		tagmap.put(TAG.REMOVEWHENFARAWAY, ley.getRemoveWhenFarAway()); //?
		tagmap.put(TAG.SILENT, ley.isSilent());
		tagmap.put(TAG.TICKSLIVED, ley.getTicksLived());
		tagmap.put(TAG.VISUALFIRE, ley.isVisualFire());
		//tagmap.put(TAG., ley.);
		if(ley instanceof Ageable)
		{
			Ageable age = (Ageable) ley;
			tagmap.put(TAG.AGEABLE_ADULT, age.isAdult());
			tagmap.put(TAG.AGEABLE_AGE, age.getAge());
		}
		if(ley instanceof Axolotl)
		{
			Axolotl ax = (Axolotl) ley;
			tagmap.put(TAG.AXOLOTL_VARIANT, ax.getVariant().toString());
		}
		if(ley instanceof Breedable)
		{
			Breedable breed = (Breedable) ley;
			tagmap.put(TAG.BREEDABLE_AGELOCK, breed.getAgeLock());
			tagmap.put(TAG.BREEDABLE_BREED, breed.canBreed());
		}
		if(ley instanceof Cat)
		{
			Cat cat = (Cat) ley;
			tagmap.put(TAG.CAT_COLLARCOLOR, cat.getCollarColor().getColor().asRGB());
			tagmap.put(TAG.CAT_TYPE, cat.getCatType().toString());
		}
		if(ley instanceof ChestedHorse)
		{
			ChestedHorse ch = (ChestedHorse) ley;
			tagmap.put(TAG.CHESTEDHORSE_CARRYINGCHEST, ch.isCarryingChest());
		}
		if(ley instanceof Fox)
		{
			Fox fox = (Fox) ley;
			if(fox.getFirstTrustedPlayer() != null)
			{
				tagmap.put(TAG.FOX_FIRSTTRUSTEDPLAYER, fox.getFirstTrustedPlayer().getUniqueId().toString()
						+"!"+fox.getFirstTrustedPlayer().getName());
			}
			tagmap.put(TAG.FOX_TYPE, fox.getFoxType());
			if(fox.getSecondTrustedPlayer() != null)
			{
				tagmap.put(TAG.FOX_SECONDTRUSTEDPLAYER, fox.getSecondTrustedPlayer().getUniqueId().toString()
						+"!"+fox.getSecondTrustedPlayer().getName());
			}
		}
		if(ley instanceof Hoglin)
		{
			Hoglin hog = (Hoglin) ley;
			tagmap.put(TAG.HOGLIN_CONVERSIONTIME, hog.getConversionTime());
			tagmap.put(TAG.HOGLIN_IMMUNETOZOMBIFICATION, hog.isImmuneToZombification());
			tagmap.put(TAG.HOGLIN_ISABLETOBEHUNTED, hog.isAbleToBeHunted());
		}
		if(ley instanceof Horse)
		{
			Horse ho = (Horse) ley;
			tagmap.put(TAG.HORSE_COLOR, ho.getColor().toString());
			tagmap.put(TAG.HORSE_INVENTORY, toBase64itemStack(ho.getInventory().getArmor()));
			tagmap.put(TAG.HORSE_STYLE, ho.getStyle().toString());
		}
		if(ley instanceof Llama)
		{
			Llama ll = (Llama) ley;
			tagmap.put(TAG.LLAMA_COLOR, ll.getColor().toString());
			tagmap.put(TAG.LLAMA_STRENGHT, ll.getStrength());
		}
		if(ley instanceof Merchant)
		{
			Merchant mer = (Merchant) ley;
			tagmap.put(TAG.MERCHANT, recipeSerialize(mer.getRecipes()));
		}
		if(ley instanceof Mob)
		{
			Mob mob = (Mob) ley;
			tagmap.put(TAG.MOB_AWARE, mob.isAware());
		}
		if(ley instanceof MushroomCow)
		{
			MushroomCow mc = (MushroomCow) ley;
			tagmap.put(TAG.MUSHROOMCOW_VARIANT, mc.getVariant().toString());
		}
		if(ley instanceof Ocelot)
		{
			Ocelot oc = (Ocelot) ley;
			tagmap.put(TAG.OCELOT_TRUSTING, oc.isTrusting());
		}
		if(ley instanceof Panda)
		{
			Panda pa = (Panda) ley;
			tagmap.put(TAG.PANDA_HIDDENGENE, pa.getHiddenGene().toString());
			tagmap.put(TAG.PANDA_MAINGENE, pa.getMainGene().toString());
		}
		if(ley instanceof Parrot)
		{
			Parrot pa = (Parrot) ley;
			tagmap.put(TAG.PARROT_VARIANT, pa.getVariant().toString());
		}
		if(ley instanceof Phantom)
		{
			Phantom ph = (Phantom) ley;
			tagmap.put(TAG.PHANTOM_SIZE, ph.getSize());
		}
		if(ley instanceof PiglinAbstract)
		{
			PiglinAbstract pa = (PiglinAbstract) ley;
			tagmap.put(TAG.PIGLINABSTRACT_CONVERSIONTIME, pa.getConversionTime());
			tagmap.put(TAG.PIGLINABSTRACT_IMMUNETOZOMBIFICATION, pa.isImmuneToZombification());
		}
		if(ley instanceof PigZombie)
		{
			PigZombie pz = (PigZombie) ley;
			tagmap.put(TAG.PIGZOMBIE_ANGER, pz.getAnger());
			tagmap.put(TAG.PIGZOMBIE_ANGRY, pz.isAngry());
		}
		if(ley instanceof PufferFish)
		{
			PufferFish pf = (PufferFish) ley;
			tagmap.put(TAG.PUFFERFISH_STATE, pf.getPuffState());
		}
		if(ley instanceof Rabbit)
		{
			Rabbit ra = (Rabbit) ley;
			tagmap.put(TAG.RABBIT_TYPE, ra.getType().toString());
		}
		if(ley instanceof Raider)
		{
			Raider ra = (Raider) ley;
			tagmap.put(TAG.RAIDER_CANJOINRAID, ra.isCanJoinRaid());
			tagmap.put(TAG.RAIDER_PATROLLEADER, ra.isPatrolLeader());
		}
		if(ley instanceof Sheep)
		{
			Sheep sh = (Sheep) ley;
			tagmap.put(TAG.SHEEP_SHEARED, sh.isSheared());
		}
		if(ley instanceof Slime)
		{
			Slime sl = (Slime) ley;
			tagmap.put(TAG.SLIME_SIZE, sl.getSize());
		}
		if(ley instanceof Snowman)
		{
			Snowman sm = (Snowman) ley;
			tagmap.put(TAG.SNOWMAN_DERP, sm.isDerp());
		}
		if(ley instanceof Steerable)
		{
			Steerable st = (Steerable) ley;
			tagmap.put(TAG.STEERABLE_BOOSTTICKS, st.getBoostTicks());
			tagmap.put(TAG.STEERABLE_CURRENTBOOSTTICKS, st.getCurrentBoostTicks());
			tagmap.put(TAG.STEERABLE_SADDLE, st.hasSaddle());
		}
		if(ley instanceof Tameable)
		{
			Tameable ta = (Tameable) ley;
			if(ta.getOwner() != null)
			{
				tagmap.put(TAG.TAMEABLE_OWNER, ta.getOwner().getUniqueId().toString()+"!"+ta.getOwner().getName());
			}
			tagmap.put(TAG.TAMEABLE_TAMED, ta.isTamed());
		}
		if(ley instanceof TropicalFish)
		{
			TropicalFish tf = (TropicalFish) ley;
			tagmap.put(TAG.TROPICALFISH_BODYCOLOR, tf.getBodyColor().getColor().asRGB());
			tagmap.put(TAG.TROPICALFISH_PATTERN, tf.getPattern().toString());
			tagmap.put(TAG.TROPICALFISH_PATTERNCOLOR, tf.getPatternColor().getColor().asRGB());
		}
		if(ley instanceof Villager)
		{
			Villager vi = (Villager) ley;
			tagmap.put(TAG.VILLAGER_PROFESSION, vi.getProfession().toString());
			tagmap.put(TAG.VILLAGER_EXPERIENCE, vi.getVillagerExperience());
			tagmap.put(TAG.VILLAGER_TYPE, vi.getVillagerType().toString());
		}
		if(ley instanceof Wolf)
		{
			Wolf wo = (Wolf) ley;
			tagmap.put(TAG.WOLF_ANGRY, wo.isAngry());
			tagmap.put(TAG.WOLF_COLLARCOLOR, wo.getCollarColor().getColor().asRGB());
		}
		if(ley instanceof ZombieVillager)
		{
			ZombieVillager zv = (ZombieVillager) ley;
			if(zv.getVillagerProfession() != null)
			{
				tagmap.put(TAG.ZOMBIEVILLAGER_PROFESSION, zv.getVillagerProfession().toString());
			}
			tagmap.put(TAG.ZOMBIEVILLAGER_TYPE, zv.getVillagerType().toString());
		}
		//Is down here, because other things must become before
		if(ley instanceof AbstractHorse)
		{
			AbstractHorse ah = (AbstractHorse) ley;
			tagmap.put(TAG.AH_DOMESTICATION, ah.getDomestication());
			tagmap.put(TAG.AH_INVENTORY, toBase64itemStackArray(ah.getInventory().getContents()));
			tagmap.put(TAG.AH_JUMPSTRENGTH, ah.getJumpStrength());
			tagmap.put(TAG.AH_MAXDOMESTICATION, ah.getMaxDomestication());
			if(ah.getInventory().getSaddle() != null)
			{
				tagmap.put(TAG.AH_SADDLE, toBase64itemStack(ah.getInventory().getSaddle()));
			}
		}
		if(ley instanceof AbstractVillager)
		{
			AbstractVillager av = (AbstractVillager) ley;
			tagmap.put(TAG.AV_INVENTORY, toBase64itemStackArray(av.getInventory().getContents()));
		}
		int i = 0;
		int end = tagmap.size();
		String re = "";
		for(Entry<TAG, Object> tag : tagmap.entrySet())
		{
			re += tag.getKey().toString()+":"+tag.getValue();
			if(i+1 < end)
			{
				re += ",";
			}
		}
		return re;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void spawnEntity(Location location, String serializeEntity)
	{
		LinkedHashMap<TAG, Object> tagmap = getEntityMap(serializeEntity);
		LivingEntity ley = (LivingEntity) location.getWorld().spawnEntity(location, 
				EntityType.valueOf(String.valueOf(tagmap.get(TAG.ENTITYTYPE))));
		for(Entry<TAG, Object> tag : tagmap.entrySet())
		{
			Object tagVO = tag.getValue();
			BungeeTeleportManager.log.info("Tag:"+tag.getKey().toString());
		    BungeeTeleportManager.log.info("Value:"+(tagVO.toString() != null ? tagVO.toString() : "null"));
			switch(tag.getKey())
			{
			case ABSORPTION:
				ley.setAbsorptionAmount((Double) tagVO);
				break;
			case AI:
				ley.setAI((Boolean) tagVO);
				break;
			case ARROWCOOLDOWN:
				ley.setArrowCooldown((Integer) tagVO);
				break;
			case ARROWINBODY:
				ley.setArrowsInBody((Integer) tagVO);
				break;
			case CANPICKUPITEMS:
				ley.setCanPickupItems((Boolean) tagVO);
				break;
			case COLLIDABLE:
				ley.setCollidable((Boolean) tagVO);
				break;
			case CUSTOMNAME:
				ley.setCustomName((String) tagVO);
				break;
			case CUSTOMNAMEVISIABLE:
				ley.setCustomNameVisible((Boolean) tagVO);
				break;
			case ENTITYTYPE:
				break;
			case FIRETICKS:
				ley.setFireTicks((Integer) tagVO);
				break;
			case FREEZETICKS:
				ley.setFreezeTicks((Integer) tagVO);
				break;
			case GLOWING:
				ley.setGlowing((Boolean) tagVO);
				break;
			case GRAVITY:
				ley.setGravity((Boolean) tagVO);
				break;
			case ATTRIBUTE_MAXHEALTH:
				ley.setMaxHealth((Double) tagVO);
				break;
			case HEALTH:
				ley.setHealth((Double) tagVO);
				break;
			case INVISIBLE:
				ley.setInvisible((Boolean) tagVO);
				break;
			case INVULNERABLE:
				ley.setInvulnerable((Boolean) tagVO);
				break;
			case MAXIMUMAIR:
				ley.setMaximumAir((Integer) tagVO);
				break;
			case PERSISTENT:
				ley.setPersistent((Boolean) tagVO);
				break;
			case PERSISTENTDATACONTAINER:
				PersistentDataContainer pdc = ley.getPersistentDataContainer();
				pdc.set(new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTransportHelper.OWNER),
						PersistentDataType.STRING, (String) tagVO);
				break;
			case REMAININGAIR:
				ley.setRemainingAir((Integer) tagVO);
				break;
			case REMOVEWHENFARAWAY:
				ley.setRemoveWhenFarAway((Boolean) tagVO);
				break;
			case SILENT:
				ley.setSilent((Boolean) tagVO);
				break;
			case TICKSLIVED:
				ley.setTicksLived((Integer) tagVO);
				break;
			case VISUALFIRE:
				ley.setVisualFire((Boolean) tagVO);
				break;
			case AGEABLE_ADULT:
				if(ley instanceof Ageable)
				{
					Ageable age = (Ageable) ley;
					Boolean boo = (Boolean) tagVO;
					if(boo)
					{
						age.setAdult();
					} else
					{
						age.setBaby();
					}
				}
				break;
			case AGEABLE_AGE:
				if(ley instanceof Ageable)
				{
					Ageable age = (Ageable) ley;
					age.setAge((Integer) tagVO);
				}
				break;
			case AH_DOMESTICATION:
				if(ley instanceof AbstractHorse)
				{
					AbstractHorse ah = (AbstractHorse) ley;
					ah.setDomestication((Integer) tagVO);
				}
				break;
			case AH_INVENTORY:
				if(ley instanceof AbstractHorse)
				{
					AbstractHorse ah = (AbstractHorse) ley;
					ah.getInventory().setContents((ItemStack[]) tagVO);
				}
				break;
			case AH_JUMPSTRENGTH:
				if(ley instanceof AbstractHorse)
				{
					AbstractHorse ah = (AbstractHorse) ley;
					ah.setJumpStrength((Double) tagVO);
				}
				break;
			case AH_MAXDOMESTICATION:
				if(ley instanceof AbstractHorse)
				{
					AbstractHorse ah = (AbstractHorse) ley;
					ah.setMaxDomestication((Integer) tagVO);
				}
				break;
			case AH_SADDLE:
				if(ley instanceof AbstractHorse)
				{
					AbstractHorse ah = (AbstractHorse) ley;
					ah.getInventory().setSaddle((ItemStack) tagVO);
				}
				break;
			case AV_INVENTORY:
				if(ley instanceof AbstractVillager)
				{
					AbstractVillager av = (AbstractVillager) ley;
					av.getInventory().setContents((ItemStack[]) tagVO);
				}
				break;
			case AXOLOTL_VARIANT:
				if(ley instanceof Axolotl)
				{
					Axolotl ax = (Axolotl) ley;
					ax.setVariant((Axolotl.Variant) tagVO);
				}
				break;
			case BREEDABLE_AGELOCK:
				if(ley instanceof Breedable)
				{
					Breedable br = (Breedable) ley;
					br.setAgeLock((Boolean) tagVO);
				}
				break;
			case BREEDABLE_BREED:
				if(ley instanceof Breedable)
				{
					Breedable br = (Breedable) ley;
					br.setBreed((Boolean) tagVO);
				}
				break;
			case CAT_COLLARCOLOR:
				if(ley instanceof Cat)
				{
					Cat cat = (Cat) ley;
					cat.setCollarColor((DyeColor) tagVO);
				}
				break;
			case CAT_TYPE:
				if(ley instanceof Cat)
				{
					Cat cat = (Cat) ley;
					cat.setCatType((Cat.Type) tagVO);
				}
				break;
			case CHESTEDHORSE_CARRYINGCHEST:
				if(ley instanceof ChestedHorse)
				{
					ChestedHorse ch = (ChestedHorse) ley;
					ch.setCarryingChest((Boolean) tagVO);
				}
				break;
			case FOX_FIRSTTRUSTEDPLAYER:
				if(ley instanceof Fox)
				{
					Fox fox = (Fox) ley;
					fox.setFirstTrustedPlayer((AnimalTamer) tagVO);
				}
				break;
			case FOX_SECONDTRUSTEDPLAYER:
				if(ley instanceof Fox)
				{
					Fox fox = (Fox) ley;
					fox.setSecondTrustedPlayer((AnimalTamer) tagVO);
				}
				break;
			case FOX_TYPE:
				if(ley instanceof Fox)
				{
					Fox fox = (Fox) ley;
					fox.setFoxType((Fox.Type) tagVO);
				}
				break;
			case HOGLIN_CONVERSIONTIME:
				if(ley instanceof Hoglin)
				{
					Hoglin hog = (Hoglin) ley;
					hog.setConversionTime((Integer) tagVO);
				}
				break;
			case HOGLIN_IMMUNETOZOMBIFICATION:
				if(ley instanceof Hoglin)
				{
					Hoglin hog = (Hoglin) ley;
					hog.setImmuneToZombification((Boolean) tagVO);
				}
				break;
			case HOGLIN_ISABLETOBEHUNTED:
				if(ley instanceof Hoglin)
				{
					Hoglin hog = (Hoglin) ley;
					hog.setIsAbleToBeHunted((Boolean) tagVO);
				}
				break;
			case HORSE_COLOR:
				if(ley instanceof Horse)
				{
					Horse ho = (Horse) ley;
					ho.setColor((Horse.Color) tagVO);
				}
				break;
			case HORSE_INVENTORY:
				if(ley instanceof Horse)
				{
					Horse ho = (Horse) ley;
					ho.getInventory().setArmor((ItemStack) tagVO);
				}
				break;
			case HORSE_STYLE:
				if(ley instanceof Horse)
				{
					Horse ho = (Horse) ley;
					ho.setStyle((Horse.Style) tagVO);
				}
				break;
			case LLAMA_COLOR:
				if(ley instanceof Llama)
				{
					Llama ll = (Llama) ley;
					ll.setColor((Llama.Color) tagVO);
				}
				break;
			case LLAMA_DECOR:
				if(ley instanceof Llama)
				{
					Llama ll = (Llama) ley;
					ll.getInventory().setDecor((ItemStack) tagVO);
				}
				break;
			case LLAMA_STRENGHT:
				if(ley instanceof Llama)
				{
					Llama ll = (Llama) ley;
					ll.setStrength((Integer) tagVO);
				}
				break;
			case MERCHANT:
				if(ley instanceof Merchant)
				{
					Merchant mer = (Merchant) ley;
					mer.setRecipes((List<MerchantRecipe>) tagVO);
				}
				break;
			case MOB_AWARE:
				if(ley instanceof Mob)
				{
					Mob mob = (Mob) ley;
					mob.setAware((Boolean) tagVO);
				}
				break;
			case MUSHROOMCOW_VARIANT:
				if(ley instanceof MushroomCow)
				{
					MushroomCow mu = (MushroomCow) ley;
					mu.setVariant((MushroomCow.Variant) tagVO);
				}
				break;
			case OCELOT_TRUSTING:
				if(ley instanceof Ocelot)
				{
					Ocelot oc = (Ocelot) ley;
					oc.setTrusting((Boolean) tagVO);
				}
				break;
			case PANDA_HIDDENGENE:
				if(ley instanceof Panda)
				{
					Panda pa = (Panda) ley;
					pa.setHiddenGene((Panda.Gene) tagVO);
				}
				break;
			case PANDA_MAINGENE:
				if(ley instanceof Panda)
				{
					Panda pa = (Panda) ley;
					pa.setMainGene((Panda.Gene) tagVO);
				}
				break;
			case PARROT_VARIANT:
				if(ley instanceof Parrot)
				{
					Parrot pa = (Parrot) ley;
					pa.setVariant((Parrot.Variant) tagVO);
				}
				break;
			case PHANTOM_SIZE:
				if(ley instanceof Phantom)
				{
					Phantom ph = (Phantom) ley;
					ph.setSize((Integer) tagVO);
				}
				break;
			case PIGLINABSTRACT_CONVERSIONTIME:
				if(ley instanceof PiglinAbstract)
				{
					PiglinAbstract pa = (PiglinAbstract) ley;
					pa.setConversionTime((Integer) tagVO);
				}
				break;
			case PIGLINABSTRACT_IMMUNETOZOMBIFICATION:
				if(ley instanceof PiglinAbstract)
				{
					PiglinAbstract pa = (PiglinAbstract) ley;
					pa.setImmuneToZombification((Boolean) tagVO);
				}
				break;
			case PIGZOMBIE_ANGER:
				if(ley instanceof PigZombie)
				{
					PigZombie pz = (PigZombie) ley;
					pz.setAnger((Integer) tagVO);
				}
				break;
			case PIGZOMBIE_ANGRY:
				if(ley instanceof PigZombie)
				{
					PigZombie pz = (PigZombie) ley;
					pz.setAngry((Boolean) tagVO);
				}
				break;
			case PUFFERFISH_STATE:
				if(ley instanceof PufferFish)
				{
					PufferFish pf = (PufferFish) ley;
					pf.setPuffState((Integer) tagVO);
				}
				break;
			case RABBIT_TYPE:
				if(ley instanceof Rabbit)
				{
					Rabbit ra = (Rabbit) ley;
					ra.setRabbitType((Rabbit.Type) tagVO);
				}
				break;
			case RAIDER_CANJOINRAID:
				if(ley instanceof Raider)
				{
					Raider ra = (Raider) ley;
					ra.setCanJoinRaid((Boolean) tagVO);
				}
				break;
			case RAIDER_PATROLLEADER:
				if(ley instanceof Raider)
				{
					Raider ra = (Raider) ley;
					ra.setPatrolLeader((Boolean) tagVO);
				}
				break;
			case SHEEP_SHEARED:
				if(ley instanceof Sheep)
				{
					Sheep sh = (Sheep) ley;
					sh.setSheared((Boolean) tagVO);
				}
				break;
			case SLIME_SIZE:
				if(ley instanceof Slime)
				{
					Slime sl = (Slime) ley;
					sl.setSize((Integer) tagVO);
				}
				break;
			case SNOWMAN_DERP:
				if(ley instanceof Snowman)
				{
					Snowman sm = (Snowman) ley;
					sm.setDerp((Boolean) tagVO);
				}
				break;
			case STEERABLE_BOOSTTICKS:
				if(ley instanceof Steerable)
				{
					Steerable st = (Steerable) ley;
					st.setBoostTicks((Integer) tagVO);
				}
				break;
			case STEERABLE_CURRENTBOOSTTICKS:
				if(ley instanceof Steerable)
				{
					Steerable st = (Steerable) ley;
					st.setCurrentBoostTicks((Integer) tagVO);
				}
				break;
			case STEERABLE_SADDLE:
				if(ley instanceof Steerable)
				{
					Steerable st = (Steerable) ley;
					st.setSaddle((Boolean) tagVO);
				}
				break;
			case TAMEABLE_OWNER:
				if(ley instanceof Tameable)
				{
					Tameable ta = (Tameable) ley;
					ta.setOwner((AnimalTamer) tagVO);
				}
				break;
			case TAMEABLE_TAMED:
				if(ley instanceof Tameable)
				{
					Tameable ta = (Tameable) ley;
					ta.setTamed((Boolean) tagVO);
				}
				break;
			case TROPICALFISH_BODYCOLOR:
				if(ley instanceof TropicalFish)
				{
					TropicalFish tf = (TropicalFish) ley;
					tf.setBodyColor((DyeColor) tagVO);
				}
				break;
			case TROPICALFISH_PATTERN:
				if(ley instanceof TropicalFish)
				{
					TropicalFish tf = (TropicalFish) ley;
					tf.setPattern((TropicalFish.Pattern) tagVO);
				}
				break;
			case TROPICALFISH_PATTERNCOLOR:
				if(ley instanceof TropicalFish)
				{
					TropicalFish tf = (TropicalFish) ley;
					tf.setPatternColor((DyeColor) tagVO);
				}
				break;
			case VILLAGER_EXPERIENCE:
				if(ley instanceof Villager)
				{
					Villager vi = (Villager) ley;
					vi.setVillagerExperience((Integer) tagVO);
				}
				break;
			case VILLAGER_PROFESSION:
				if(ley instanceof Villager)
				{
					Villager vi = (Villager) ley;
					vi.setProfession((Villager.Profession) tagVO);
				}
				break;
			case VILLAGER_TYPE:
				if(ley instanceof Villager)
				{
					Villager vi = (Villager) ley;
					vi.setVillagerType((Villager.Type) tagVO);
				}
				break;
			case WOLF_ANGRY:
				if(ley instanceof Wolf)
				{
					Wolf wo = (Wolf) ley;
					wo.setAngry((Boolean) tagVO);
				}
				break;
			case WOLF_COLLARCOLOR:
				if(ley instanceof Wolf)
				{
					Wolf wo = (Wolf) ley;
					wo.setCollarColor((DyeColor) tagVO);
				}
				break;
			case ZOMBIEVILLAGER_PROFESSION:
				if(ley instanceof ZombieVillager)
				{
					ZombieVillager zv = (ZombieVillager) ley;
					zv.setVillagerProfession((Villager.Profession) tagVO);
				}
				break;
			case ZOMBIEVILLAGER_TYPE:
				if(ley instanceof ZombieVillager)
				{
					ZombieVillager zv = (ZombieVillager) ley;
					zv.setVillagerType((Villager.Type) tagVO);
				}
				break;
			}
		}
	}
	
	private static LinkedHashMap<TAG, Object> getEntityMap(String data)
	{
		LinkedHashMap<TAG, Object> map = new LinkedHashMap<>();
		String[] pairs = data.split(",");
		for (int i = 0; i < pairs.length; i++) 
		{
		    String pair = pairs[i];
		    String[] keyValue = pair.split(":");
		    TAG tag = TAG.valueOf(keyValue[0]);
		    switch(tag.getObjectDesc())
		    {
		    case "STRING":
		    	map.put(tag, keyValue[1]);
		    	break;
		    case "BOOLEAN":
		    	map.put(tag, Boolean.valueOf(keyValue[1]));
		    	break;
		    case "INT":
		    	map.put(tag, Integer.valueOf(keyValue[1]));
		    	break;
		    case "DOUBLE":
		    	map.put(tag, Double.valueOf(keyValue[1]));
		    	break;
		    case "FLOAT":
		    	map.put(tag, Float.valueOf(keyValue[1]));
		    	break;
		    case "ITEMSTACK":
		    	try
				{
					map.put(tag, fromBase64itemStack(keyValue[1]));
				} catch (IOException e)
		    	{
					e.printStackTrace();
				}
		    	break;
		    case "ITEMSTACKARRAY":
		    	try
				{
					map.put(tag, fromBase64itemStackArray(keyValue[1]));
				} catch (IOException e){
					e.printStackTrace();
				}
		    	break;
		    case "ANIMALTAMER":
		    	String[] split = keyValue[1].split("!");
		    	map.put(tag, new AnimalTamer()
				{
		    		String uuid = split[0];
			    	String playername = split[1];
					@Override
					public UUID getUniqueId()
					{
						if(uuid == null || uuid.isEmpty())
						{
							return null;
						}
						return UUID.fromString(uuid);
					}
					
					@Override
					public String getName()
					{
						return playername;
					}
				});
		    	break;
		    case "AXOLOTLVARIANT":
		    	map.put(tag, Axolotl.Variant.valueOf(keyValue[1]));
		    	break;
		    case "CATTYPE":
		    	map.put(tag, Cat.Type.valueOf(keyValue[1]));
		    	break;
		    case "DYECOLOR":
		    	map.put(tag, DyeColor.getByColor(Color.fromRGB(Integer.valueOf(keyValue[1]))));
		    	break;
		    case "ENTITYTYPE":
		    	map.put(tag, EntityType.valueOf(keyValue[1]));
		    	break;
		    case "FOXTYPE":
		    	map.put(tag, Fox.Type.valueOf(keyValue[1]));
		    	break;
		    case "HORSECOLOR":
		    	map.put(tag, Horse.Color.valueOf(keyValue[1]));
		    	break;
		    case "HORSESTYLE":
		    	map.put(tag, Horse.Style.valueOf(keyValue[1]));
		    	break;
		    case "MERCHAT":
		    	map.put(tag, recipeDeserialze(keyValue[1]));
		    	break;
		    case "MUSHROOMCOWVARIANT":
		    	map.put(tag, MushroomCow.Variant.valueOf(keyValue[1]));
		    	break;
		    case "PANDAGENE":
		    	map.put(tag, Panda.Gene.valueOf(keyValue[1]));
		    	break;
		    case "PARROTVARIANT":
		    	map.put(tag, Parrot.Variant.valueOf(keyValue[1]));
		    	break;
		    case "RABBITTYPE":
		    	map.put(tag, Rabbit.Type.valueOf(keyValue[1]));
		    	break;
		    case "TROPICALFISHPATTERN":
		    	map.put(tag, TropicalFish.Pattern.valueOf(keyValue[1]));
		    	break;
		    case "VILLAGERPROFESSION":
		    	map.put(tag, Villager.Profession.valueOf(keyValue[1]));
		    	break;
		    case "VILLAGERTYPE":
		    	map.put(tag, Villager.Type.valueOf(keyValue[1]));
		    	break;
		    }
		}
		return map;
	}
	
	private static String recipeSerialize(List<MerchantRecipe> list)
	{
		String s = "";
		for(int i = 0; i < list.size(); i++)
		{
			MerchantRecipe mr = list.get(i);
			if(mr == null)
			{
				continue;
			}
			ItemStack[] isa = new ItemStack[mr.getIngredients().size()];
			isa = mr.getIngredients().toArray(isa);
			s += toBase64itemStackArray(isa)+";!;";
			s += String.valueOf(mr.hasExperienceReward())+";!;";
			s += String.valueOf(mr.getMaxUses())+";!;";
			s += String.valueOf(mr.getPriceMultiplier())+";!;";
			s += String.valueOf(mr.getResult())+";!;";
			s += String.valueOf(mr.getUses())+";!;";
			s += String.valueOf(mr.getVillagerExperience());
			if(i+1 < list.size())
			{
				s += "?!?";
			}
		}
		return s;
	}
	
	private static ArrayList<MerchantRecipe> recipeDeserialze(String data)
	{
		ArrayList<MerchantRecipe> list = new ArrayList<>();
		String[] section = data.split("?!?");
		for(String s : section)
		{
			String[] mr = s.split(";!;");
			if(mr.length != 7)
			{
				continue;
			}
			ItemStack[] isa = null;
			ItemStack res = null;
			try
			{
				isa = fromBase64itemStackArray(mr[0]);
				res = fromBase64itemStack(mr[4]);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			boolean hex = Boolean.valueOf(mr[1]);
			int maxu = Integer.valueOf(mr[2]);
			float pmu = Float.valueOf(mr[3]);
			int use = Integer.valueOf(mr[5]);
			int vex = Integer.valueOf(mr[6]);
			MerchantRecipe mer = new MerchantRecipe(res, maxu);
			mer.setExperienceReward(hex);
			for(ItemStack i : isa)
			{
				mer.addIngredient(i);
			}
			mer.setPriceMultiplier(pmu);
			mer.setUses(use);
			mer.setVillagerExperience(vex);
			list.add(mer);
		}
		return list;
	}
	
	private static String toBase64itemStackArray(ItemStack[] items) throws IllegalStateException  //FIN
    {
    	try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeInt(items.length);
            
            for (int i = 0; i < items.length; i++) 
            {
                dataOutput.writeObject(items[i]);
            }
            
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    
    private static ItemStack[] fromBase64itemStackArray(String data) throws IOException  //FIN
    {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++) 
            {
            	items[i] = (ItemStack) dataInput.readObject();
            }
            
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) 
    	{
            throw new IOException("Unable to decode class type.", e);
        }
    }
    
    private static String toBase64itemStack(ItemStack item) throws IllegalStateException  //FIN
    {
    	try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeObject(item);
            
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    
    private static ItemStack fromBase64itemStack(String data) throws IOException  //FIN
    {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack items = (ItemStack) dataInput.readObject();
            
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) 
    	{
            throw new IOException("Unable to decode class type.", e);
        }
    }
}