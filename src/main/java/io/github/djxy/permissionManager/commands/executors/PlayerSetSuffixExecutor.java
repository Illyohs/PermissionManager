package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.repositories.PlayerRepository;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Samuel on 2016-04-09.
 */
public class PlayerSetSuffixExecutor extends CommandExecutor {

    public PlayerSetSuffixExecutor() {
        setPermission(Permissions.PLAYER_SET_SUFFIX);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Player subject = (Player) values.get("subject");
        String suffix = (String) values.get("suffix");
        String name = PlayerRepository.getInstance().getPlayerName(UUID.fromString(subject.getIdentifier()));

        source.sendMessage(PREFIX.concat(Text.of("The suffix of ", INFO_COLOR, name, RESET_COLOR, " is now ").concat(TextSerializers.FORMATTING_CODE.deserialize(suffix+"&f").concat(Text.of(".")))));

        subject.setSuffix(suffix);
    }

}
