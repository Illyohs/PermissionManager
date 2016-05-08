package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.promotions.Promotion;
import io.github.djxy.permissionManager.promotions.PromotionManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-14.
 */
public class PromotionSetNameExecutor extends CommandExecutor {

    public PromotionSetNameExecutor() {
        setPermission(Permissions.PROMOTION_SET_NAME);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) throws CommandException {
        Promotion promotion = (Promotion) values.get("promotion");
        String name = (String) values.get("name");

        source.sendMessage(Main.getTranslatorInstance().translate(source, "setPromotionName", CoreUtil.createMap("newName", name, "promotion", promotion.getName())));
        PromotionManager.getInstance().setPromotionName(promotion.getName(), name);
    }

}