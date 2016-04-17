package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.files.fileManagers.ConfigFile;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-10.
 */
public class SetTimeIntervalForSavingExecutor extends CommandExecutor {

    private final ConfigFile configFile;

    public SetTimeIntervalForSavingExecutor(ConfigFile configFile) {
        this.configFile = configFile;
        setPermission(Permissions.SET_SAVING_INTERVAL);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        int time = (int) values.get("time");

        if(time > 0) {
            source.sendMessage(PREFIX.concat(Text.of("The saving interval has been set to ", INFO_COLOR, time, RESET_COLOR, " minute(s).")));

            configFile.setTimeIntervalForSaving(time);
        }
        else
            source.sendMessage(PREFIX.concat(Text.of("The saving interval can't be lower than 1.")));
    }

}
