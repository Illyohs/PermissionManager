package io.github.djxy.permissionManager.rules;

import io.github.djxy.permissionManager.subjects.Permission;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Samuel on 2016-03-27.
 */
public class EconomyRule implements Rule {

    public static final String RULE_NAME = "economy";

    private double cost;

    public EconomyRule(double cost) {
        this.cost = cost;
    }

    public EconomyRule() {
    }

    @Override
    public boolean canApplyRule(Player player) {
        Optional<EconomyService> service = Sponge.getServiceManager().provide(EconomyService.class);

        if(service.isPresent()){
            EconomyService economyService = service.get();
            Account account = economyService.getOrCreateAccount(player.getUniqueId()).get();

            return account.getBalance(economyService.getDefaultCurrency()).doubleValue() >= cost;
        }
        else
            return true;
    }

    @Override
    public void applyRule(Player player) {
        Optional<EconomyService> service = Sponge.getServiceManager().provide(EconomyService.class);

        if(service.isPresent()){
            EconomyService economyService = service.get();
            Account account = economyService.getOrCreateAccount(player.getUniqueId()).get();

            account.withdraw(economyService.getDefaultCurrency(), BigDecimal.valueOf(cost), Cause.of(NamedCause.source(this)));
        }
    }

    @Override
    public void initFromNode(ConfigurationNode node) {
        cost = node.getNode("cost").getDouble(0.0);
    }

    @Override
    public void setNode(ConfigurationNode node) {
        node.getNode("cost").setValue(cost);
    }

    public static class Executor extends RuleExecutor{

        @Override
        public void execute(CommandSource source, Map<String, Object> values, Permission permission) {
            double cost = (double) values.get("cost");

            permission.addRule(new EconomyRule(cost));
        }

    }
}
