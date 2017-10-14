package org.kucro3.keleton.service.economy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.kucro3.keleton.auth.MurmurHash3;
import org.spongepowered.api.service.context.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Misc {
	private Misc()
	{
	}
	
	public static String serialize(Set<Context> contexts)
	{
		JsonArray array = new JsonArray();
		for(Context context : contexts)
		{
			JsonObject object = new JsonObject();
			object.addProperty("type", context.getType());
			object.addProperty("name", context.getName());
			array.add(object);
		}
		return new Gson().toJson(array);
	}
	
	public static Set<Context> deserialize(Set<Context> contexts, String json)
	{
		JsonArray array = new Gson().fromJson(json, JsonArray.class);
		for(JsonElement element : array)
		{
			JsonObject object = element.getAsJsonObject();
			String type = object.get("type").getAsString();
			String name = object.get("name").getAsString();
			contexts.add(new Context(type, name));
		}
		return contexts;
	}
	
	public static Set<Context> deserialize(String json)
	{
		return deserialize(new HashSet<>(), json);
	}
	
	public static BigDecimal hash128(String value)
	{
		MurmurHash3.LongPair out = new MurmurHash3.LongPair();
		byte[] byts = value.getBytes();
		MurmurHash3.murmurhash3_x64_128(byts, 0, byts.length, MurmurHash3.DEFAULT_SEED, out);
		return BigDecimal.valueOf(out.val1).multiply(BigDecimal.valueOf(1L << 32)).add(BigDecimal.valueOf(out.val2));
	}
	
	public static <K, V> Map<K, V> hashMap(K key, V value)
	{
		return put(key, value, new HashMap<>());
	}
	
	public static <K, V> Map<K, V> concurrentHashMap(K key, V value)
	{
		return put(key, value, new ConcurrentHashMap<>());
	}
	
	private static <K, V> Map<K, V> put(K key, V value, Map<K, V> map)
	{
		map.put(key, value);
		return map;
	}
	
	static int nosql()
	{
		return 0x00000001;
	}
	
	static boolean nosql(int v)
	{
		return (v & nosql()) != 0;
	}
	
	static int sync()
	{
		return 0x00000004;
	}
	
	static boolean sync(int v)
	{
		return (v & sync()) != 0;
	}
	
	static class Naming
	{
		static String unique(UUID uuid)
		{
			return unique(uuid.toString());
		}
		
		static String unique(String uuid)
		{
			return ACCOUNT_UNIQUE + uuid;
		}
		
		static boolean isUnique(String s)
		{
			return s.startsWith(ACCOUNT_UNIQUE);
		}
		
		static String virtual(String identifier)
		{
			return ACCOUNT_VIRTUAL + identifier;
		}
		
		static boolean isVirtual(String s)
		{
			return s.startsWith(ACCOUNT_VIRTUAL);
		}
		
		static String identifier(String s)
		{
			return s.substring(1);
		}
		
		private static final String ACCOUNT_UNIQUE = "U";
		
		private static final String ACCOUNT_VIRTUAL = "V";
		
		private Naming()
		{
		}
	}
}
