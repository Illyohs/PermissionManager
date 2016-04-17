package io.github.djxy.permissionManager.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Samuel on 2016-04-03.
 */
public class PlayerRepository {

    private static final PlayerRepository instance = new PlayerRepository();

    public static PlayerRepository getInstance() {
        return instance;
    }

    private final ConcurrentHashMap<String,UUID> playersByName = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID,String> playersByUUID = new ConcurrentHashMap<>();


    private PlayerRepository() {
    }

    public void setPlayer(UUID uuid, String name){
        if(playersByUUID.containsKey(uuid) && !playersByUUID.get(uuid).equals(name))
            playersByName.remove(name);

        playersByName.put(name, uuid);
        playersByUUID.put(uuid, name);
    }

    public UUID getPlayerUUID(String name){
        if(name != null)
            return playersByName.containsKey(name)?playersByName.get(name):null;

        return null;
    }

    public String getPlayerName(UUID uuid){
        return playersByUUID.get(uuid);
    }

    public List<String> getPlayersName() {
        return new ArrayList<>(playersByName.keySet());
    }

    public List<UUID> getPlayersUUID() {
        return new ArrayList<>(playersByUUID.keySet());
    }

}
