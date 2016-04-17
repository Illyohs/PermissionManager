package io.github.djxy.permissionManager.subjects;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-03-27.
 */
public abstract class Player extends Subject {

    public static String SUFFIX = " <%player%>: ";

    private final UUID uuid;
    private final Map<String, Map<String, String>> datas;//Key/Location/Value
    private String suffix;

    abstract public String getCurrentWorld();

    public Player(UUID identifier) {
        super(identifier.toString());
        this.uuid = identifier;
        this.datas = new ConcurrentHashMap<>();
    }

    public String getSuffix() {
        return suffix == null?SUFFIX:suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void initFromNode(ConfigurationNode node) {
        super.initFromNode(node);

        suffix = node.getNode("suffix").isVirtual()?null:node.getNode("suffix").getString("");

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
    public void setNode(ConfigurationNode node) {
        super.setNode(node);

        if(suffix != null)
            node.getNode("suffix").setValue(suffix);

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

    public String getDisplayPrefix() {
        String displayPrefix = getPrefix();

        for(Group gr : getGlobalGroups())
            displayPrefix += gr.getPrefix();

        return displayPrefix;
    }

    public String getDataValue(String world, String key){
        String value = getData(world, key);

        if(value == null)
            value = getData(key);

        return value;
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

    public UUID getUUID() {
        return uuid;
    }

    public boolean hasPermission(String perm){
        return hasPermission(getCurrentWorld(), perm);
    }

}
