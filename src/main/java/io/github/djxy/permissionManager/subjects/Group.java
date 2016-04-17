package io.github.djxy.permissionManager.subjects;

import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.listeners.GroupListener;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-03-27.
 */
public class Group extends Subject implements Comparable<Group> {

    private final CopyOnWriteArrayList<GroupListener> groupListeners;
    private int rank = Integer.MAX_VALUE;

    public Group(String identifier) {
        super(identifier);
        this.groupListeners = new CopyOnWriteArrayList<>();
    }

    public void setIdentifier(String identifier) {
        String last = this.identifier;
        this.identifier = identifier;

        for(GroupListener listener : groupListeners)
            listener.onIdentifierChange(this, last);
    }

    @Override
    public void initFromNode(ConfigurationNode node) {
        super.initFromNode(node);

        setRank(node.getNode("rank").getInt(Integer.MAX_VALUE));

        if(node.getNode("default").getBoolean(false))
            PermissionManager.getInstance().setDefaultGroup(this);
    }

    @Override
    public void setNode(ConfigurationNode node) {
        super.setNode(node);

        if(rank != Integer.MAX_VALUE)
            node.getNode("rank").setValue(rank);

        if(PermissionManager.getInstance().getDefaultGroup() == this)
            node.getNode("default").setValue(true);
    }

    public void addListener(GroupListener listener){
        super.addListener(listener);
        groupListeners.add(listener);
    }

    public void removeListener(GroupListener listener){
        super.addListener(listener);
        groupListeners.remove(listener);
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank < 0?0:rank;

        for(GroupListener listener : groupListeners)
            listener.onRankChange(this);
    }

    public void delete(){
        for(GroupListener listener : groupListeners)
            listener.onDelete(this);
    }

    @Override
    public int compareTo(Group o) {
        if(this.getRank() == o.getRank()) return 0;
        if(this.getRank() < o.getRank()) return -1;
        if(this.getRank() > o.getRank()) return 1;

        return 0;
    }

}
