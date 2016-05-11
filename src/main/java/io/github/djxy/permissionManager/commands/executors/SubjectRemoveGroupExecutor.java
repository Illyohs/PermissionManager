package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.subjects.Group;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-09.
 */
public class SubjectRemoveGroupExecutor extends CommandExecutor {

    public SubjectRemoveGroupExecutor() {
        setPermission(Permissions.SUBJECT_REMOVE_GROUP);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        Group group = (Group) values.get("group");
        String name = subject instanceof Player?PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

        if(subject.hasGroup(group)){
            source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectRemoveGroup", CoreUtil.createMap("subject", name, "group", group.getIdentifier())));
            subject.removeGroup(group);
        }
        else
            source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectRemoveGroupDoesntHaveGroup", CoreUtil.createMap("subject", name, "group", group.getIdentifier())));
    }

}
