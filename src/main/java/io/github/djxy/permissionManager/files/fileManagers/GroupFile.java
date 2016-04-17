package io.github.djxy.permissionManager.files.fileManagers;

import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.files.FileManager;
import io.github.djxy.permissionManager.subjects.Group;
import ninja.leaping.configurate.ConfigurationNode;

import java.nio.file.Path;

/**
 * Created by Samuel on 2016-04-02.
 */
public class GroupFile extends FileManager {

    public GroupFile(Path folder) {
        super(folder, "groups");
    }

    @Override
    protected void save(ConfigurationNode root) {
        for(Group group : PermissionManager.getInstance().getGroups())
            group.setNode(root.getNode(group.getIdentifier()));
    }

    @Override
    protected void load(ConfigurationNode root) {
        for(Object node : root.getChildrenMap().keySet()){
            Group group = PermissionManager.getInstance().getOrCreateGroup((String) node);
            group.initFromNode(root.getNode(node));
        }
    }
}