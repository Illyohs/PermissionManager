package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.CommandExecutor;
import io.github.djxy.permissionManager.Main;
import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandSource;

import java.util.Map;

/**
 * Created by Samuel on 2016-04-10.
 */
public class GroupSetRankExecutor extends CommandExecutor {

    public GroupSetRankExecutor() {
        setPermission(Permissions.GROUP_SET_RANK);
    }

    @Override
    public void execute(CommandSource source, Map<String, Object> values) {
        Group subject = (Group) values.get("subject");
        int rank = (int) values.get("rank");

        source.sendMessage(Main.getTranslatorInstance().translate(source, "setGroupRank", CoreUtil.createMap("group", subject.getIdentifier(), "rank", rank)));

        subject.setRank(rank);
    }

}
