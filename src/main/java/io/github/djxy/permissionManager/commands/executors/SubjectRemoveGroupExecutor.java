package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.repositories.PlayerRepository;
import io.github.djxy.permissionManager.subjects.Group;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

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

        if(subject.hasGroup(group)){
            if(subject instanceof Player){
                String name = PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID());

                source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " is no longer a member of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, ".")));
            }
            else
                source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, subject.getIdentifier(), RESET_COLOR, " no longer inherit of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, ".")));

            subject.removeGroup(group);
        }
        else {
            if(subject instanceof Player){
                String name = PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID());

                source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " is not a member of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, ".")));
            }
            else
                source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, subject.getIdentifier(), RESET_COLOR, " doesn't inherit of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, ".")));
        }
    }

}
