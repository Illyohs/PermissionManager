package io.github.djxy.permissionManager.pmSubjects;

import io.github.djxy.permissionManager.PermissionManager;
import org.spongepowered.api.service.permission.PermissionService;

import java.util.UUID;

/**
 * Created by Samuel on 2016-03-28.
 */
public class PMPlayerSubjectCollection extends PMSubjectCollection {

    public PMPlayerSubjectCollection() {
        super(PermissionService.SUBJECTS_USER);
    }

    @Override
    public PMSubject getPMSubject(String identifier) {
        UUID uuid = UUID.fromString(identifier);

        return PermissionManager.getInstance().hasPlayer(uuid)?new PMPlayerSubject(PermissionManager.getInstance().getPlayer(uuid), this):null;
    }

    @Override
    public boolean hasRegistered(String s) {
        return PermissionManager.getInstance().hasPlayer(UUID.fromString(s));
    }

}
