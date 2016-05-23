package io.github.djxy.permissionManager.rules;

import io.github.djxy.permissionManager.home.HomeService;
import io.github.djxy.permissionManager.subjects.Permission;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Samuel on 2016-04-15.
 */
public class HomeRule implements Rule {

    public static final String RULE_NAME = "home";

    private List<String> worlds = null;

    public HomeRule(String world) {
        this.worlds = Arrays.asList(world);
    }

    public HomeRule() {
    }

    @Override
    public boolean canApplyRule(Player player) {
        return (worlds == null || worlds.contains(player.getWorld().getName())) && HomeService.getInstance().isPlayerInHome(player);
    }

    @Override
    public void applyRule(Player player) {}

    @Override
    public void initFromNode(ConfigurationNode node) {
        if(node.getNode("world").hasListChildren()){
            List<ConfigurationNode> worlds = (List<ConfigurationNode>) node.getNode("world").getChildrenList();
            this.worlds = new ArrayList<>();

            for(ConfigurationNode world : worlds)
                this.worlds.add(world.getString());
        }
        else {
            String world = node.getNode("world").getString("*");

            if(!world.equals("*"))
                this.worlds = Arrays.asList(world);
        }
    }

    @Override
    public void setNode(ConfigurationNode node) {
        node.getNode("world").setValue(worlds == null?"*": worlds);
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
