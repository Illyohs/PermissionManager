package io.github.djxy.permissionManager.pmSubjects;

import io.github.djxy.permissionManager.subjects.CustomSubject;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.util.Tristate;

import java.util.Set;

/**
 * Created by Samuel on 2016-04-01.
 */
public class PMCustomSubject extends PMSubject {

    public PMCustomSubject(CustomSubject subject, SubjectCollection collection) {
        super(subject, collection);
    }

    @Override
    public boolean hasPermission(Set<Context> set, String s) {
        return true;
    }

    @Override
    public Tristate getPermissionValue(Set<Context> set, String s) {
        return Tristate.TRUE;
    }

}
