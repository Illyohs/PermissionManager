package io.github.djxy.permissionManager.home;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-04-15.
 */
public class HomeService {

    private final static HomeService service = new HomeService();

    public static HomeService getInstance() {
        return service;
    }

    private final CopyOnWriteArrayList<HomeContainer> homeContainers = new CopyOnWriteArrayList<>();

    private HomeService() {
    }

    public void addHomeContainer(HomeContainer homeContainer){
        homeContainers.add(homeContainer);
    }

    public void removeHomeContainer(HomeContainer homeContainer){
        homeContainers.remove(homeContainer);
    }

    public boolean isPlayerInHome(Player player, World world){
        for(HomeContainer homeContainer : homeContainers)
            if(homeContainer.isPlayerIn(player, world))
                return true;

        return false;
    }

}
