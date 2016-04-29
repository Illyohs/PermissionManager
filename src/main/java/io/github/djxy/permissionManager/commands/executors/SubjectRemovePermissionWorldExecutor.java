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

/**
 * Created by Samuel on 2016-04-09.
 */
public class SubjectRemovePermissionWorldExecutor extends CommandExecutor {

    public SubjectRemovePermissionWorldExecutor() {
        setPermission(Permissions.SUBJECT_REMOVE_PERMISSION);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        String permission = (String) values.get("permission");
        String worldName =((World) values.get("world")).getName();
        Permission perm = subject.getPermission(worldName, permission);

        if(perm != null) {
            String name = subject instanceof Player ? PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " no longer has the permission ", INFO_COLOR, permission, RESET_COLOR, " in the world ", INFO_COLOR, worldName, RESET_COLOR,".")));

            subject.removePermission(worldName, permission);
        }
        else {
            String name = subject instanceof Player ?PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " doesn't have the permission ", INFO_COLOR, permission, RESET_COLOR, " in the world ", INFO_COLOR, worldName, RESET_COLOR,".")));
        }
    }

}
