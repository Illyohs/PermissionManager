package io.github.djxy.permissionManager.region;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-03-29.
 */
public class RegionService {

    private final static RegionService service = new RegionService();

    public static RegionService getInstance() {
        return service;
    }

    private final CopyOnWriteArrayList<RegionContainer> regionContainers;

    private RegionService() {
        this.regionContainers = new CopyOnWriteArrayList<>();
    }

    public void addRegionContainer(RegionContainer regionContainer){
        regionContainers.add(regionContainer);
    }

    public void removeRegionContainer(RegionContainer regionContainer){
        regionContainers.remove(regionContainer);
    }

    public boolean isPlayerInRegion(Player player, World world, String region){
        for(RegionContainer regionContainer : regionContainers)
            if(regionContainer.isPlayerIn(player, world, region))
                return true;

        return false;
    }

}
