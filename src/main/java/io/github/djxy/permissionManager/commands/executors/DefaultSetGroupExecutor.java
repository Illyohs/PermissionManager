package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-11.
 */
public class DefaultSetGroupExecutor extends CommandExecutor {

    public DefaultSetGroupExecutor() {
        setPermission(Permissions.SET_DEFAULT_GROUP);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Group group = (Group) values.get("group");

        source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, group.getIdentifier(), RESET_COLOR, " is now the default group.")));

        PermissionManager.getInstance().setDefaultGroup(group);
    }

}
