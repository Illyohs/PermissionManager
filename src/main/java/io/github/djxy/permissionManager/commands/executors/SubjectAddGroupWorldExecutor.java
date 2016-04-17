package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.repositories.PlayerRepository;
import io.github.djxy.permissionManager.subjects.Group;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-10.
 */
public class SubjectAddGroupWorldExecutor extends CommandExecutor {

    public SubjectAddGroupWorldExecutor() {
        setPermission(Permissions.SUBJECT_REMOVE_PERMISSION);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        Group group = (Group) values.get("group");
        String worldName =((World) values.get("world")).getName();

        if(subject.hasGroup(group)){
            if(subject instanceof Player){
                String name = PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID());

                source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " is already a member of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, ".")));
            }
            else
                source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, subject.getIdentifier(), RESET_COLOR, " already inherit of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, ".")));
        }
        else{

            if(subject instanceof Player){
                String name = PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID());

                source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR,  " is now a member of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, " in the world ", INFO_COLOR, worldName, RESET_COLOR,".")));
                subject.addGroup(worldName, group);
            }
            else {
                if(!subject.getIdentifier().equals(group.getIdentifier())) {
                    source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, subject.getIdentifier(), RESET_COLOR, " inherit of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, " in the world ", INFO_COLOR, worldName, RESET_COLOR, ".")));
                    subject.addGroup(worldName, group);
                }
                else
                    source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, subject.getIdentifier(), RESET_COLOR, " can't inherit of the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, ".")));
            }
        }
    }

}
