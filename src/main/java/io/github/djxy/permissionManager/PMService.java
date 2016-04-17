package io.github.djxy.permissionManager;

import io.github.djxy.permissionManager.pmSubjects.PMCustomSubjectCollection;
import io.github.djxy.permissionManager.pmSubjects.PMGroupSubjectCollection;
import io.github.djxy.permissionManager.pmSubjects.PMPlayerSubjectCollection;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.permission.*;

import java.util.*;

/**
 * Created by Samuel on 2016-03-28.
 */
public class PMService implements PermissionService {

    private final static PMService instance = new PMService();

    public static PMService getInstance() {
        return instance;
    }

    private final HashMap<String,SubjectCollection> collections;

    private PMService(){
        this.collections = new HashMap<>();
        this.collections.put(PermissionService.SUBJECTS_COMMAND_BLOCK, new PMCustomSubjectCollection(PermissionService.SUBJECTS_COMMAND_BLOCK));
        this.collections.put(PermissionService.SUBJECTS_ROLE_TEMPLATE, new PMCustomSubjectCollection(PermissionService.SUBJECTS_ROLE_TEMPLATE));
        this.collections.put(PermissionService.SUBJECTS_SYSTEM, new PMCustomSubjectCollection(PermissionService.SUBJECTS_SYSTEM));
        this.collections.put(PermissionService.SUBJECTS_GROUP, new PMGroupSubjectCollection());
        this.collections.put(PermissionService.SUBJECTS_USER, new PMPlayerSubjectCollection());
    }

    @Override
    public SubjectCollection getUserSubjects() {
        return this.collections.get(PermissionService.SUBJECTS_USER);
    }

    @Override
    public SubjectCollection getGroupSubjects() {
        return this.collections.get(PermissionService.SUBJECTS_GROUP);
    }

    @Override
    public SubjectData getDefaultData() {
        return this.collections.get(PermissionService.SUBJECTS_GROUP).get(PermissionManager.getInstance().getDefaultGroup().getIdentifier()).getSubjectData();
    }

    @Override
    public SubjectCollection getSubjects(String s) {
        return this.collections.get(s);
    }

    @Override
    public Map<String, SubjectCollection> getKnownSubjects() {
        return new HashMap<>(collections);
    }

    @Override
    public Optional<PermissionDescription.Builder> newDescriptionBuilder(Object o) {
        return Optional.empty();
    }

    @Override
    public Optional<PermissionDescription> getDescription(String s) {
        return Optional.empty();
    }

    @Override
    public Collection<PermissionDescription> getDescriptions() {
        return new ArrayList<>();
    }

    @Override
    public void registerContextCalculator(ContextCalculator<Subject> contextCalculator) {

    }

}
