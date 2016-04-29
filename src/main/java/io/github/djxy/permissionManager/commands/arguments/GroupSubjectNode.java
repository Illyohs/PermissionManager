package io.github.djxy.permissionManager.commands.arguments;

import io.github.djxy.core.commands.nodes.ArgumentNode;
import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.subjects.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-04-03.
 */
public class GroupSubjectNode extends ArgumentNode {

    public GroupSubjectNode(String alias, String name) {
        super(alias, name);
    }

    public GroupSubjectNode(String alias) {
        super(alias);
    }

    @Override
    public Object getValue(String argument) {
        return PermissionManager.getInstance().getGroup(argument);
    }

    @Override
    public List<String> complete(String complete) {
        List<String> groups = new ArrayList<>();

        for (Group group : PermissionManager.getInstance().getGroups())
            if (group.getIdentifier().toLowerCase().startsWith(complete.toLowerCase()))
                groups.add(group.getIdentifier());

        return groups;
    }
}
