package io.github.djxy.permissionManager.home;

import com.arckenver.nations.DataHandler;
import com.arckenver.nations.object.Nation;
import com.arckenver.nations.object.Zone;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Samuel on 2016-05-22.
 */
public class NationHomeContainer implements HomeContainer {

    @Override
    public boolean isPlayerIn(Player player) {
        Nation nation = DataHandler.getNation(player.getLocation());

        if(nation != null) {
            Zone zone = nation.getZone(player.getLocation());

            return zone != null && (zone.isOwner(player.getUniqueId()) || zone.isCoowner(player.getUniqueId()));
        }
        else
            return false;
    }

}
