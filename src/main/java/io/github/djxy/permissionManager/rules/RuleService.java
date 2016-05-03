package io.github.djxy.permissionManager.rules;

import io.github.djxy.core.commands.nodes.ChoiceNode;
import io.github.djxy.core.commands.nodes.Node;
import io.github.djxy.core.commands.nodes.arguments.DoubleNode;
import io.github.djxy.core.commands.nodes.arguments.LongNode;
import io.github.djxy.core.commands.nodes.arguments.StringNode;
import io.github.djxy.core.commands.nodes.arguments.WorldNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samuel on 2016-04-03.
 */
public class RuleService {

    private static final RuleService instance = new RuleService();

    public static RuleService getInstance() {
        return instance;
    }

    private final HashMap<String,Class<? extends Rule>> ruleClassesByName;
    private final HashMap<String,Node> ruleNodes;
    private final HashMap<Class<? extends Rule>,String> ruleClassesByClass;

    private RuleService() {
        this.ruleClassesByName = new HashMap<>();
        this.ruleNodes = new HashMap<>();
        this.ruleClassesByClass = new HashMap<>();
        addRule(EconomyRule.RULE_NAME, EconomyRule.class, new DoubleNode("cost").setExecutor(new EconomyRule.Executor()));
        addRule(CooldownRule.RULE_NAME, CooldownRule.class, new LongNode("cooldown").setExecutor(new CooldownRule.Executor()));
        addRule(RegionRule.RULE_NAME, RegionRule.class, new WorldNode("world", "worldRule").addNode(new StringNode("location").setExecutor(new RegionRule.Executor())));
        addRule(HomeRule.RULE_NAME, HomeRule.class, new ChoiceNode("global").setExecutor(new HomeRule.ExecutorWithoutWorld()), new WorldNode("world", "worldRule").setExecutor(new HomeRule.ExecutorWithWorld()));
        addRule(TimeRule.RULE_NAME, TimeRule.class);
    }

    public List<String> getRulesName() {
        return new ArrayList<>(ruleClassesByName.keySet());
    }

    public String getRuleName(Class<? extends Rule> c){
        return ruleClassesByClass.get(c);
    }

    public Node getRuleNode(String name){
        return ruleNodes.get(name);
    }

    public void addRule(String name, Class<? extends Rule> c, Node... nodes){
        ruleClassesByName.put(name, c);
        ruleClassesByClass.put(c, name);

        ChoiceNode choiceNode = new ChoiceNode("");

        for(Node node : nodes)
            choiceNode.addNode(node);

        ruleNodes.put(name, choiceNode);
    }

    public Rule createRule(String name) {
        if(ruleClassesByName.containsKey(name))
            try {
                return ruleClassesByName.get(name).getConstructor().newInstance();
            } catch (Exception e) {}

        return null;
    }


}
