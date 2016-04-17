package io.github.djxy.permissionManager.rules;

import io.github.djxy.permissionManager.files.ObjectSerializer;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Samuel on 2016-03-27.
 */
public interface Rule extends ObjectSerializer {

    boolean canApplyRule(Player player);

    void applyRule(Player player);

}
