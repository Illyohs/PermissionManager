package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.repositories.PlayerRepository;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-10.
 */
public class PlayerSetDataWorldExecutor extends CommandExecutor {

    public PlayerSetDataWorldExecutor() {
        setPermission(Permissions.PLAYER_SET_DATA);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Player subject = (Player) values.get("subject");
        String key = (String) values.get("key");
        String value = (String) values.get("value");
        String worldName =((World) values.get("world")).getName();
        String name = PlayerRepository.getInstance().getPlayerName(subject.getUUID());

        source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, name, RESET_COLOR, " has now the value ", INFO_COLOR, key, RESET_COLOR, " set to ", INFO_COLOR, value, RESET_COLOR, " in the world ", INFO_COLOR, worldName, RESET_COLOR, ".")));
        subject.setData(worldName, key, value);
    }

}
