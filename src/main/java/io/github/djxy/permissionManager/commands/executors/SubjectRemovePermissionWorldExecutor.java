package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
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
        String name = subject instanceof Player ?PlayerRepository.getInstance().getPlayerName(((Player) subject).getUUID()):subject.getIdentifier();

        if(perm != null) {
            source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectRemovePermissionWorld", createMap("subject", name, "permission", permission, "world", worldName)));
            subject.removePermission(worldName, permission);
        }
        else
            source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectRemovePermissionDoesntHavePerm", CoreUtil.createMap("subject", name, "permission", permission)));
    }

    private Map<String,Object> createMap(String key1, Object value1, String key2, Object value2, String key3, Object value3){
        HashMap<String,Object> map = new HashMap<>();

        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);

        return map;
    }

}
