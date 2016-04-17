package io.github.djxy.permissionManager.listeners;

import io.github.djxy.permissionManager.subjects.Permission;
import io.github.djxy.permissionManager.subjects.Subject;

/**
 * Created by Samuel on 2016-03-28.
 */
public interface SubjectListener {

    public void onPermissionSet(Subject subject, Permission permission);

    public void onPermissionSet(Subject subject, String world, Permission permission);

    public void onPermissionRemove(Subject subject, Permission permission);

    public void onPermissionRemove(Subject subject, String world, Permission permission);

}
