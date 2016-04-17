package io.github.djxy.permissionManager.commands.executors;

import io.github.djxy.permissionManager.Permissions;
import io.github.djxy.permissionManager.commands.CommandExecutor;
import io.github.djxy.permissionManager.subjects.Group;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

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

        source.sendMessage(PREFIX.concat(Text.of("The rank of ", INFO_COLOR, subject.getIdentifier(), RESET_COLOR, " is now ", INFO_COLOR, rank, RESET_COLOR, ".")));

        subject.setRank(rank);
    }

}
