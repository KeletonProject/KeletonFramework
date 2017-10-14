package org.kucro3.keleton.service.permission;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.kucro3.keleton.auth.MurmurHash3;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.NodeTree;
import org.spongepowered.api.util.Tristate;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Misc {
	public static NodeTree newNodeTree()
	{
		return NodeTree.of(Collections.emptyMap());
	}
	
	public static NodeTree newNodeTree(String key, boolean value)
	{
		return NodeTree.of(ImmutableMap.of(key, value));
	}
	
	public static NodeTree merge(NodeTree tree, String path, Tristate value)
	{
		Map<String, Boolean> map = new HashMap<>(tree.asMap());
		
		if(value.equals(Tristate.UNDEFINED))
			map.remove(path);
		else
			map.put(path, value.asBoolean());
		
		return NodeTree.of(map);
	}
	
	public static BigDecimal hash128(String value)
	{
		MurmurHash3.LongPair out = new MurmurHash3.LongPair();
		byte[] byts = value.getBytes();
		MurmurHash3.murmurhash3_x64_128(byts, 0, byts.length, MurmurHash3.DEFAULT_SEED, out);
		return BigDecimal.valueOf(out.val1).multiply(BigDecimal.valueOf(1L << 32)).add(BigDecimal.valueOf(out.val2));
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
	
	static class Naming
	{
		static String table(String name)
		{
			return PREFIX_TABLE + name;
		}
		
		static String description_table(String name)
		{
			return PREFIX_TABLE_DESCRIPTION + name;
		}
		
		static String const_table(String name)
		{
			return PREFIX_TABLE_CONSTANT + name;
		}
		
		static String permission_data()
		{
			return PREFIX_DATA_PERMISSION;
		}
		
		static String parent_data()
		{
			return PREFIX_DATA_PARENT;
		}
		
		static String option_data()
		{
			return PREFIX_DATA_OPTION;
		}
		
		static String permission_data(String name)
		{
			return PREFIX_DATA_PERMISSION + name;
		}
		
		static String parent_data(String name)
		{
			return PREFIX_DATA_PARENT + name;
		}
		
		static String option_data(String name)
		{
			return PREFIX_DATA_OPTION + name;
		}
		
		static String data(String name)
		{
			return PREFIX_DATA + name;
		}
		
		static String attribute()
		{
			return PREFIX_ATTRIBUTE;
		}
		
		static String attribute(String name)
		{
			return PREFIX_ATTRIBUTE + name;
		}
		
		static String identifier(String name)
		{
			return name.substring(1);
		}
		
		static boolean isPermissionData(String name)
		{
			return name.startsWith(PREFIX_DATA_PERMISSION);
		}
		
		static boolean isParentData(String name)
		{
			return name.startsWith(PREFIX_DATA_PARENT);
		}
		
		static boolean isOptionData(String name)
		{
			return name.startsWith(PREFIX_DATA_OPTION);
		}
		
		static boolean isAttribute(String name)
		{
			return name.startsWith(PREFIX_ATTRIBUTE);
		}
		
		static boolean isData(String name)
		{
			return name.startsWith(PREFIX_DATA);
		}
		
		static boolean isTable(String name)
		{
			return name.startsWith(PREFIX_TABLE);
		}
		
		static boolean isConstantTable(String name)
		{
			return name.startsWith(PREFIX_TABLE_CONSTANT);
		}
		
		static boolean isDescriptionTable(String name)
		{
			return name.startsWith(PREFIX_TABLE_DESCRIPTION);
		}
		
		public static final String TRUE = "TRUE";
		
		public static final String FALSE = "FALSE";
		
		private static final String PREFIX_TABLE_DESCRIPTION = "P";
		
		private static final String PREFIX_DATA = "B";
		
		private static final String PREFIX_DATA_PERMISSION = "D";
		
		private static final String PREFIX_DATA_PARENT = "F";
		
		private static final String PREFIX_DATA_OPTION = "T";
		
		private static final String PREFIX_ATTRIBUTE = "A";
		
		private static final String PREFIX_TABLE = "X";
		
		private static final String PREFIX_TABLE_CONSTANT = "Z";
		
		private Naming()
		{
		}
	}
	
	static int nosql()
	{
		return 0x00000001;
	}
	
	static boolean nosql(int v)
	{
		return (v & nosql()) != 0;
	}
	
	static int ensured()
	{
		return 0x00000002;
	}
	
	static boolean ensured(int v)
	{
		return (v & ensured()) != 0;
	}
	
	static int sync()
	{
		return 0x00000004;
	}
	
	static boolean sync(int v)
	{
		return (v & sync()) != 0;
	}
	
	public static Supplier<? extends CommandSource> fromIdentifier(String identifier)
	{
		if(identifier.equalsIgnoreCase("SERVER"))
			return () -> Sponge.getServer().getConsole();
		try {
			return () -> Sponge.getServer().getPlayer(UUID.fromString(identifier)).orElse(null);
		} catch (IllegalArgumentException e) {
			return () -> null;
		}
	}
	
	private Misc()
	{
	}
}
