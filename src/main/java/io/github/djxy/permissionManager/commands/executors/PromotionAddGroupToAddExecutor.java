package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.promotions.Promotion;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-14.
 */
public class PromotionAddGroupToAddExecutor extends CommandExecutor{

    public PromotionAddGroupToAddExecutor() {
        setPermission(Permissions.PROMOTION_ADD_GROUP);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) throws CommandException {
        Promotion promotion = (Promotion) values.get("promotion");
        Group group = (Group) values.get("group");

        source.sendMessage(PREFIX.concat(Text.of("You added the group ", INFO_COLOR, group.getIdentifier(), RESET_COLOR, " to add to the promotion ", INFO_COLOR, promotion.getName(), RESET_COLOR, ".")));
        promotion.addGroupToAdd(group);
    }
}
