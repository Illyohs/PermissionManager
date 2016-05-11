package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.command.CommandSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samuel on 2016-04-10.
 */
public class PlayerSetDataExecutor extends CommandExecutor {

    public PlayerSetDataExecutor() {
        setPermission(Permissions.PLAYER_SET_DATA);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Player subject = (Player) values.get("subject");
        String key = (String) values.get("key");
        String value = (String) values.get("value");
        String name = PlayerRepository.getInstance().getPlayerName(subject.getUUID());
        HashMap<String,Object> val = new HashMap<>();
        val.put("key", key);
        val.put("value", value);
        val.put("player", name);

        source.sendMessage(Main.getTranslatorInstance().translate(source, "setPlayerData", val));
        subject.setData(key, value);
    }

}
