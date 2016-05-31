package io.github.djxy.permissionManager.fileManagers;

import io.github.djxy.core.files.FileManager;
import io.github.djxy.permissionManager.subjects.Player;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.scheduler.Task;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * Created by Samuel on 2016-04-10.
 */
public class ConfigFile extends FileManager {

    private final Object plugin;
    private final FileManager[] fileManagers;
    private int timeIntervalForSaving = 10;
    private String commandAlias = "pm";
    private Task task;

    public ConfigFile(Path folder, Object plugin, FileManager... fileManagers) {
        super(folder, "config");
        this.plugin = plugin;
        this.fileManagers = fileManagers;
    }

    public String getCommandAlias() {
        return commandAlias;
    }

    public void setTimeIntervalForSaving(int timeIntervalForSaving) {
        this.timeIntervalForSaving = timeIntervalForSaving;

        if(task != null)
            task.cancel();

        task = Task.builder().delay(timeIntervalForSaving, TimeUnit.MINUTES).execute(() -> {
            try {
                this.save();
                for(FileManager fileManager : fileManagers)
                    fileManager.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).submit(plugin);
    }

    @Override
    protected void save(ConfigurationNode root) {
        root.getNode("timeIntervalForSaving").setValue(timeIntervalForSaving);
        root.getNode("defaultNameFormat").setValue(Player.PLAYER_NAME_FORMAT);
        root.getNode("commandAlias").setValue(commandAlias);
    }

    @Override
    protected void load(ConfigurationNode root) {
        if(!root.getNode("suffix").isVirtual())
            Player.PLAYER_NAME_FORMAT = root.getNode("suffix").getString();
        else if(!root.getNode("defaultNameFormat").isVirtual())
            Player.PLAYER_NAME_FORMAT = root.getNode("defaultNameFormat").getString();

        if(!root.getNode("commandAlias").isVirtual())
            commandAlias = root.getNode("commandAlias").getString();

        if(!root.getNode("timeIntervalForSaving").isVirtual())
            setTimeIntervalForSaving(root.getNode("timeIntervalForSaving").getInt(10));
    }

}
