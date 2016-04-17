package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.repositories.PlayerRepository;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Samuel on 2016-04-13.
 */
public class PlayerRemoveSuffixExecutor extends CommandExecutor {

    public PlayerRemoveSuffixExecutor() {
        setPermission(Permissions.PLAYER_REMOVE_SUFFIX);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Player subject = (Player) values.get("subject");
        String name = PlayerRepository.getInstance().getPlayerName(UUID.fromString(subject.getIdentifier()));

        source.sendMessage(PREFIX.concat(Text.of("The suffix of ", INFO_COLOR, name, RESET_COLOR, " is now the default suffix.")));

        subject.setSuffix(null);
    }

}
