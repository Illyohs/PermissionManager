package io.github.djxy.permissionManager.rules;

import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Subject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-14.
 */
public abstract class RuleExecutor extends CommandExecutor {

    abstract public void execute(CommandSource source, Map<String, Object> values, Permission permission);

    @Override
    public void execute(CommandSource source, Map<String, Object> values) throws CommandException {
        Permission permission;
        Subject subject = (Subject) values.get("subject");
        String perm = (String) values.get("permission");

        if(values.containsKey("world")){
            String worldName =((World) values.get("world")).getName();

            permission = subject.getPermission(worldName, perm);
        }else
            permission = subject.getPermission(perm);

        if(permission != null)
            execute(source, values, permission);
        else
            throw new CommandException(Text.of("ERROR!!!"));
    }

}
