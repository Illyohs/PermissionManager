package io.github.djxy.permissionManager.repositories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Samuel on 2016-04-03.
 */
public class PermissionRepository {

    private static final PermissionRepository instance = new PermissionRepository();

    public static PermissionRepository getInstance() {
        return instance;
    }

    private final CopyOnWriteArraySet<String> permissions = new CopyOnWriteArraySet<>();
    private final PermissionNode permissionNode = new PermissionNode("");

    private PermissionRepository() {
    }

    public List<String> getPermissions() {
        return new ArrayList<>(permissions);
    }

    public void addPermission(String permission){
        permissions.add(permission.toLowerCase());
        permissionNode.addPermission(permission);
    }

    public List<String> getPermissionNodes(String permission){
        List<String> list = new ArrayList<>();
        Iterator<String> nodes = permissionNode.getPermissionNode(permission).getNodes().iterator();

        while(nodes.hasNext()){
            String node = nodes.next();

            if(node.toLowerCase().startsWith(permission.toLowerCase()))
                list.add(node);
        }

        return list;
    }

    private class PermissionNode {

        private final ConcurrentHashMap<String, PermissionNode> permissions;
        private final String permissionNode;

        public PermissionNode(String permissionNode){
            permissions = new ConcurrentHashMap<>();
            this.permissionNode = permissionNode;
        }

        public PermissionNode getPermissionNode(String permission){
            int index = permission.indexOf(".");

            if(index != -1){
                String perm = this.permissionNode+permission.substring(0, index+1);

                if(!permissions.containsKey(perm)) {
                    return this;
                }
                else
                    return permissions.get(perm).getPermissionNode(permission.substring(index+1));
            }
            else
                return this;
        }

        public void addPermission(String permission){
            int index = permission.indexOf(".");

            if(index != -1){
                String perm = this.permissionNode+permission.substring(0, index+1);

                if(!permissions.containsKey(perm)) {
                    PermissionNode permissionNode = new PermissionNode(perm);

                    permissionNode.addPermission(permission.substring(index+1));

                    permissions.put(perm, permissionNode);
                }
                else
                    permissions.get(perm).addPermission(permission.substring(index+1));
            }
            else
                permissions.put(this.permissionNode+permission, new PermissionNode(this.permissionNode+permission));
        }

        public Set<String> getNodes(){
            return permissions.keySet();
        }
    }

}
