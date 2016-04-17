package io.github.djxy.permissionManager.files.fileManagers;

import io.github.djxy.permissionManager.PermissionManager;
import io.github.djxy.permissionManager.files.FileManager;
import io.github.djxy.permissionManager.subjects.Player;
import ninja.leaping.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.UUID;

/**
 * Created by Samuel on 2016-04-02.
 */
public class PlayerFile extends FileManager {

    public PlayerFile(Path folder) {
        super(folder, "players");
    }

    @Override
    protected void save(ConfigurationNode root) {
        for(Player player : PermissionManager.getInstance().getPlayers())
            player.setNode(root.getNode(player.getIdentifier()));
    }

    @Override
    protected void load(ConfigurationNode root) {
        for(Object node : root.getChildrenMap().keySet()){
            Player player = PermissionManager.getInstance().getOrCreatePlayer(UUID.fromString((String) node));
            player.initFromNode(root.getNode(node));
        }
    }

}