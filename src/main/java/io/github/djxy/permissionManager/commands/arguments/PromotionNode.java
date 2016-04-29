package io.github.djxy.permissionManager.commands.arguments;

import io.github.djxy.core.commands.nodes.ArgumentNode;
import io.github.djxy.permissionManager.promotions.PromotionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-04-14.
 */
public class PromotionNode extends ArgumentNode {

    public PromotionNode(String alias, String name) {
        super(alias, name);
    }

    public PromotionNode(String alias) {
        super(alias);
    }

    @Override
    public Object getValue(String arg) {
        return PromotionManager.getInstance().getPromotion(arg);
    }

    @Override
    protected List<String> complete(String complete) {
        List<String> values = new ArrayList<>();

        for(String value : PromotionManager.getInstance().getPromotions())
            if(value.toLowerCase().startsWith(complete))
                values.add(value);

        return values;
    }

}
