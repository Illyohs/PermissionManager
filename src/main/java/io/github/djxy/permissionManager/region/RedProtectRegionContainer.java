package io.github.djxy.permissionManager.region;

import br.net.fabiozumbi12.redprotect.API.RedProtectAPI;
import br.net.fabiozumbi12.redprotect.Region;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

/**
 * Created by Samuel on 2016-03-29.
 */
public class RedProtectRegionContainer implements RegionContainer {

    @Override
    public boolean isPlayerIn(Player player, World world, String region) {
        Region r = RedProtectAPI.getRegion(player.getLocation());

        return r != null && r.getName().equals(region);
    }

}
