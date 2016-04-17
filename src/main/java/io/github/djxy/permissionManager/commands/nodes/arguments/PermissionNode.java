package io.github.djxy.permissionManager.commands.nodes.arguments;

import io.github.djxy.permissionManager.commands.nodes.ArgumentNode;
import io.github.djxy.permissionManager.repositories.PermissionRepository;

import java.util.List;

/**
 * Created by Samuel on 2016-04-09.
 */
public class PermissionNode extends ArgumentNode {

    public PermissionNode(String alias, String name) {
        super(alias, name);
    }

    public PermissionNode(String alias) {
        super(alias);
    }

    @Override
    public Object getValue(String arg) {
        return arg;
    }

    @Override
    protected List<String> complete(String complete) {
        return PermissionRepository.getInstance().getPermissionNodes(complete);
    }

}
