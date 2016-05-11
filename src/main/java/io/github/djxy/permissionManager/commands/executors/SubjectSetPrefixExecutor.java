package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Player;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Samuel on 2016-04-09.
 */
public class SubjectSetPrefixExecutor extends CommandExecutor {

    public SubjectSetPrefixExecutor() {
        setPermission(Permissions.SUBJECT_SET_PREFIX);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Subject subject = (Subject) values.get("subject");
        String prefix = (String) values.get("prefix");
        String name = subject instanceof Player ? PlayerRepository.getInstance().getPlayerName(UUID.fromString(subject.getIdentifier())):subject.getIdentifier();

        source.sendMessage(Main.getTranslatorInstance().translate(source, "subjectSetPrefix", CoreUtil.createMap("subject", name, "prefix", TextSerializers.FORMATTING_CODE.deserialize(prefix + "&f"))));

        subject.setPrefix(prefix);
    }

}
