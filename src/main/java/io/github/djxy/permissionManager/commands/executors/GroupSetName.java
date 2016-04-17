package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

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
            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, group.getIdentifier(), RESET_COLOR, " is now named ", INFO_COLOR, name, RESET_COLOR, ".")));

            group.setIdentifier(name);
        }
        else
            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, group.getIdentifier(), RESET_COLOR, " is already named ", INFO_COLOR, name, RESET_COLOR, ".")));

    }

}
