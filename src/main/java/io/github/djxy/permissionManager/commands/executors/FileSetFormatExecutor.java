package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.files.FileFormat;
import io.github.djxy.permissionManager.files.FileManager;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-09.
 */
public class FileSetFormatExecutor extends CommandExecutor {

    public FileSetFormatExecutor() {
        setPermission(Permissions.FILE_SET_FORMAT);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        FileManager fileManager = (FileManager) values.get("file");

        FileFormat format = (FileFormat) values.get("format");
        source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, fileManager.getName(), RESET_COLOR, " file type has been changed to ", INFO_COLOR, format.name(), RESET_COLOR, ".")));
        fileManager.setFormat(format);
    }

}
