package io.github.djxy.permissionManager.pmSubjects;

import io.github.djxy.permissionManager.PermissionManager;
import org.spongepowered.api.service.permission.PermissionService;

/**
 * Created by Samuel on 2016-03-28.
 */
public class PMGroupSubjectCollection extends PMSubjectCollection {

    public PMGroupSubjectCollection() {
        super(PermissionService.SUBJECTS_GROUP);
    }

    @Override
    public PMSubject getPMSubject(String identifier) {
        return PermissionManager.getInstance().hasGroup(identifier)?new PMGroupSubject(PermissionManager.getInstance().getGroup(identifier), this):null;
    }

    @Override
    public boolean hasRegistered(String s) {
        return PermissionManager.getInstance().hasGroup(s);
    }

}
