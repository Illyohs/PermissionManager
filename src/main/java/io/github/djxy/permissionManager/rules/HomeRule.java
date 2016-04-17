package io.github.djxy.permissionManager.rules;

import io.github.djxy.permissionManager.home.HomeService;
import io.github.djxy.permissionManager.subjects.Permission;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-15.
 */
public class HomeRule implements Rule {

    public static final String RULE_NAME = "home";

    private String world = null;

    public HomeRule(String world) {
        this.world = world;
    }

    public HomeRule() {
    }

    @Override
    public boolean canApplyRule(Player player) {
        return (world == null || player.getWorld().getName().equals(world)) && HomeService.getInstance().isPlayerInHome(player, player.getWorld());
    }

    @Override
    public void applyRule(Player player) {}

    @Override
    public void initFromNode(ConfigurationNode node) {
        String world = node.getNode("world").getString("*");

        if(!world.equals("*"))
            this.world = world;
    }

    @Override
    public void setNode(ConfigurationNode node) {
        node.getNode("world").setValue(world == null?"*":world);
    }

    public static class ExecutorWithoutWorld extends RuleExecutor {

        @Override
        public void execute(CommandSource source, Map<String, Object> values, Permission permission) {
            permission.addRule(new HomeRule());
        }

    }

    public static class ExecutorWithWorld extends RuleExecutor {

        @Override
        public void execute(CommandSource source, Map<String, Object> values, Permission permission) {
            String world = ((World) values.get("worldRule")).getName();

            permission.addRule(new HomeRule(world));
        }

    }

}
