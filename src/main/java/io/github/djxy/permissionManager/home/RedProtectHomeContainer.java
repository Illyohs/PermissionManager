package io.github.djxy.permissionManager.home;

import br.net.fabiozumbi12.redprotect.API.RedProtectAPI;
import br.net.fabiozumbi12.redprotect.Region;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

/**
 * Created by Samuel on 2016-03-29.
 */
public class RedProtectHomeContainer implements HomeContainer {

    @Override
    public boolean isPlayerIn(Player player, World world) {
        Region r = RedProtectAPI.getRegion(player.getLocation());

        return r != null && r.getOwners().contains(player.getUniqueId().toString());
    }

}
