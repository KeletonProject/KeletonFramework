package org.kucro3.keleton.misc;

import org.spongepowered.api.service.context.Context;

import java.util.HashSet;
import java.util.Set;

public class ContextUtil {
    public static Set<Context> filter(Set<Context> contexts, String... keys)
    {
        Set<String> keySet = new HashSet<>();
        Set<Context> result = new HashSet<>();

        for(String key : keys)
            keySet.add(key);

        for(Context context : contexts)
            if(!keySet.contains(context.getKey()))
                result.add(context);

        return result;
    }
}
