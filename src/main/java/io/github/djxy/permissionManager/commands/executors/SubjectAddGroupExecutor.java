package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Group;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-10.
 */
public class SubjectAddGroupExecutor extends CommandExecutor {

    public SubjectAddGroupExecutor() {
        setPermission(Permissions.SUBJECT_ADD_GROUP);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        Group group = (Group) values.get("group");
        String name = subject instanceof Player?PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

        if(subject.hasGroup(group))
            source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectAddGroupAlreadyInGroup", CoreUtil.createMap("group", group.getIdentifier(), "subject", name)));
        else{
            if(!subject.getIdentifier().equals(group.getIdentifier())) {
                source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectAddGroup", CoreUtil.createMap("group", group.getIdentifier(), "subject", name)));
                subject.addGroup(group);
            }
            else
                source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectAddGroupSameSubject", CoreUtil.createMap("subject", name)));
        }
    }

}
