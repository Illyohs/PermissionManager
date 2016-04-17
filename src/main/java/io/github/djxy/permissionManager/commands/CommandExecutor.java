package io.github.djxy.permissionManager.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-05.
 */
public abstract class CommandExecutor {

    public static final Text PREFIX = TextSerializers.FORMATTING_CODE.deserialize("&f[&6Permission&l&4M&r&f] ");
    public static final Text ACTION_NO_LONGER_POSSIBLE = Text.of("This action is no longer possible. You have to call the command again.");
    public static final TextColor INFO_COLOR = TextColors.YELLOW;
    public static final TextColor WARNING_COLOR = TextColors.RED;
    public static final TextColor RESET_COLOR = TextColors.RESET;
    public static final TextStyle RESET_STYLE = TextStyles.RESET;

    private String permission;

    abstract public void execute(CommandSource source, Map<String,Object> values) throws CommandException ;

    public String getPermission() {
        return permission;
    }

    public CommandExecutor setPermission(String permission) {
        this.permission = permission;
        return this;
    }

}
