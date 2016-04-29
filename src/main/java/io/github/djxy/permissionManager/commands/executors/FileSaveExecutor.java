package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.files.FileManager;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-10.
 */
public class FileSaveExecutor extends CommandExecutor {

    public FileSaveExecutor() {
        setPermission(Permissions.FILE_SAVE);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        FileManager fileManager = (FileManager) values.get("file");

        try {
            fileManager.save();
            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, fileManager.getName(), RESET_COLOR, " is now saved!")));
        } catch (Exception e) {
            source.sendMessage(PREFIX.concat(Text.of(WARNING_COLOR, fileManager.getName(), RESET_COLOR, " hasn't been saved! An error happened.")));
            e.printStackTrace();
        }
    }

}
