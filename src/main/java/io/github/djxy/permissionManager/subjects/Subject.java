package io.github.djxy.permissionManager.subjects;

import io.github.djxy.core.files.ObjectSerializer;
import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.listeners.GroupListener;
import io.github.djxy.permissionManager.listeners.SubjectListener;
import io.github.djxy.permissionManager.rules.Rule;
import io.github.djxy.permissionManager.rules.RuleService;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-03-27.
 */
public abstract class Subject implements ObjectSerializer {

    public static final String GLOBAL_LOCATION = UUID.randomUUID().toString();

    private final Listener listener;
    protected String identifier;
    private final CopyOnWriteArrayList<SubjectListener> subjectListeners;
    private final PermissionMap globalPermissions;
    private final Map<String, PermissionMap> permissionsPerWorld;
    private final CopyOnWriteArrayList<Group> globalGroups;
    private final Map<String, CopyOnWriteArrayList<Group>> groupsPerWorld;
    private final Map<Group, String> groupsLocation;
    private final Map<String, Map<String, String>> datas;//Key/Location/Value
    private String prefix = "";

    public Subject(String identifier) {
        this.identifier = identifier;
        this.subjectListeners = new CopyOnWriteArrayList<>();
        this.globalGroups = new CopyOnWriteArrayList<>();
        this.groupsPerWorld = new ConcurrentHashMap<>();
        this.globalPermissions = new PermissionMap();
        this.permissionsPerWorld = new ConcurrentHashMap<>();
        this.groupsLocation = new ConcurrentHashMap<>();
        this.listener = new Listener();
        this.datas = new ConcurrentHashMap<>();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public PermissionMap getGlobalPermissions() {
        return globalPermissions.clone();
    }

    public Map<String, PermissionMap> getWorldsPermissions() {
        Map<String, PermissionMap> permissionMaps = new HashMap<>();

        for(String world : permissionsPerWorld.keySet())
            permissionMaps.put(world, permissionsPerWorld.get(world).clone());

        return permissionMaps;
    }

    public PermissionMap getWorldPermissions(String world) {
        if(permissionsPerWorld.containsKey(world))
            return permissionsPerWorld.get(world).clone();
        else
            return new PermissionMap();
    }

    public Map<String, PermissionMap> getPermissionsPerWorld() {
        return permissionsPerWorld;
    }

    public void addListener(SubjectListener listener){
        subjectListeners.add(listener);
    }

    public void removeListener(SubjectListener listener){
        subjectListeners.remove(listener);
    }

    public List<Group> getGlobalGroups() {
        return (List<Group>) globalGroups.clone();
    }

    public List<Group> getWorldGroups(String world){
        if(groupsPerWorld.containsKey(world))
            return (List<Group>) groupsPerWorld.get(world).clone();
        else
            return new ArrayList<>();
    }

    public Map<String, List<Group>> getWorldsGroups(){
        Map<String, List<Group>> groups = new HashMap<>();

        for(String world : groupsPerWorld.keySet())
            groups.put(world, getWorldGroups(world));

        return groups;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean hasPermission(String world, String perm){
        return getPermissionValue(world, perm) != null;
    }

    public Permission getPermissionValue(String world, String permission){
        return getPermissionValue(world, permission, true, new HashMap<>());
    }

    protected Permission getPermissionValue(String world, String permission, boolean firstCheck, Map<Group, Object> groupChecked){
        Permission perm = null;

        if(firstCheck) {
            perm = getPermission(world, permission);

            if (perm == null) {
                perm = getPermissionController(permission);

                if(perm != null)
                    return perm;
            } else {
                return perm;
            }
        }

        List<Group> worldGroups = groupsPerWorld.get(world);

        if(worldGroups != null) {
            for (int i = 0; i < worldGroups.size() && perm == null; i++) {
                Group group = worldGroups.get(i);
                perm = group.getPermission(world, permission);

                if (perm == null)
                    perm = group.getPermissionController(permission);
            }

        }

        for(int i = 0; i < globalGroups.size() && perm == null; i++){
            Group group = globalGroups.get(i);
            perm = group.getPermission(world, permission);

            if(perm == null)
                perm = group.getPermissionController(permission);
        }

        if(perm != null)
            return perm;

        if(worldGroups != null) {
            for (int i = 0; i < worldGroups.size() && perm == null; i++) {
                Group group = worldGroups.get(i);

                if(!groupChecked.containsKey(group)) {
                    groupChecked.put(group, null);
                    perm = group.getPermissionValue(world, permission, false, groupChecked);
                }
            }
        }

        for(int i = 0; i < globalGroups.size() && perm == null; i++){
            Group group = globalGroups.get(i);

            if(!groupChecked.containsKey(group)) {
                groupChecked.put(group, null);
                perm = group.getPermissionValue(world, permission, false, groupChecked);
            }
        }

        return perm;
    }

    protected String getDataValue(String world, String key, boolean firstCheck, Map<Group, Object> groupChecked){
        String value = null;

        if(firstCheck) {
            value = getData(world, key);

            if (value == null) {
                value = getData(key);

                if(value != null)
                    return value;
            } else {
                return value;
            }
        }

        List<Group> worldGroups = groupsPerWorld.get(world);

        if(worldGroups != null) {
            for (int i = 0; i < worldGroups.size() && value == null; i++) {
                Group group = worldGroups.get(i);
                value = group.getData(world, key);

                if (value == null)
                    value = group.getData(key);
            }

            if(value != null)
                return value;
        }

        for(int i = 0; i < globalGroups.size() && value == null; i++){
            Group group = globalGroups.get(i);
            value = group.getData(world, key);

            if(value == null)
                value = group.getData(key);
        }

        if(value != null)
            return value;

        if(worldGroups != null) {
            for (int i = 0; i < worldGroups.size() && value == null; i++) {
                Group group = worldGroups.get(i);

                if(!groupChecked.containsKey(group)) {
                    groupChecked.put(group, null);
                    value = group.getDataValue(world, key, false, groupChecked);
                }
            }
        }

        for(int i = 0; i < globalGroups.size() && value == null; i++){
            Group group = globalGroups.get(i);

            if(!groupChecked.containsKey(group)) {
                groupChecked.put(group, null);
                value = group.getDataValue(world, key, false, groupChecked);
            }
        }

        return value;
    }

    public boolean hasGroup(Group group){
        return groupsLocation.containsKey(group);
    }

    public void addGroup(Group group){
        removeGroup(group, false);

        group.addListener(listener);
        groupsLocation.put(group, GLOBAL_LOCATION);
        globalGroups.add(group);

        Collections.sort(globalGroups);
    }

    public void addGroup(String world, Group group){
        removeGroup(group, false);
        CopyOnWriteArrayList<Group> groups = groupsPerWorld.get(world);

        groupsLocation.put(group, world);

        if(groups == null)
            groupsPerWorld.put(world, (groups = new CopyOnWriteArrayList<>()));

        group.addListener(listener);
        groups.add(group);

        Collections.sort(groups);
    }

    public void removeGroup(Group group){
        removeGroup(group, true);
    }

    public Permission getPermission(String permission){
        return globalPermissions.get(permission);
    }

    protected Permission getPermissionController(String permission){
        return globalPermissions.getPermissionControllerOf(permission);
    }

    public Permission getPermission(String world, String permission){
        Map<String, Permission> worldPermissions = permissionsPerWorld.get(world);

        if(worldPermissions != null)
            return worldPermissions.get(permission);

        return null;
    }

    public void setPermission(Permission permission){
        System.out.println(globalPermissions.keySet());
        globalPermissions.put(permission.getPermission(), permission);
        System.out.println(globalPermissions.keySet());

        for (SubjectListener listener : subjectListeners)
            listener.onPermissionSet(this, permission);
    }

    public void setPermission(String world, Permission value){
        PermissionMap worldPermissions = permissionsPerWorld.get(world);

        if(worldPermissions == null)
            permissionsPerWorld.put(world, (worldPermissions = new PermissionMap()));

        worldPermissions.put(value.getPermission(), value);

        for (SubjectListener listener : subjectListeners)
            listener.onPermissionSet(this, world, value);
    }

    public void removePermission(String permission){
        Permission perm = globalPermissions.remove(permission);

        if(perm != null)
            for (SubjectListener listener : subjectListeners)
                listener.onPermissionRemove(this, perm);
    }

    public void removePermission(String world, String permission){
        Map<String, Permission> worldPermissions = permissionsPerWorld.get(world);

        if(worldPermissions != null) {
            Permission perm = worldPermissions.remove(permission);

            if(perm != null)
                for (SubjectListener listener : subjectListeners)
                    listener.onPermissionRemove(this, world, perm);
        }
    }

    public void clearGlobalPermissions(){
        for(Permission permission : globalPermissions.values())
            for (SubjectListener listener : subjectListeners)
                listener.onPermissionRemove(this, permission);

        globalPermissions.clear();
    }

    public void clearWorldsPermissions(){
        for(String world : permissionsPerWorld.keySet())
            for(Permission permission : permissionsPerWorld.get(world).values())
                for (SubjectListener listener : subjectListeners)
                    listener.onPermissionRemove(this, world, permission);

        permissionsPerWorld.clear();
    }

    public void clearWorldPermissions(String world){
        if(permissionsPerWorld.containsKey(world)) {
            for(Permission permission : permissionsPerWorld.get(world).values())
                for (SubjectListener listener : subjectListeners)
                    listener.onPermissionRemove(this, world, permission);

            permissionsPerWorld.get(world).clear();
        }
    }

    public void clearWorldsGroups(){
        groupsPerWorld.clear();
    }

    public void clearGlobalGroups(){
        globalGroups.clear();
    }

    public void clearWorldGroups(String world){
        if(groupsPerWorld.containsKey(world))
            groupsPerWorld.get(world).clear();
    }

    public String getDataValue(String world, String key){
        return getDataValue(world, key, true, new HashMap<>());
    }

    public Map<String, String> getGlobalData() {
        return getWorldData(Subject.GLOBAL_LOCATION);
    }

    public Map<String, String> getWorldData(String world) {
        if(datas.containsKey(world))
            return new HashMap<>(datas.get(world));
        else
            return new HashMap<>();
    }

    public Map<String, Map<String, String>> getWorldsData() {
        Map<String, Map<String, String>> map = new HashMap<>();

        for(String key : datas.keySet())
            if(!Subject.GLOBAL_LOCATION.equals(key))
                map.put(key, new HashMap<>(datas.get(key)));

        return map;
    }

    public void setData(String key, String value){
        setData(GLOBAL_LOCATION, key, value);
    }

    public void setData(String world, String key, String value){
        if(!datas.containsKey(key))
            datas.put(key, new ConcurrentHashMap<>());

        datas.get(key).put(world, value);
    }

    public String getData(String key){
        return getData(GLOBAL_LOCATION, key);
    }

    public String getData(String world, String key){
        if(datas.containsKey(key))
            return datas.get(key).get(world);

        return null;
    }

    public void clearGlobalData(){
        datas.remove(Subject.GLOBAL_LOCATION);
    }

    public void clearWorldsData(){
        for(String location : datas.keySet())
            if(!Subject.GLOBAL_LOCATION.equals(location))
                datas.remove(location);
    }

    public void clearWorldData(String world){
        datas.remove(world);
    }

    @Override
    public void initFromNode(ConfigurationNode node){
        prefix = node.getNode("prefix").getString("");

        if(!node.getNode("permissions").isVirtual()){
            List<ConfigurationNode> permissions = (List<ConfigurationNode>) node.getNode("permissions").getChildrenList();

            for(ConfigurationNode permission : permissions){
                String perm = permission.getString();
                boolean value = !perm.startsWith("-");

                if(!value)
                    perm = perm.substring(1);

                Permission p = new Permission(perm, value);

                if(!node.getNode("rules", perm).isVirtual()){
                    Map<Object, ConfigurationNode> rules = (Map<Object, ConfigurationNode>) node.getNode("rules", perm).getChildrenMap();

                    for(Object rule : rules.keySet()){
                        Rule r = RuleService.getInstance().createRule((String) rule);

                        if(r != null)
                            r.initFromNode(rules.get(rule));

                        p.addRule(r);
                    }
                }

                setPermission(p);
            }
        }

        if(!node.getNode("groups").isVirtual()){
            List<ConfigurationNode> groups = (List<ConfigurationNode>) node.getNode("groups").getChildrenList();

            for(ConfigurationNode group : groups)
                addGroup(PermissionManager.getInstance().getOrCreateGroup(group.getString()));
        }

        if(!node.getNode("worlds").isVirtual()) {
            Map<Object, ConfigurationNode> worlds = (Map<Object, ConfigurationNode>) node.getNode("worlds").getChildrenMap();

            for(Object w : worlds.keySet()) {
                ConfigurationNode world = worlds.get(w);

                if (!world.getNode("permissions").isVirtual()) {
                    List<ConfigurationNode> permissions = (List<ConfigurationNode>) world.getNode("permissions").getChildrenList();

                    for (ConfigurationNode permission : permissions) {
                        String perm = permission.getString();
                        boolean value = !perm.startsWith("-");

                        if (!value)
                            perm = perm.substring(1);

                        Permission p = new Permission(perm, value);

                        if (!world.getNode("rules", perm).isVirtual()) {
                            Map<Object, ConfigurationNode> rules = (Map<Object, ConfigurationNode>) world.getNode("rules", perm).getChildrenMap();

                            for (Object rule : rules.keySet()) {
                                Rule r = RuleService.getInstance().createRule((String) rule);

                                if (r != null)
                                    r.initFromNode(rules.get(rule));

                                p.addRule(r);
                            }
                        }

                        setPermission((String) w, p);
                    }
                }

                if (!world.getNode("groups").isVirtual()) {
                    List<ConfigurationNode> groups = (List<ConfigurationNode>) world.getNode("groups").getChildrenList();

                    for (ConfigurationNode group : groups)
                        addGroup((String) w, PermissionManager.getInstance().getOrCreateGroup(group.getString()));
                }
            }
        }

        if(!node.getNode("data").isVirtual()){
            Map<Object, ConfigurationNode> datas = (Map<Object, ConfigurationNode>) node.getNode("data").getChildrenMap();

            for(Object data : datas.keySet())
                setData((String) data, datas.get(data).getString(""));
        }

        if(!node.getNode("worlds").isVirtual()) {
            Map<Object, ConfigurationNode> worlds = (Map<Object, ConfigurationNode>) node.getNode("worlds").getChildrenMap();

            for (Object w : worlds.keySet()) {
                ConfigurationNode world = worlds.get(w);

                if(!world.getNode("data").isVirtual()){
                    Map<Object, ConfigurationNode> datas = (Map<Object, ConfigurationNode>) world.getNode("data").getChildrenMap();

                    for(Object data : datas.keySet())
                        setData((String) w, (String) data, datas.get(data).getString(""));
                }
            }
        }
    }

    @Override
    public void setNode(ConfigurationNode node){
        node.getNode("prefix").setValue(prefix);

        if(!globalPermissions.isEmpty()) {
            List<String> permissions = new ArrayList<>(globalPermissions.values().size());

            for (Permission permission : globalPermissions.values()) {
                permissions.add((permission.getValue() ? "" : "-") + permission.getPermission());

                for(Rule rule : permission.getRules())
                    rule.setNode(node.getNode("rules", permission.getPermission(), RuleService.getInstance().getRuleName(rule.getClass())));
            }

            node.getNode("permissions").setValue(permissions);
        }

        for(Map.Entry pairs : permissionsPerWorld.entrySet()) {
            List<String> permissions = new ArrayList<>(((PermissionMap) pairs.getValue()).values().size());

            if(((PermissionMap) pairs.getValue()).values().size() != 0) {
                for (Permission permission : ((PermissionMap) pairs.getValue()).values()) {
                    permissions.add((permission.getValue() ? "" : "-") + permission.getPermission());

                    for (Rule rule : permission.getRules())
                        rule.setNode(node.getNode("worlds", pairs.getKey(), "rules", permission.getPermission(), RuleService.getInstance().getRuleName(rule.getClass())));
                }

                node.getNode("worlds", pairs.getKey(), "permissions").setValue(permissions);
            }
        }

        if(!globalGroups.isEmpty()) {
            List<String> groups = new ArrayList<>(globalGroups.size());

            for (Group group : globalGroups)
                groups.add(group.getIdentifier());

            node.getNode("groups").setValue(groups);
        }

        for(Map.Entry pairs : groupsPerWorld.entrySet()) {
            List<String> groups = new ArrayList<>(((List<Group>) pairs.getValue()).size());

            if(((List<Group>) pairs.getValue()).size() != 0) {
                for (Group group : ((List<Group>) pairs.getValue()))
                    groups.add(group.getIdentifier());

                node.getNode("worlds", pairs.getKey(), "groups").setValue(groups);
            }
        }

        for(String key : datas.keySet()){
            if(!datas.get(key).isEmpty()){
                Map<String, String> data = datas.get(key);

                for(String location : data.keySet()) {
                    if(location.equals(Subject.GLOBAL_LOCATION))
                        node.getNode("data", key).setValue(data.get(location));
                    else
                        node.getNode("worlds", location, "data", key).setValue(data.get(location));
                }
            }
        }
    }

    private void removeGroup(Group group, boolean sort){
        String location = groupsLocation.get(group);

        if(location != null){
            if(location.equals(GLOBAL_LOCATION)) {
                if(globalGroups.remove(group)) {
                    if (sort)
                        Collections.sort(globalGroups);
                }
            }
            else {
                List<Group> groups = groupsPerWorld.get(location);

                if(groups.remove(group)) {
                    if (groups.isEmpty())
                        groupsPerWorld.remove(location);
                    else if (sort)
                        Collections.sort(groupsPerWorld.get(location));
                }
            }

            group.removeListener(listener);
            groupsLocation.remove(group);
        }
    }

    private class Listener implements SubjectListener, GroupListener {

        @Override
        public void onPermissionSet(Subject subject, Permission permission) {}

        @Override
        public void onPermissionSet(Subject subject, String world, Permission permission) {}

        @Override
        public void onPermissionRemove(Subject subject, Permission permission) {}

        @Override
        public void onPermissionRemove(Subject subject, String world, Permission permission) {}

        @Override
        public void onRankChange(Group group) {
            String location = Subject.this.groupsLocation.get(group);

            if(location.equals(GLOBAL_LOCATION))
                Collections.sort(Subject.this.globalGroups);
            else if(Subject.this.groupsPerWorld.containsKey(location))
                Collections.sort(Subject.this.groupsPerWorld.get(location));
        }

        @Override
        public void onDelete(Group group) {
            Subject.this.removeGroup(group);
        }

        @Override
        public void onIdentifierChange(Group group, String lastIdentifier) {}
    }

}
