package main.java.me.avankziar.spigot.bungeeteleportmanager.object.serialization;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.entity.LivingEntity;

import main.java.me.avankziar.general.object.MultiMap;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;

public class LivingEntitySerialization
{
	public static String serializeEntityAsString(LivingEntity entity) 
	{
		
		return null;
	}
	
	public static void mainOld(String[] args)
	{
		ArrayList<Object> key1 = new ArrayList<>();
		key1.add("Hallo");
		key1.add(Double.valueOf(5.0));
		key1.add(new ServerLocation("gateway", "spawn", 50, 10, 120, 20F, 0F).toString());
		ArrayList<Object> key2 = new ArrayList<>();
		key2.add(Integer.valueOf(42));
		key2.add("Moinsen");
		key2.add(new ServerLocation("gateway", "spawn", 447, 99, 8, 80F, 0F));
		ArrayList<Object> v1 = new ArrayList<>();
		v1.add(MysqlHandler.Type.BACK);
		v1.add("Avankziar");
		ArrayList<Object> v2 = new ArrayList<>();
		v2.add("Nemesis");
		v2.add(Float.valueOf(80F));
		LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
		map.put(key1, v1);
		map.put(key2, v2);
		ArrayList<Object> key3 = new ArrayList<>();
		key3.add("Hallo");
		key3.add(Double.valueOf(5.0));
		key3.add(new ServerLocation("gateway", "spawn", 50, 10, 120, 20F, 0F).toString());
		
		System.out.println(map.toString());
		System.out.println(key3.toString());
		System.out.println(map.containsKey(key3));
		
		MultiMap<Object, Object> mm = new MultiMap<>()
				.setKeys("Hallo", Double.valueOf(5.0), new ServerLocation("gateway", "spawn", 50, 10, 120, 20F, 0F).toString())
				.setValues(MysqlHandler.Type.HOME, "Tragur");
		System.out.println(mm.toString());
	}
	/*protected LivingEntitySerialization() {
	}
	
	public static JSONObject serializeEntity(LivingEntity entity) {
		if(entity instanceof Player) {
			return null;
		}
		try {
			JSONObject root = new JSONObject();
			if(shouldSerialize("age") && entity instanceof Ageable)
				root.put("age", ((Ageable) entity).getAge());
			if(shouldSerialize("health"))
				root.put("health", entity.getHealth());
			if(shouldSerialize("name"))
				root.put("name", entity.getCustomName());
			root.put("type", entity.getType().getTypeId());
			return root;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String serializeEntityAsString(LivingEntity entity) {
		return serializeEntityAsString(entity, false);
	}
	
	public static String serializeEntityAsString(LivingEntity entity, boolean pretty) {
		return serializeEntityAsString(entity, pretty, 5);
	}
	
	public static String serializeEntityAsString(LivingEntity entity, boolean pretty, int indentFactor) {
		try {
			if(pretty) {
				return serializeEntity(entity).toString(indentFactor);
			} else {
				return serializeEntity(entity).toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LivingEntity spawnEntity(Location location, String stats) {
		try {
			return spawnEntity(location, new JSONObject(stats));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LivingEntity spawnEntity(Location location, JSONObject stats) {
		try {
			if(!stats.has("type")) {
				throw new IllegalArgumentException("The type of the entity cannot be determined");
			} else {
				LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.fromId(stats.getInt("type")));
				if(stats.has("age") && entity instanceof Ageable)
					((Ageable) entity).setAge(stats.getInt("age"));
				if(stats.has("health"))
					entity.setHealth(stats.getDouble("health"));
				if(stats.has("name"))
					entity.setCustomName(stats.getString("name"));
				if(stats.has("potion-effects"))
					;
				PotionEffectSerialization.addPotionEffects(stats.getString("potion-effects"), entity);
				return entity;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean shouldSerialize(String key) {
		return SerializationConfig.getShouldSerialize("living-entity." + key);
	}*/
	
}