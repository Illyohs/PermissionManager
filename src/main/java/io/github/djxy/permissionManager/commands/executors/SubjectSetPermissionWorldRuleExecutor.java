package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.permissionManager.rules.Rule;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.world.World;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-10.
 */
public class SubjectSetPermissionWorldRuleExecutor extends CommandExecutor {

    public SubjectSetPermissionWorldRuleExecutor() {
        setPermission("");
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        String permission = (String) values.get("permission");
        String worldName =((World) values.get("world")).getName();
        Rule rule = (Rule) values.get("rule");

        Permission perm = subject.getPermission(worldName, permission);

        if(perm != null)
            perm.addRule(rule);
    }

}
