package io.github.djxy.permissionManager.subjects;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-03-27.
 */
public abstract class Player extends Subject {

    public static String PLAYER_NAME_FORMAT = "<%player%>";
    public static String TEXT_FORMAT = "%f&r: ";

    private final Map<String,String> fullWorldsPrefix = new ConcurrentHashMap<>();
    private final Map<String,String> fullWorldsSuffix = new ConcurrentHashMap<>();
    private String fullSuffix = "";
    private String fullPrefix = "";
    private final UUID uuid;
    private String playerNameFormat;
    private Group groupTextFormat;

    abstract public String getCurrentWorld();

    public Player(UUID identifier) {
        super(identifier.toString());
        this.uuid = identifier;
    }

    public String getNameFormat() {
        return playerNameFormat == null? PLAYER_NAME_FORMAT :playerNameFormat;
    }

    public void setNameFormat(String playerNameFormat) {
        this.playerNameFormat = playerNameFormat;
    }

    @Override
    public void initFromNode(ConfigurationNode node) {
        super.initFromNode(node);

        playerNameFormat = node.getNode("format", "name").isVirtual()?null:node.getNode("format", "name").getString("");
    }

    @Override
    public void setNode(ConfigurationNode node) {
        super.setNode(node);

        if(playerNameFormat != null)
            node.getNode("nameFormat").setValue(playerNameFormat);
    }

    public String getFullPrefix(String world) {
        return fullWorldsPrefix.containsKey(world)?fullPrefix+fullWorldsPrefix.get(world):fullPrefix;
    }

    public String getFullSuffix(String world){
        return fullWorldsSuffix.containsKey(world)?fullSuffix+fullWorldsSuffix.get(world):fullSuffix;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean hasPermission(String perm){
        return hasPermission(getCurrentWorld(), perm);
    }

    @Override
    public String getTextFormat() {
        String format = super.getTextFormat();

        if(format == null){
            if(groupTextFormat != null){
                if(groupTextFormat.getTextFormat() == null){
                    setTextFormat();
                    return getTextFormat();
                }
                else
                    return groupTextFormat.getTextFormat();
            }
            else {
                return TEXT_FORMAT;
            }
        }
        else
            return format;
    }

    @Override
    public void setTextFormat(String textFormat) {
        super.setTextFormat(textFormat);
        setTextFormat();
    }

    @Override
    public void addGroup(Group group) {
        super.addGroup(group);
        setFullPrefixSuffix();
        setTextFormat();
    }

    @Override
    public void addGroup(String world, Group group) {
        super.addGroup(world, group);
        setFullWorldPrefixSuffix(world);
    }

    @Override
    public void removeGroup(Group group) {
        String location = getGroupLocation(group);
        super.removeGroup(group);
        setTextFormat();

        if(location != null){
            if(location.equals(GLOBAL_LOCATION))
                setFullPrefixSuffix();
            else
                setFullWorldPrefixSuffix(location);
        }
    }

    private void setTextFormat(){
        if(super.getTextFormat() == null){
            List<Group> groups = getGlobalGroups();
            groupTextFormat = null;

            for(int i = 0; i < groups.size() && groupTextFormat == null; i++)
                if(groups.get(i).getTextFormat() != null)
                    groupTextFormat = groups.get(i);
        }
    }

    private void setFullPrefixSuffix(){
        fullPrefix = getPrefix();
        fullSuffix = getSuffix();

        for(Group gr : getGlobalGroups()) {
            fullPrefix += gr.getPrefix();
            fullSuffix += gr.getSuffix();
        }
    }

    private void setFullWorldPrefixSuffix(String world){
        String fullPrefix = "";
        String fullSuffix = "";

        for(Group gr : getWorldGroups(world)){
            fullPrefix += gr.getPrefix();
            fullSuffix += gr.getSuffix();
        }

        fullWorldsPrefix.put(world, fullPrefix);
        fullWorldsSuffix.put(world, fullSuffix);
    }
}
