package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.promotions.Promotion;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-14.
 */
public class PromotionRemoveGroupToRemoveExecutor extends CommandExecutor{

    public PromotionRemoveGroupToRemoveExecutor() {
        setPermission(Permissions.PROMOTION_REMOVE_GROUP);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) throws CommandException {
        Promotion promotion = (Promotion) values.get("promotion");
        Group group = (Group) values.get("group");

        promotion.removeGroupToRemove(group);
        source.sendMessage(Main.getTranslatorInstance().translate(source, "promotionRemoveGroupToRemove", CoreUtil.createMap("group", group.getIdentifier(), "promotion", promotion.getName())));
    }

}