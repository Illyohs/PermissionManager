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
import org.spongepowered.api.world.World;

import java.util.HashMap;
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
        String worldName = ((World) values.get("world")).getName();
        String name = subject instanceof Player?PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

        if(subject.hasGroup(group))
            source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectAddGroupAlreadyInGroup", CoreUtil.createMap("group", group.getIdentifier(), "subject", name)));
        else{
            if(!subject.getIdentifier().equals(group.getIdentifier())) {
                source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectAddGroupWorld", createMap("group", group.getIdentifier(), "subject", name, "world", worldName)));
                subject.addGroup(worldName, group);
            }
            else
                source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectAddGroupSameSubject", CoreUtil.createMap("subject", name)));
        }
    }

    private Map<String,Object> createMap(String key1, Object value1, String key2, Object value2, String key3, Object value3){
        HashMap<String,Object> map = new HashMap<>();

        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);

        return map;
    }
}
