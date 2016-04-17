package io.github.djxy.permissionManager.pmSubjects;

import io.github.djxy.permissionManager.PermissionManager;

/**
 * Created by Samuel on 2016-04-01.
 */
public class PMCustomSubjectCollection extends PMSubjectCollection {

    public PMCustomSubjectCollection(String identifier) {
        super(identifier);
    }

    @Override
    public PMSubject getPMSubject(String identifier) {
        return new PMCustomSubject(PermissionManager.getInstance().getOrCreateCustomSubject(identifier), this);
    }

    @Override
    public boolean hasRegistered(String s) {
        return PermissionManager.getInstance().hasCustomSubject(s);
    }

}
