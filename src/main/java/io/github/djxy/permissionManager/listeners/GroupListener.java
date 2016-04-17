package io.github.djxy.permissionManager.listeners;

import io.github.djxy.permissionManager.subjects.Group;

/**
 * Created by Samuel on 2016-04-03.
 */
public interface GroupListener extends SubjectListener {

    public void onRankChange(Group group);

    public void onDelete(Group group);

    public void onIdentifierChange(Group group, String lastIdentifier);

}
