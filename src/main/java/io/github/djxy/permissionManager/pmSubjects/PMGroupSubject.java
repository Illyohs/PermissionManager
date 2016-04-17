package io.github.djxy.permissionManager.pmSubjects;

import io.github.djxy.permissionManager.subjects.Group;
import io.github.djxy.permissionManager.subjects.Permission;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.util.Tristate;

import java.util.Set;

/**
 * Created by Samuel on 2016-03-28.
 */
public class PMGroupSubject extends PMSubject {

    public PMGroupSubject(Group subject, SubjectCollection collection) {
        super(subject, collection);
    }

    public Group getGroup(){
        return (Group) subject;
    }

    @Override
    public boolean hasPermission(Set<Context> set, String s) {
        if(Util.isWorldContext(set))
            return subject.hasPermission(Util.getWorldFromContext(set), s);
        else
            return false;
    }

    @Override
    public Tristate getPermissionValue(Set<Context> set, String s) {
        if(Util.isWorldContext(set))
            return Tristate.fromBoolean(getPermissionValue(Util.getWorldFromContext(set), s));
        else
            return Tristate.UNDEFINED;
    }

    private boolean getPermissionValue(String world, String permission){
        Permission perm = subject.getPermissionValue(world, permission);

        if(perm != null)
            return perm.getValue();
        else
            return false;
    }

}
