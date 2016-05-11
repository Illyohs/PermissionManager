package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.action.TextActions;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-04-09.
 */
public class DeleteGroupExecutor extends CommandExecutor {

    private final ConcurrentHashMap<UUID, Long> timeValid = new ConcurrentHashMap<>();

    public DeleteGroupExecutor() {
        setPermission(Permissions.DELETE_GROUP);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Group subject = (Group) values.get("group");
        UUID random = UUID.randomUUID();
        long time = System.currentTimeMillis()/1000;

        timeValid.put(random, time);

        source.sendMessage(Main.getTranslatorInstance().translate(source, "deleteGroup", CoreUtil.createMap("group", subject.getIdentifier(), "clickHere", TextActions.executeCallback(
                source1 -> {
                    long currentTime = System.currentTimeMillis() / 1000;

                    if (timeValid.containsKey(random) && timeValid.get(random) + 15 >= currentTime) {
                        timeValid.remove(random);
                        source.sendMessage(Main.getTranslatorInstance().translate(source, "deleteGroupConfirmed", CoreUtil.createMap("group", subject.getIdentifier())));
                        subject.delete();
                    } else
                        source.sendMessage(Main.getTranslatorInstance().translate(source, "actionNoLongerPossible", null));
                }
        ))));
    }

}
