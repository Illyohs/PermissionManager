package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-09.
 */
public class CreateGroupExecutor extends CommandExecutor {

    public CreateGroupExecutor() {
        setPermission(Permissions.CREATE_GROUP);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        String group = (String) values.get("group");

        if(!PermissionManager.getInstance().hasGroup(group)) {
            PermissionManager.getInstance().getOrCreateGroup(group);
            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, group, RESET_COLOR, " has been created.")));
        }
        else
            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, group, RESET_COLOR, " already exist.")));
    }

}
