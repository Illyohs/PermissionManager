package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-09.
 */
public class GroupSetName extends CommandExecutor {

    public GroupSetName() {
        setPermission(Permissions.GROUP_SET_NAME);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Group group = (Group) values.get("subject");
        String name = (String) values.get("name");

        if(!group.getIdentifier().equals(name)) {
            source.sendMessage(Main.getTranslatorInstance().translate(source, "setGroupName", CoreUtil.createMap("group", group.getIdentifier(), "newName", name)));

            group.setIdentifier(name);
        }
        else
            source.sendMessage(Main.getTranslatorInstance().translate(source, "setGroupNameSameName", CoreUtil.createMap("group", group.getIdentifier(), "name", name)));
    }

}
