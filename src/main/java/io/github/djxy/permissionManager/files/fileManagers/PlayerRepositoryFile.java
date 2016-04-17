package io.github.djxy.permissionManager.files.fileManagers;

import io.github.djxy.permissionManager.files.FileManager;
import io.github.djxy.permissionManager.repositories.PlayerRepository;
import ninja.leaping.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Samuel on 2016-04-13.
 */
public class PlayerRepositoryFile extends FileManager {

    public PlayerRepositoryFile(Path folder) {
        super(folder, "playerList");
    }

    @Override
    protected void save(ConfigurationNode root) {
        for(UUID uuid : PlayerRepository.getInstance().getPlayersUUID())
            root.getNode(uuid.toString()).setValue(PlayerRepository.getInstance().getPlayerName(uuid));
    }

    @Override
    protected void load(ConfigurationNode root) {
        Map<Object, ConfigurationNode> players = (Map<Object, ConfigurationNode>) root.getChildrenMap();

        for(Object uuid : players.keySet())
            PlayerRepository.getInstance().setPlayer(UUID.fromString((String) uuid), players.get(uuid).getString());
    }

}
