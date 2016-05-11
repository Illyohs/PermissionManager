package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.fileManagers.ConfigFile;
import org.spongepowered.api.command.CommandSource;

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
            source.sendMessage(Main.getTranslatorInstance().translate(source, "setSavingInterval", CoreUtil.createMap("minute", time)));

            configFile.setTimeIntervalForSaving(time);
        }
        else
            source.sendMessage(Main.getTranslatorInstance().translate(source, "setSavingIntervalCantLower1", null));
    }

}
