package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.promotions.Promotion;
import io.github.djxy.permissionManager.promotions.PromotionManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-14.
 */
public class CreatePromotionExecutor extends CommandExecutor {

    public CreatePromotionExecutor() {
        setPermission(Permissions.CREATE_PROMOTION);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) throws CommandException {
        String promotion = (String) values.get("promotion");

        if(!PromotionManager.getInstance().hasPromotion(promotion)) {
            PromotionManager.getInstance().setPromotion(promotion, new Promotion());
            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, promotion, RESET_COLOR, " has been created.")));
        }
        else
            source.sendMessage(PREFIX.concat(Text.of(INFO_COLOR, promotion, RESET_COLOR, " already exist.")));
    }

}
