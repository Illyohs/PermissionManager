package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.command.CommandSource;

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

        source.sendMessage(Main.getTranslatorInstance().translate(source, "removePlayerSuffix", CoreUtil.createMap("player", name)));

        subject.setSuffix(null);
    }

}
