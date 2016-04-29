package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Samuel on 2016-04-09.
 */
public class SubjectSetPermissionWorldExecutor extends CommandExecutor {

    public SubjectSetPermissionWorldExecutor() {
        setPermission(Permissions.SUBJECT_SET_PERMISSION);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        String permission = (String) values.get("permission");
        String worldName =((World) values.get("world")).getName();
        boolean value = (Boolean) values.get("value");
        String name = subject instanceof Player? PlayerRepository.getInstance().getPlayerName(UUID.fromString(subject.getIdentifier())):subject.getIdentifier();

        source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " has now the permission ", INFO_COLOR, permission, RESET_COLOR, " set to ", INFO_COLOR, value, RESET_COLOR, " in the world ", INFO_COLOR, worldName, RESET_COLOR, ".")));

        Permission perm = subject.getPermission(worldName, permission);

        if(perm != null)
            perm.setValue(value);
        else
            subject.setPermission(worldName, new Permission(permission, value));
    }

}
