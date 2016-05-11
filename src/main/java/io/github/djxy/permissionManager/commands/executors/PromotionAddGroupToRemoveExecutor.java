package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.promotions.Promotion;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-14.
 */
public class PromotionAddGroupToRemoveExecutor extends CommandExecutor {

    public PromotionAddGroupToRemoveExecutor() {
        setPermission(Permissions.PROMOTION_ADD_GROUP);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) throws CommandException {
        Promotion promotion = (Promotion) values.get("promotion");
        Group group = (Group) values.get("group");

        promotion.addGroupToRemove(group);
        source.sendMessage(Main.getTranslatorInstance().translate(source, "promotionAddGroupToRemove", CoreUtil.createMap("group", group.getIdentifier(), "promotion", promotion.getName())));
    }
}