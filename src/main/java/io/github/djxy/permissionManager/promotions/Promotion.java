package io.github.djxy.permissionManager.promotions;

import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.files.ObjectSerializer;
import io.github.djxy.permissionManager.listeners.GroupListener;
import io.github.djxy.permissionManager.subjects.Group;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-04-13.
 */
public class Promotion implements ObjectSerializer {

    private final List<Group> groupsToAdd;
    private final List<Group> groupsToRemove;
    private final Listener listener;
    private String name = "";

    public Promotion() {
        this.groupsToAdd = new CopyOnWriteArrayList<>();
        this.groupsToRemove = new CopyOnWriteArrayList<>();
        this.listener = new Listener();
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public void remove(Player player){
        for(Group group : groupsToAdd)
            player.removeGroup(group);

        for(Group group : groupsToRemove)
            player.addGroup(group);
    }

    public void apply(Player player){
        for(Group group : groupsToRemove)
            player.removeGroup(group);

        for(Group group : groupsToAdd)
            player.addGroup(group);
    }

    public void addGroupToAdd(Group group){
        group.addListener(listener);
        groupsToAdd.add(group);
    }

    public void addGroupToRemove(Group group){
        group.addListener(listener);
        groupsToRemove.add(group);
    }

    public void removeGroupToAdd(Group group){
        group.removeListener(listener);
        groupsToAdd.remove(group);
    }

    public void removeGroupToRemove(Group group){
        group.removeListener(listener);
        groupsToRemove.remove(group);
    }

    @Override
    public void initFromNode(ConfigurationNode node) {
        List<ConfigurationNode> groupsAdd = (List<ConfigurationNode>) node.getNode("groups", "add").getChildrenList();
        List<ConfigurationNode> groupsRemove = (List<ConfigurationNode>) node.getNode("groups", "remove").getChildrenList();
        PermissionManager pm = PermissionManager.getInstance();

        for(ConfigurationNode grp : groupsAdd){
            String group = grp.getString();

            if(pm.hasGroup(group))
                addGroupToAdd(pm.getOrCreateGroup(group));
        }
        for(ConfigurationNode grp : groupsRemove){
            String group = grp.getString();

            if(pm.hasGroup(group))
                addGroupToRemove(pm.getOrCreateGroup(group));
        }
    }

    @Override
    public void setNode(ConfigurationNode node) {
        ArrayList<String> groupsRemove = new ArrayList<>();
        ArrayList<String> groupsAdd = new ArrayList<>();

        for(Group group : groupsToAdd)
            groupsAdd.add(group.getIdentifier());

        for(Group group : groupsToRemove)
            groupsRemove.add(group.getIdentifier());

        if(!groupsAdd.isEmpty())
            node.getNode("groups", "add").setValue(groupsAdd);

        if(!groupsRemove.isEmpty())
            node.getNode("groups", "remove").setValue(groupsRemove);
    }

    private class Listener implements GroupListener {

        @Override
        public void onRankChange(Group group) {}

        @Override
        public void onDelete(Group group) {
            Promotion.this.groupsToAdd.remove(group);
            Promotion.this.groupsToRemove.remove(group);
            group.removeListener(Promotion.this.listener);
        }

        @Override
        public void onIdentifierChange(Group group, String lastIdentifier) {}

        @Override
        public void onPermissionSet(Subject subject, Permission permission) {}

        @Override
        public void onPermissionSet(Subject subject, String world, Permission permission) {}

        @Override
        public void onPermissionRemove(Subject subject, Permission permission) {}

        @Override
        public void onPermissionRemove(Subject subject, String world, Permission permission) {}
    }

}
