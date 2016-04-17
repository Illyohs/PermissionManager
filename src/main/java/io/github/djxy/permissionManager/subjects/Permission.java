package io.github.djxy.permissionManager.subjects;

import io.github.djxy.permissionManager.rules.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-03-27.
 */
public class Permission {

    private final String permission;
    private boolean value;
    private final ConcurrentHashMap<Class<? extends Rule>, Rule> rules;

    public Permission(String permission, boolean value) {
        this.permission = permission.toLowerCase();
        this.value = value;
        this.rules = new ConcurrentHashMap<>();
    }

    public String getPermission() {
        return permission;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public List<Rule> getRules() {
        return new ArrayList<>(rules.values());
    }

    public void addRule(Rule rule){
        rules.put(rule.getClass(), rule);
    }

    public Rule getRule(Class<? extends Rule> clazz){
        return rules.get(clazz);
    }

    public boolean getValue(org.spongepowered.api.entity.living.player.Player player) {
        if(rules.isEmpty())
            return value;
        else{
            boolean canApplyRules = true;
            Iterator<Rule> rules = this.rules.values().iterator();

            while(rules.hasNext() && canApplyRules)
                canApplyRules = rules.next().canApplyRule(player);

            if(canApplyRules){
                Collection<Rule> r = this.rules.values();

                for(Rule rule : r)
                    rule.applyRule(player);

                return value;
            }
            else
                return false;
        }
    }
}
