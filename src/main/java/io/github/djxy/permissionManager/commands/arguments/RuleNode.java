package io.github.djxy.permissionManager.commands.arguments;

import io.github.djxy.core.commands.nodes.Node;
import io.github.djxy.permissionManager.rules.RuleService;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-04-10.
 */
public class RuleNode extends Node {

    public RuleNode(String alias) {
        super(alias);
    }

    @Override
    public Node addNode(Node node) {
        return this;
    }

    @Override
    public Node getNode(String node) {
        return this;
    }

    @Override
    protected List<String> complete(String complete) {
        List<String> values = new ArrayList<>();

        for(String rule : RuleService.getInstance().getRulesName())
            if(rule.toLowerCase().startsWith(complete))
                values.add(rule);

        return values;
    }

    @Override
    public List<String> getSuggestion(String[] args, int index){
        if(index+1 < args.length){
            RuleService rs = RuleService.getInstance();
            Node next = rs.getRuleNode(args[index]);

            if(next != null)
                return next.getSuggestion(args, index + 1);
            else
                return EMPTY_LIST;
        }
        else
            return complete(args[index].toLowerCase());
    }

    @Override
    public void createCommandCalled(CommandCalled commandCalled, CommandSource source, String[] args, int index) throws CommandException {
        if(index+1 < args.length){
            RuleService rs = RuleService.getInstance();
            Node next = rs.getRuleNode(args[index]);

            if(next != null)
                next.createCommandCalled(commandCalled, source, args, index + 1);
        }
        else
            commandCalled.setExecutor(getExecutor());
    }

}
