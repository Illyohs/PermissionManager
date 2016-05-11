package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.world.World;

import java.util.HashMap;
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

        source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectSetPermissionWorld", createMap("subject", name, "permission", permission, "world", worldName, "value", value)));

        Permission perm = subject.getPermission(worldName, permission);

        if(perm != null)
            perm.setValue(value);
        else
            subject.setPermission(worldName, new Permission(permission, value));
    }

    private Map<String,Object> createMap(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4){
        HashMap<String,Object> map = new HashMap<>();

        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);

        return map;
    }

}
