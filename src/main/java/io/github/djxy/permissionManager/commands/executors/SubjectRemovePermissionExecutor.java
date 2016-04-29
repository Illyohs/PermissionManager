package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-09.
 */
public class SubjectRemovePermissionExecutor extends CommandExecutor {

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        String permission = (String) values.get("permission");
        Permission perm = subject.getPermission(permission);

        if(perm != null) {
            String name = subject instanceof Player? PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " no longer has the permission ", INFO_COLOR, permission, RESET_COLOR, " globally.")));

            subject.removePermission(permission);
        }
        else {
            String name = subject instanceof Player?PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " doesn't have the permission ", INFO_COLOR, permission, RESET_COLOR, " globally.")));
        }
    }

}
