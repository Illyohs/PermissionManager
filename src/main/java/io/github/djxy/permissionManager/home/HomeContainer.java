package io.github.djxy.permissionManager.home;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

/**
 * Created by Samuel on 2016-04-15.
 */
public interface HomeContainer {

    public boolean isPlayerIn(Player player, World world);

}
