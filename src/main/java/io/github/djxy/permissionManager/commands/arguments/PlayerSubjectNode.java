package io.github.djxy.permissionManager.commands.arguments;

import io.github.djxy.core.commands.nodes.ArgumentNode;
import io.github.djxy.core.repositories.PlayerRepository;
import io.github.djxy.permissionManager.PermissionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2016-04-09.
 */
public class PlayerSubjectNode extends ArgumentNode {

    public PlayerSubjectNode(String alias, String name) {
        super(alias, name);
    }

    public PlayerSubjectNode(String alias) {
        super(alias, alias);
    }

    @Override
    protected List<String> complete(String complete) {
        List<String> values = new ArrayList<>();

        for(String value : PlayerRepository.getInstance().getPlayersName())
            if(value.toLowerCase().startsWith(complete))
                values.add(value);

        return values;
    }

    @Override
    public Object getValue(String arg) {
        return PermissionManager.getInstance().getPlayer(PlayerRepository.getInstance().getPlayerUUID(arg));
    }

}
