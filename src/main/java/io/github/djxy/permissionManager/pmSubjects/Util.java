package io.github.djxy.permissionManager.pmSubjects;

import org.spongepowered.api.service.context.Context;

import java.util.Set;

/**
 * Created by Samuel on 2016-03-28.
 */
public class Util {

    public static boolean isGlobalContext(Set<Context> set){
        return set == null || set.isEmpty();
    }

    public static boolean isWorldContext(Set<Context> set){
        return set.size() == 1 && set.iterator().next().getKey().equals("world");
    }

    public static String getWorldFromContext(Set<Context> set){
        return set.iterator().next().getValue();
    }

}
