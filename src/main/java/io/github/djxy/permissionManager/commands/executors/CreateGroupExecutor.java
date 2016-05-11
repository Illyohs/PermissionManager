package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.Permissions;
import org.spongepowered.api.command.CommandSource;

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
            source.sendMessage(Main.getTranslatorInstance().translate(source, "createGroup", CoreUtil.createMap("group", group)));
        }
        else
            source.sendMessage(Main.getTranslatorInstance().translate(source, "createGroupAlreadyExist", CoreUtil.createMap("group", group)));
    }

}
