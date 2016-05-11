package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Samuel on 2016-04-13.
 */
public class SubjectRemovePrefixExecutor extends CommandExecutor {

    public SubjectRemovePrefixExecutor() {
        setPermission(Permissions.SUBJECT_REMOVE_PREFIX);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        String name = subject instanceof Player ? PlayerRepository.getInstance().getPlayerName(UUID.fromString(subject.getIdentifier())):subject.getIdentifier();

        source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectRemovePrefix", CoreUtil.createMap("subject", name)));

        subject.setPrefix("");
    }

}
