package io.github.djxy.permissionManager.region;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

/**
 * Created by Samuel on 2016-03-29.
 */
public interface RegionContainer {

    public boolean isPlayerIn(Player player, World world, String region);

}
