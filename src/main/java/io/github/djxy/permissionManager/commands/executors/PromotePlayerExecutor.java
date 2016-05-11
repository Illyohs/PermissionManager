package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.promotions.Promotion;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-15.
 */
public class PromotePlayerExecutor extends CommandExecutor {

    public PromotePlayerExecutor() {
        setPermission(Permissions.PROMOTE_PLAYER);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) throws CommandException {
        Promotion promotion = (Promotion) values.get("promotion");
        Player player = (Player) values.get("player");
        String name = PlayerRepository.getInstance().getPlayerName(player.getUUID());

        promotion.apply(player);
        source.sendMessage(Main.getTranslatorInstance().translate(source, "promotePlayer", CoreUtil.createMap("player", name, "promotion", promotion.getName())));
    }

}
