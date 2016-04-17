package io.github.djxy.permissionManager;

import io.github.djxy.permissionManager.listeners.GroupListener;
import io.github.djxy.permissionManager.listeners.SubjectListener;
import io.github.djxy.permissionManager.repositories.PermissionRepository;
import io.github.djxy.permissionManager.subjects.*;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-03-28.
 */
public class PermissionManager {

    private static final PermissionManager instance = new PermissionManager();

    public static PermissionManager getInstance() {
        return instance;
    }

    private Group defaultGroup;
    private final ConcurrentHashMap<String,Group> groups = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID,Player> players = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,CustomSubject> customSubjects = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, CopyOnWriteArrayList<Subject>>> subjectsPerPermission = new ConcurrentHashMap<>();

    private PermissionManager() {
    }

    public List<Player> getPlayers(){
        return new ArrayList<>(players.values());
    }

    public List<Group> getGroups(){
        return new ArrayList<>(groups.values());
    }

    public Group getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(Group defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public boolean hasGroup(String identifier){
        return groups.containsKey(identifier);
    }

    public boolean hasPlayer(UUID identifier){
        return players.containsKey(identifier);
    }

    public boolean hasCustomSubject(String identifier){
        return customSubjects.containsKey(identifier);
    }

    public CustomSubject getOrCreateCustomSubject(String identifier){
        if(customSubjects.containsKey(identifier))
            return customSubjects.get(identifier);
        else{
            CustomSubject subject = new CustomSubject(identifier);

            customSubjects.put(identifier, subject);

            subject.addListener(new Listener());

            return subject;
        }
    }

    public Group getGroup(String identifier){
        return identifier != null?groups.get(identifier):null;
    }

    public Player getPlayer(UUID uuid){
        return uuid != null?players.get(uuid):null;
    }

    public Group getOrCreateGroup(String identifier){
        if(groups.containsKey(identifier))
            return groups.get(identifier);
        else{
            Group group = new Group(identifier);

            groups.put(identifier, group);

            group.addListener(new Listener());

            return group;
        }
    }

    public Player getOrCreatePlayer(UUID uuid){
        return getOrCreatePlayer(uuid, false);
    }

    public Player getOrCreatePlayer(UUID uuid, boolean addDefaultGroup){
        if(players.containsKey(uuid)) {
            return players.get(uuid);
        }
        else{
            Player player = new Player(uuid) {
                @Override
                public String getCurrentWorld() {
                    return Sponge.getServer().getPlayer(uuid).get().getWorld().getName();
                }
            };

            players.put(uuid, player);

            if(addDefaultGroup)
                player.addGroup(getDefaultGroup());

            player.addListener(new Listener());

            return player;
        }
    }

    private class Listener implements SubjectListener, GroupListener {

        @Override
        public void onPermissionSet(Subject subject, Permission permission) {
            PermissionRepository.getInstance().addPermission(permission.getPermission());

            if(!subjectsPerPermission.containsKey(permission.getPermission()))
                subjectsPerPermission.put(permission.getPermission(), new ConcurrentHashMap<>());

            if(!subjectsPerPermission.get(permission.getPermission()).containsKey(Subject.GLOBAL_LOCATION))
                subjectsPerPermission.get(permission.getPermission()).put(Subject.GLOBAL_LOCATION, new CopyOnWriteArrayList<>());

            if(permission.getValue())
                subjectsPerPermission.get(permission.getPermission()).get(Subject.GLOBAL_LOCATION).add(subject);
        }

        @Override
        public void onPermissionSet(Subject subject, String world, Permission permission) {
            PermissionRepository.getInstance().addPermission(permission.getPermission());

            if(!subjectsPerPermission.containsKey(permission.getPermission()))
                subjectsPerPermission.put(permission.getPermission(), new ConcurrentHashMap<>());

            if(!subjectsPerPermission.get(permission.getPermission()).containsKey(world))
                subjectsPerPermission.get(permission.getPermission()).put(world, new CopyOnWriteArrayList<>());

            if(permission.getValue())
                subjectsPerPermission.get(permission.getPermission()).get(world).add(subject);
        }

        @Override
        public void onPermissionRemove(Subject subject, Permission permission) {
            if(subjectsPerPermission.containsKey(permission.getPermission()))
                if(subjectsPerPermission.get(permission.getPermission()).containsKey(Subject.GLOBAL_LOCATION))
                    subjectsPerPermission.get(permission.getPermission()).get(Subject.GLOBAL_LOCATION).remove(subject);
        }

        @Override
        public void onPermissionRemove(Subject subject, String world, Permission permission) {
            if(subjectsPerPermission.containsKey(permission.getPermission()))
                if(subjectsPerPermission.get(permission.getPermission()).containsKey(world))
                    subjectsPerPermission.get(permission.getPermission()).get(world).remove(subject);
        }

        @Override
        public void onRankChange(Group subject) {}

        @Override
        public void onDelete(Group subject) {
            PermissionManager.this.groups.remove(subject.getIdentifier());

            if(PermissionManager.this.defaultGroup == subject)
                PermissionManager.this.setDefaultGroup(PermissionManager.this.getOrCreateGroup("default"));
        }

        @Override
        public void onIdentifierChange(Group group, String lastIdentifier) {
            PermissionManager.this.groups.remove(lastIdentifier, group);
            PermissionManager.this.groups.put(group.getIdentifier(), group);
        }

    }

}
