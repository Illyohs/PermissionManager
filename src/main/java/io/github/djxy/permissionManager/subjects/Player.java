package io.github.djxy.permissionManager.subjects;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.UUID;

/**
 * Created by Samuel on 2016-03-27.
 */
public abstract class Player extends Subject {

    public static String SUFFIX = "<%player%>: ";

    private final UUID uuid;
    private String suffix;

    abstract public String getCurrentWorld();

    public Player(UUID identifier) {
        super(identifier.toString());
        this.uuid = identifier;
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
    }

    @Override
    public void setNode(ConfigurationNode node) {
        super.setNode(node);

        if(suffix != null)
            node.getNode("suffix").setValue(suffix);
    }

    public String getDisplayPrefix() {
        String displayPrefix = getPrefix();

        for(Group gr : getGlobalGroups())
            displayPrefix += gr.getPrefix();

        return displayPrefix;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean hasPermission(String perm){
        return hasPermission(getCurrentWorld(), perm);
    }

}
