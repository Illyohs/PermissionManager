package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;

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
        String name = subject instanceof Player? PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

        if(perm != null) {
            source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectRemovePermission", CoreUtil.createMap("subject", name, "permission", permission)));
            subject.removePermission(permission);
        }
        else
            source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectRemovePermissionDoesntHavePerm", CoreUtil.createMap("subject", name, "permission", permission)));
    }

}
