package io.github.djxy.permissionManager.rules;

import com.arckenver.nations.DataHandler;
import com.arckenver.nations.object.Nation;
import com.arckenver.nations.service.NationsService;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-05-19.
 */
public class NationRule implements Rule {

    public static final String RULE_NAME = "nation";

    enum Location {OWN_NATION, OTHER_NATION, WILDERNESS}

    private final List<Location> locations = new ArrayList<>();

    @Override
    public boolean canApplyRule(Player player) {
        if(Sponge.getServiceManager().provide(NationsService.class).isPresent()){
            Nation nation = DataHandler.getNation(player.getLocation());
            boolean insideNation = nation != null;

            if(insideNation){
                boolean isMember = nation.isPresident(player.getUniqueId()) || nation.isMinister(player.getUniqueId()) || nation.isCitizen(player.getUniqueId());

                if(isMember)
                    return locations.contains(Location.OWN_NATION);
                else
                    return locations.contains(Location.OTHER_NATION);
            }
            else
                return locations.contains(Location.WILDERNESS);
        }
        else
            return false;
    }

    @Override
    public void applyRule(Player player) {}

    @Override
    public void initFromNode(ConfigurationNode configurationNode) {
        List<ConfigurationNode> values = (List<ConfigurationNode>) configurationNode.getChildrenList();

        for(ConfigurationNode node : values){
            try{
                locations.add(Location.valueOf(node.getString()));
            }catch (Exception e){}
        }
    }

    @Override
    public void setNode(ConfigurationNode configurationNode) {
        List<String> values = new ArrayList<>();

        for(Location location : locations)
            values.add(location.name());

        configurationNode.setValue(values);
    }

}
