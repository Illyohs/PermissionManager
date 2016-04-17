package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-09.
 */
public class DefaultSetSuffixExecutor extends CommandExecutor {

    public DefaultSetSuffixExecutor() {
        setPermission(Permissions.SET_DEFAULT_SUFFIX);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        String suffix = (String) values.get("suffix");

        Player.SUFFIX = suffix;
        source.sendMessage(PREFIX.concat(TextSerializers.FORMATTING_CODE.deserialize(suffix).concat(Text.of(" is now the default suffix."))));
    }

}
