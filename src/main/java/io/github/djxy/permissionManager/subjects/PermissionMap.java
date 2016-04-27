package io.github.djxy.permissionManager.subjects;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-03-23.
 */
public class PermissionMap implements Map<String, Permission> {

    private final ConcurrentHashMap<String,Permission> permissions;

    public PermissionMap() {
        this.permissions = new ConcurrentHashMap<>();
    }

    private PermissionMap(ConcurrentHashMap<String, Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public int size() {
        return permissions.size();
    }

    @Override
    public boolean isEmpty() {
        return permissions.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return permissions.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return permissions.containsValue(value);
    }

    /**
     * Check to get the controller of a permission. permissionmanager.* is the controller of all the permissions of permissionmanager.
     * @param permission
     * @return
     */
    public Permission getPermissionControllerOf(String permission) {
        if(permissions.containsKey(permission))
            return permissions.get(permission);
        else if(permission.contains(".")){
            String permissions[] = new String[permission.length() - permission.replace(".", "").length() + 1];
            int lastIndex = 1;

            permissions[0] = "*";

            for(int i = 0; i < permission.length(); i++){
                if(permission.charAt(i) == '.')
                    permissions[lastIndex++] = permission.substring(0, i) + ".*";
            }

            for(int i = permissions.length-1; i >= 0; i--){
                Permission perm = this.permissions.get(permissions[i]);

                if(perm != null)
                    return perm;
            }
        }

        return null;
    }

    //Only good way to check the permissions
    @Override
    public Permission get(Object key) {
        if(permissions.containsKey(key))
            return permissions.get(key);

        return null;
    }

    @Override
    public Permission put(String key, Permission value) {
        return permissions.put(key, value);
    }

    @Override
    public Permission remove(Object key) {
        return permissions.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Permission> m) {
        permissions.putAll(m);
    }

    @Override
    public void clear() {
        permissions.clear();
    }

    @Override
    public Set<String> keySet() {
        return permissions.keySet();
    }

    @Override
    public Collection<Permission> values() {
        return permissions.values();
    }

    @Override
    public Set<Entry<String, Permission>> entrySet() {
        return permissions.entrySet();
    }

    public PermissionMap clone(){
        return new PermissionMap(new ConcurrentHashMap<>(permissions));
    }

}
