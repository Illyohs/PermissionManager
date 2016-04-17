package io.github.djxy.permissionManager.commands.nodes.arguments;

import io.github.djxy.permissionManager.commands.nodes.ArgumentNode;

import java.util.List;

/**
 * Created by Samuel on 2016-04-10.
 */
public class FloatNode extends ArgumentNode {

    public FloatNode(String alias, String name) {
        super(alias, name);
    }

    public FloatNode(String alias) {
        super(alias);
    }

    @Override
    public Object getValue(String arg) {
        try{
            return Float.parseFloat(arg);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected List<String> complete(String complete) {
        return EMPTY;
    }

}
