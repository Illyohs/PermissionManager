package io.github.djxy.permissionManager.region;

import net.foxdenstudio.sponge.foxguard.plugin.FGManager;
import net.foxdenstudio.sponge.foxguard.plugin.region.IRegion;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

/**
 * Created by Samuel on 2016-03-29.
 */
public class FoxGuardRegionContainer implements RegionContainer {

    @Override
    public boolean isPlayerIn(Player player, World world, String region) {
        IRegion reg = FGManager.getInstance().getRegion(world, region);

        if(reg != null)
            return reg.isInRegion(player.getLocation().getBlockPosition());
        else
            return false;
    }

}
