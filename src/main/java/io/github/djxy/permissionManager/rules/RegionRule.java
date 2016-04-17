package io.github.djxy.permissionManager.rules;

import io.github.djxy.permissionManager.region.RegionService;
import io.github.djxy.permissionManager.subjects.Permission;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Map;

/**
 * Created by Samuel on 2016-03-27.
 */
public class RegionRule implements Rule {

    public static final String RULE_NAME = "region";

    private String world;
    private String location;

    public RegionRule(String world, String location) {
        this.world = world;
        this.location = location;
    }

    public RegionRule() {
    }

    @Override
    public boolean canApplyRule(Player player) {
        return player.getWorld().getName().equals(world) && RegionService.getInstance().isPlayerInRegion(player, player.getWorld(), location);
    }

    @Override
    public void applyRule(Player player) {}

    @Override
    public void initFromNode(ConfigurationNode node) {
        world = node.getNode("world").getString("");
        location = node.getNode("location").getString("");
    }

    @Override
    public void setNode(ConfigurationNode node) {
        node.getNode("world").setValue(world);
        node.getNode("location").setValue(location);
    }

    public static class Executor extends RuleExecutor {

        @Override
        public void execute(CommandSource source, Map<String, Object> values, Permission permission) {
            String worldName =((World) values.get("worldRule")).getName();
            String location = (String) values.get("location");

            permission.addRule(new RegionRule(worldName, location));
        }

    }

}
