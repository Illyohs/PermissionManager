package io.github.djxy.permissionManager.pmSubjects;

import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-03-28.
 */
public abstract class PMSubjectCollection implements SubjectCollection {

    private final String identifier;
    private final ConcurrentHashMap<String,PMSubject> subjects = new ConcurrentHashMap<>();

    abstract public PMSubject getPMSubject(String identifier);

    public PMSubjectCollection(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Subject get(String s) {
        if(!subjects.containsKey(s)) {
            PMSubject subject = getPMSubject(s);

            if(subject != null) {
                subjects.put(s, subject);

                return subject;
            }
            else
                return null;
        }

        return subjects.get(s);
    }

    @Override
    public Iterable<Subject> getAllSubjects() {
        return new ArrayList<>(subjects.values());
    }

    @Override
    public Map<Subject, Boolean> getAllWithPermission(String s) {
        return null;
    }

    @Override
    public Map<Subject, Boolean> getAllWithPermission(Set<Context> set, String s) {
        return null;
    }

}
