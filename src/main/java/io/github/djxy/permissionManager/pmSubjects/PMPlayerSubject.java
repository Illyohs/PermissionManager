package io.github.djxy.permissionManager.pmSubjects;

import com.google.common.collect.Sets;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.option.OptionSubject;
import org.spongepowered.api.service.permission.option.OptionSubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Samuel on 2016-03-28.
 */
public class PMPlayerSubject extends PMSubject implements OptionSubject, OptionSubjectData {

    private int tickHelpCommand = -1;

    public PMPlayerSubject(Player subject, SubjectCollection collection) {
        super(subject, collection);
    }

    public void updateTickHelpCommand() {
        this.tickHelpCommand = Sponge.getServer().getRunningTimeTicks();
    }

    public Player getPlayer(){
        return (Player) subject;
    }

    @Override
    public OptionSubjectData getSubjectData() {
        return this;
    }

    @Override
    public OptionSubjectData getTransientSubjectData() {
        return this;
    }

    @Override
    public Optional<String> getOption(Set<Context> set, String s) {
        String value;

        if(Util.isGlobalContext(set))
            return (value = getPlayer().getDataValue(getPlayer().getCurrentWorld(), s)) != null?Optional.of(value):Optional.empty();
        else if(Util.isWorldContext(set))
            return (value = getPlayer().getDataValue(Util.getWorldFromContext(set), s)) != null?Optional.of(value):Optional.empty();
        else
            return Optional.empty();
    }

    @Override
    public Map<Set<Context>, Map<String, String>> getAllOptions() {
        Map<Set<Context>, Map<String, String>> map = new HashMap<>();

        map.put(GLOBAL_CONTEXT, getPlayer().getGlobalData());

        Map<String, Map<String, String>> mapWorlds = getPlayer().getWorldsData();

        for(String world : mapWorlds.keySet())
            map.put(Sets.newHashSet(new Context("world", world)), mapWorlds.get(world));

        return map;
    }

    @Override
    public Map<String, String> getOptions(Set<Context> set) {
        if(Util.isGlobalContext(set))
            return getPlayer().getGlobalData();
        else if(Util.isWorldContext(set))
            return getPlayer().getWorldData(Util.getWorldFromContext(set));
        else
            return new HashMap<>();
    }

    @Override
    public boolean setOption(Set<Context> set, String s, String s1) {
        if(Util.isGlobalContext(set))
            getPlayer().setData(s, s1);
        else if(Util.isWorldContext(set))
            getPlayer().setData(Util.getWorldFromContext(set), s, s1);

        return true;
    }

    @Override
    public boolean clearOptions(Set<Context> set) {
        if(Util.isGlobalContext(set))
            getPlayer().clearGlobalData();
        else if(Util.isWorldContext(set))
            getPlayer().clearWorldData(Util.getWorldFromContext(set));

        return true;
    }

    @Override
    public boolean clearOptions() {
        getPlayer().clearGlobalData();
        getPlayer().clearWorldsData();
        return true;
    }

    @Override
    public boolean hasPermission(Set<Context> set, String s) {
        if(Util.isGlobalContext(set))
            return getPlayer().hasPermission(s);
        else if(Util.isWorldContext(set))
            return getPlayer().hasPermission(Util.getWorldFromContext(set), s);
        else
            return false;
    }

    @Override
    public Tristate getPermissionValue(Set<Context> set, String s) {
        if(Util.isGlobalContext(set))
            return Tristate.fromBoolean(getPermissionValue(getPlayer().getCurrentWorld(), s));
        else if(Util.isWorldContext(set))
            return Tristate.fromBoolean(getPermissionValue(Util.getWorldFromContext(set), s));
        else
            return Tristate.UNDEFINED;
    }

    private boolean getPermissionValue(String world, String permission){
        Permission perm = getPlayer().getPermissionValue(world, permission);

        if(perm != null) {
            if(Sponge.getServer().getRunningTimeTicks() == tickHelpCommand)
                return perm.getValue();
            else
                return perm.getValue(Sponge.getServer().getPlayer(getPlayer().getUUID()).get());
        }
        else
            return false;
    }

}
