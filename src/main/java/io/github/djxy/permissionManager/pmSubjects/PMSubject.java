package io.github.djxy.permissionManager.pmSubjects;

import com.google.common.collect.Sets;
import io.github.djxy.permissionManager.PMService;
import io.github.djxy.permissionManager.subjects.Group;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.PermissionMap;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.*;

/**
 * Created by Samuel on 2016-03-28.
 */
public abstract class PMSubject implements Subject, SubjectData {

    protected final io.github.djxy.permissionManager.subjects.Subject subject;
    private final boolean isPlayer;
    private final SubjectCollection collection;

    public PMSubject(io.github.djxy.permissionManager.subjects.Subject subject, SubjectCollection collection) {
        this.subject = subject;
        this.isPlayer = subject instanceof Player;
        this.collection = collection;
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return isPlayer?Optional.of(Sponge.getServer().getPlayer(((Player)subject).getUUID()).get()):Optional.empty();
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return collection;
    }

    @Override
    public SubjectData getSubjectData() {
        return this;
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return this;
    }

    @Override
    public boolean isChildOf(Set<Context> set, Subject subject) {
        if(subject instanceof PMGroupSubject){
            if (SubjectUtil.isGlobalContext(set))
                return this.subject.getGlobalGroups().contains(((PMGroupSubject) subject).getGroup());
            else if (SubjectUtil.isWorldContext(set))
                return this.subject.getWorldGroups(SubjectUtil.getWorldFromContext(set)).contains(((PMGroupSubject) subject).getGroup());
        }

        return false;
    }

    //////////////////////

    @Override
    public Map<Set<Context>, Map<String, Boolean>> getAllPermissions() {
        Map<Set<Context>, Map<String, Boolean>> permissions = new HashMap<>();
        Map<String, Boolean> global = new HashMap<>();

        PermissionMap map = subject.getGlobalPermissions();

        for(String permission : map.keySet())
            global.put(permission, map.get(permission).getValue());

        permissions.put(GLOBAL_CONTEXT, global);

        Map<String, PermissionMap> worlds = subject.getWorldsPermissions();
        Map<String, Boolean> worldMap;

        for(String world : worlds.keySet()){
            Context context = new Context("world", world);
            worldMap = new HashMap<>();

            permissions.put(Sets.newHashSet(context), worldMap);

            for(String permission : map.keySet())
                worldMap.put(permission, map.get(permission).getValue());
        }

        return permissions;
    }

    @Override
    public Map<String, Boolean> getPermissions(Set<Context> set) {
        Map<String, Boolean> permissions = new HashMap<>();

        if (SubjectUtil.isGlobalContext(set)){
            PermissionMap map = subject.getGlobalPermissions();

            for(String permission : map.keySet())
                permissions.put(permission, map.get(permission).getValue());
        }
        else if (SubjectUtil.isWorldContext(set)){
            PermissionMap map = subject.getWorldPermissions(SubjectUtil.getWorldFromContext(set));

            for(String permission : map.keySet())
                permissions.put(permission, map.get(permission).getValue());
        }

        return permissions;
    }

    @Override
    public boolean setPermission(Set<Context> set, String s, Tristate tristate) {
        if(tristate != Tristate.UNDEFINED) {
            if (SubjectUtil.isGlobalContext(set))
                subject.setPermission(new Permission(s, tristate.asBoolean()));
            else if (SubjectUtil.isWorldContext(set))
                subject.setPermission(SubjectUtil.getWorldFromContext(set), new Permission(s, tristate.asBoolean()));
        }
        else {
            if (SubjectUtil.isGlobalContext(set))
                subject.removePermission(s);
            else if (SubjectUtil.isWorldContext(set))
                subject.removePermission(SubjectUtil.getWorldFromContext(set), s);
        }

        return true;
    }

    @Override
    public boolean clearPermissions() {
        subject.clearGlobalPermissions();
        subject.clearWorldsPermissions();
        return true;
    }

    @Override
    public boolean clearPermissions(Set<Context> set) {
        if (SubjectUtil.isGlobalContext(set))
            subject.clearGlobalPermissions();
        else if (SubjectUtil.isWorldContext(set))
            subject.clearWorldPermissions(SubjectUtil.getWorldFromContext(set));

        return true;
    }

    @Override
    public Map<Set<Context>, List<Subject>> getAllParents() {
        Map<Set<Context>, List<Subject>> subjects = new HashMap<>();

        subjects.put(SubjectData.GLOBAL_CONTEXT, getParents(SubjectData.GLOBAL_CONTEXT));

        for(Map.Entry pairs : subject.getWorldsGroups().entrySet()) {
            Context context = new Context("world", (String) pairs.getKey());

            subjects.put(Sets.newHashSet(context), getParents(Sets.newHashSet(context)));
        }

        return null;
    }

    @Override
    public List<Subject> getParents(Set<Context> set) {
        PMService service = PMService.getInstance();
        List<Subject> parents = new ArrayList<>();

        if (SubjectUtil.isGlobalContext(set)){
            for(Group group : this.subject.getGlobalGroups())
                parents.add(service.getGroupSubjects().get(group.getIdentifier()));
        }
        else if (SubjectUtil.isWorldContext(set)){
            for(Group group : this.subject.getWorldGroups(SubjectUtil.getWorldFromContext(set)))
                parents.add(service.getGroupSubjects().get(group.getIdentifier()));
        }

        return parents;
    }

    @Override
    public boolean addParent(Set<Context> set, Subject subject) {
        if(subject instanceof PMGroupSubject) {
            if (SubjectUtil.isGlobalContext(set))
                this.subject.addGroup(((PMGroupSubject) subject).getGroup());
            else if (SubjectUtil.isWorldContext(set))
                this.subject.addGroup(SubjectUtil.getWorldFromContext(set), ((PMGroupSubject) subject).getGroup());

                return true;
        }

        return false;
    }

    @Override
    public boolean removeParent(Set<Context> set, Subject subject) {
        if(subject instanceof PMGroupSubject) {
            this.subject.removeGroup(((PMGroupSubject) subject).getGroup());
            return true;
        }

        return false;
    }

    @Override
    public boolean clearParents() {
        subject.clearGlobalGroups();
        subject.clearWorldsGroups();
        return true;
    }

    @Override
    public boolean clearParents(Set<Context> set) {
        if (SubjectUtil.isGlobalContext(set))
            subject.clearGlobalGroups();
        else if (SubjectUtil.isWorldContext(set))
            subject.clearWorldGroups(SubjectUtil.getWorldFromContext(set));

        return true;
    }

    @Override
    public String getIdentifier() {
        return subject.getIdentifier();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return new HashSet<>();
    }

}
