package io.github.djxy.permissionManager;

import com.google.inject.Inject;
import io.github.djxy.core.CorePlugin;
import io.github.djxy.core.CoreUtil;
import io.github.djxy.core.commands.Command;
import io.github.djxy.core.commands.executors.FileSaveExecutor;
import io.github.djxy.core.commands.executors.FileSetFormatExecutor;
import io.github.djxy.core.commands.nodes.ChoiceNode;
import io.github.djxy.core.commands.nodes.MultipleNode;
import io.github.djxy.core.commands.nodes.Node;
import io.github.djxy.core.commands.nodes.arguments.*;
import io.github.djxy.core.files.FileManager;
import io.github.djxy.core.files.fileManagers.TranslationsFile;
import io.github.djxy.core.translation.Translator;
import io.github.djxy.permissionManager.commands.arguments.GroupSubjectNode;
import io.github.djxy.permissionManager.commands.arguments.PermissionNode;
import io.github.djxy.permissionManager.commands.arguments.PlayerSubjectNode;
import io.github.djxy.permissionManager.commands.arguments.PromotionNode;
import io.github.djxy.permissionManager.commands.executors.*;
import io.github.djxy.permissionManager.fileManagers.ConfigFile;
import io.github.djxy.permissionManager.fileManagers.GroupFile;
import io.github.djxy.permissionManager.fileManagers.PlayerFile;
import io.github.djxy.permissionManager.fileManagers.PromotionFile;
import io.github.djxy.permissionManager.home.HomeService;
import io.github.djxy.permissionManager.home.RedProtectHomeContainer;
import io.github.djxy.permissionManager.pmSubjects.PMPlayerSubject;
import io.github.djxy.permissionManager.region.FoxGuardRegionContainer;
import io.github.djxy.permissionManager.region.RedProtectRegionContainer;
import io.github.djxy.permissionManager.region.RegionService;
import io.github.djxy.permissionManager.subjects.Player;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by Samuel on 2016-03-20.
 */
@Plugin(id = "permissionmanager", name = "PermissionManager", version = "1.0", dependencies = @Dependency(id = "djxycore"))
public class Main implements CorePlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static Translator getTranslatorInstance(){
        return instance.getTranslator();
    }

    @Inject @DefaultConfig(sharedRoot = false) private Path path;
    private PlayerFile players;
    private GroupFile groups;
    private ConfigFile config;
    private PromotionFile promotions;
    private Translator translator = new Translator(TextSerializers.FORMATTING_CODE.deserialize("&f[&6Permission&l&4M&r&f] "));
    private ArrayList<FileManager> translationsFiles;

    @Override
    public String getGithubApiURL() {
        return "https://api.github.com/repos/djxy/PermissionManager";
    }

    @Override
    public Path getTranslationPath() {
        return path.getParent().resolve("translations");
    }

    @Override
    public Translator getTranslator() {
        return translator;
    }

    @Override
    public void loadTranslations() {
        translationsFiles = CoreUtil.loadTranslationFiles(getTranslationPath(), translator);
    }

    @Override
    public List<FileManager> getFileManagers(Class<? extends FileManager>... classes) {
        List<FileManager> fileManagers = new ArrayList<>();

        for(Class<? extends FileManager> clazz : classes){
            if(clazz == TranslationsFile.class) fileManagers.addAll((Collection<? extends FileManager>) translationsFiles.clone());
            else if(clazz == PlayerFile.class) fileManagers.add(players);
            else if(clazz == ConfigFile.class) fileManagers.add(config);
            else if(clazz == GroupFile.class) fileManagers.add(groups);
            else if(clazz == PromotionFile.class) fileManagers.add(promotions);
        }

        return fileManagers;
    }

    @Override
    public FileManager getFileManager(String name, Class<? extends FileManager>... classes) {
        for(Class<? extends FileManager> clazz : classes){
            if(clazz == TranslationsFile.class) {
                for (FileManager translationsFile : translationsFiles)
                    if (translationsFile.getName().equals(name))
                        return translationsFile;
            }
            else if(clazz == PlayerFile.class && players.getName().equals(name)) return players;
            else if(clazz == ConfigFile.class && config.getName().equals(name)) return config;
            else if(clazz == GroupFile.class && groups.getName().equals(name)) return groups;
            else if(clazz == PromotionFile.class && promotions.getName().equals(name)) return promotions;
        }

        return null;
    }

    @Listener
    public void onGameConstructionEvent(GameConstructionEvent event) {
        instance = this;
    }

    @Listener
    public void onGamePreInitializationEvent(GamePreInitializationEvent event) throws IOException {
        if(isClassLoaded("net.foxdenstudio.sponge.foxguard.plugin.FGManager"))
            RegionService.getInstance().addRegionContainer(new FoxGuardRegionContainer());
        if(isClassLoaded("br.net.fabiozumbi12.redprotect.API.RedProtectAPI")){
            RegionService.getInstance().addRegionContainer(new RedProtectRegionContainer());
            HomeService.getInstance().addHomeContainer(new RedProtectHomeContainer());
        }

        CoreUtil.loadFileManagers(
                players = new PlayerFile(path.getParent()),
                groups = new GroupFile(path.getParent()),
                promotions = new PromotionFile(path.getParent()),
                config = new ConfigFile(path.getParent(), this, players, groups, promotions)
        );

        if(PermissionManager.getInstance().getDefaultGroup() == null)
            PermissionManager.getInstance().setDefaultGroup(PermissionManager.getInstance().getOrCreateGroup("default"));

        PermissionManager.getInstance().getOrCreateCustomSubject("DedicatedServer");
        Sponge.getCommandManager().register(this, createCommand(), config.getCommandAlias());
        Sponge.getServiceManager().setProvider(this, PermissionService.class, PMService.getInstance());
    }

    @Listener
    public void onGameStoppingServerEvent(GameStoppingServerEvent event){
        CoreUtil.saveFileManagers(players, promotions, groups, config);
    }

    @Listener
    public void onCommand(SendCommandEvent event){
        if(event.getCommand().startsWith("help")){
            Optional<org.spongepowered.api.entity.living.player.Player> player = event.getCause().first(org.spongepowered.api.entity.living.player.Player.class);

            if(player.isPresent())
                ((PMPlayerSubject)PMService.getInstance().getUserSubjects().get(player.get().getUniqueId().toString())).updateTickHelpCommand();
        }
    }

    @Listener
    public void onMessageChatEvent(MessageChannelEvent.Chat event){
        org.spongepowered.api.entity.living.player.Player source = event.getCause().first(org.spongepowered.api.entity.living.player.Player.class).get();
        Player player = PermissionManager.getInstance().getPlayer(source.getUniqueId());

        event.setMessage(TextSerializers.FORMATTING_CODE.deserialize(player.getDisplayPrefix()).concat(TextSerializers.FORMATTING_CODE.deserialize(player.getSuffix().replace("%player%", source.getName()) + "&f&r")).concat(event.getRawMessage()));
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Login event){
        PermissionManager.getInstance().getOrCreatePlayer(event.getProfile().getUniqueId(), true);
    }

    private Command createCommand(){
        Node root = new ChoiceNode("")
                .addNode(new PlayerSubjectNode("promote", "player")
                        .addNode(new PromotionNode("as", "promotion")
                                .setExecutor(new PromotePlayerExecutor())))
                .addNode(new PromotionNode("promotion")
                        .addNode(new ChoiceNode("set")
                                .addNode(new StringNode("name")
                                        .setExecutor(new PromotionSetNameExecutor())))
                        .addNode(new ChoiceNode("add")
                                .addNode(new GroupSubjectNode("group")
                                        .addNode(new ChoiceNode("remove")
                                                .setExecutor(new PromotionAddGroupToRemoveExecutor()))
                                        .addNode(new ChoiceNode("add")
                                                .setExecutor(new PromotionAddGroupToAddExecutor()))))
                        .addNode(new ChoiceNode("remove")
                                .addNode(new GroupSubjectNode("group")
                                        .addNode(new ChoiceNode("remove")
                                                .setExecutor(new PromotionRemoveGroupToRemoveExecutor()))
                                        .addNode(new ChoiceNode("add")
                                                .setExecutor(new PromotionRemoveGroupToAddExecutor())))))
                .addNode(new ChoiceNode("set")
                        .addNode(new ChoiceNode("default")
                                .addNode(new MultipleNode("suffix")
                                        .setExecutor(new DefaultSetSuffixExecutor()))
                                .addNode(new GroupSubjectNode("group")
                                        .setExecutor(new DefaultSetGroupExecutor())))
                        .addNode(new IntegerNode("savingInterval", "time")
                                .setExecutor(new SetTimeIntervalForSavingExecutor(config))))
                .addNode(new ChoiceNode("delete")
                        .addNode(new PromotionNode("promotion")
                                .setExecutor(new DeletePromotionExecutor()))
                        .addNode(new GroupSubjectNode("group")
                                .setExecutor(new DeleteGroupExecutor())))
                .addNode(new ChoiceNode("create")
                        .addNode(new StringNode("promotion")
                                .setExecutor(new CreatePromotionExecutor()))
                        .addNode(new StringNode("group")
                                .setExecutor(new CreateGroupExecutor())))
                .addNode(new FileManagerNode("file", this, config.getClass(), groups.getClass(), players.getClass(), promotions.getClass())
                        .addNode(new ChoiceNode("save")
                                .setExecutor(new FileSaveExecutor()))
                        .addNode(new ChoiceNode("set")
                                .addNode(new FileFormatNode("format")
                                        .setExecutor(new FileSetFormatExecutor()))))
                .addNode(new PlayerSubjectNode("player", "subject")
                        .addNode(new ChoiceNode("remove")
                                .addNode(new ChoiceNode("prefix")
                                        .setExecutor(new SubjectRemovePrefixExecutor()))
                                .addNode(new ChoiceNode("suffix")
                                        .setExecutor(new PlayerRemoveSuffixExecutor()))
                                .addNode(new PermissionNode("permission")
                                        .setExecutor(new SubjectRemovePermissionExecutor())
                                        .addNode(new WorldNode("world")
                                                .setExecutor(new SubjectRemovePermissionWorldExecutor())))
                                .addNode(new GroupSubjectNode("group")
                                        .setExecutor(new SubjectRemoveGroupExecutor())))
                        .addNode(new ChoiceNode("add")
                                .addNode(new GroupSubjectNode("group")
                                        .setExecutor(new SubjectAddGroupExecutor())
                                        .addNode(new WorldNode("world")
                                                .setExecutor(new SubjectAddGroupWorldExecutor()))))
                        .addNode(new ChoiceNode("set")
                                .addNode(new ChoiceNode("data")
                                        .addNode(new StringNode("key")
                                                .addNode(new StringNode("value")
                                                        .setExecutor(new PlayerSetDataExecutor())))
                                        .addNode(new WorldNode("world")
                                                .addNode(new StringNode("key")
                                                        .addNode(new StringNode("value")
                                                                .setExecutor(new PlayerSetDataWorldExecutor())))))
                                .addNode(new MultipleNode("prefix")
                                        .setExecutor(new SubjectSetPrefixExecutor()))
                                .addNode(new MultipleNode("suffix")
                                        .setExecutor(new PlayerSetSuffixExecutor()))
                                .addNode(new PermissionNode("permission")
                                        .addNode(new WorldNode("world")
                                                .addNode(new BooleanNode("value")
                                                        .setExecutor(new SubjectSetPermissionWorldExecutor())))
                                        .addNode(new BooleanNode("value")
                                                .setExecutor(new SubjectSetPermissionExecutor())))))
                .addNode(new GroupSubjectNode("group", "subject")
                        .addNode(new ChoiceNode("add")
                                .addNode(new GroupSubjectNode("group")
                                        .setExecutor(new SubjectAddGroupExecutor())
                                        .addNode(new WorldNode("world")
                                                .setExecutor(new SubjectAddGroupWorldExecutor()))))
                        .addNode(new ChoiceNode("remove")
                                .addNode(new ChoiceNode("prefix")
                                        .setExecutor(new SubjectRemovePrefixExecutor()))
                                .addNode(new PermissionNode("permission")
                                        .setExecutor(new SubjectRemovePermissionExecutor())
                                        .addNode(new WorldNode("world")
                                                .setExecutor(new SubjectRemovePermissionWorldExecutor())))
                                .addNode(new GroupSubjectNode("group")
                                        .setExecutor(new SubjectRemoveGroupExecutor())))
                        .addNode(new ChoiceNode("set")
                                .addNode(new StringNode("name")
                                        .setExecutor(new GroupSetName()))
                                .addNode(new IntegerNode("rank")
                                        .setExecutor(new GroupSetRankExecutor()))
                                .addNode(new MultipleNode("prefix")
                                        .setExecutor(new SubjectSetPrefixExecutor()))
                                .addNode(new PermissionNode("permission")
                                        .addNode(new WorldNode("world")
                                                .addNode(new BooleanNode("value")
                                                        .setExecutor(new SubjectSetPermissionWorldExecutor())))
                                        .addNode(new BooleanNode("value")
                                                .setExecutor(new SubjectSetPermissionExecutor())))));

        return new Command(root);
    }

    private boolean isClassLoaded(String className){
        try{
            Class.forName(className);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
