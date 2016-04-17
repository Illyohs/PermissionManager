package io.github.djxy.permissionManager.rules;

import io.github.djxy.permissionManager.subjects.Permission;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-03-27.
 */
public class CooldownRule implements Rule {

    public static final String RULE_NAME = "cooldown";

    private final ConcurrentHashMap<UUID, Long> cooldowns;
    private long cooldown;

    public CooldownRule(long cooldown) {
        this.cooldowns = new ConcurrentHashMap<>();
        this.cooldown = cooldown;
    }

    public CooldownRule() {
        cooldowns = new ConcurrentHashMap<>();
    }

    @Override
    public boolean canApplyRule(Player player) {
        if(cooldowns.containsKey(player.getUniqueId()))
            return cooldowns.get(player.getUniqueId()) + cooldown <= System.currentTimeMillis();
        else
            return true;
    }

    @Override
    public void applyRule(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @Override
    public void initFromNode(ConfigurationNode node) {
        cooldown = node.getNode("cooldown").getLong(0);
    }

    @Override
    public void setNode(ConfigurationNode node) {
        node.getNode("cooldown").setValue(cooldown);
    }

    public static class Executor extends RuleExecutor {

        @Override
        public void execute(CommandSource source, Map<String, Object> values, Permission permission) {
            long cooldown = (long) values.get("cooldown");

            permission.addRule(new CooldownRule(cooldown));
        }

    }

}
