package io.github.djxy.permissionManager;

import com.google.inject.Inject;
import io.github.djxy.core.commands.Command;
import io.github.djxy.core.commands.nodes.ChoiceNode;
import io.github.djxy.core.commands.nodes.MultipleNode;
import io.github.djxy.core.commands.nodes.Node;
import io.github.djxy.core.commands.nodes.arguments.*;
import io.github.djxy.core.files.fileManagers.TranslationsFile;
import io.github.djxy.core.translation.Translator;
import io.github.djxy.permissionManager.commands.arguments.GroupSubjectNode;
import io.github.djxy.permissionManager.commands.arguments.PermissionNode;
import io.github.djxy.permissionManager.commands.arguments.PlayerSubjectNode;
import io.github.djxy.permissionManager.commands.arguments.PromotionNode;
import io.github.djxy.permissionManager.commands.executors.*;
import io.github.djxy.permissionManager.fileManagers.*;
import io.github.djxy.permissionManager.home.HomeService;
import io.github.djxy.permissionManager.home.RedProtectHomeContainer;
import io.github.djxy.permissionManager.pmSubjects.PMPlayerSubject;
import io.github.djxy.permissionManager.region.FoxGuardRegionContainer;
import io.github.djxy.permissionManager.region.RedProtectRegionContainer;
import io.github.djxy.permissionManager.region.RegionService;
import io.github.djxy.permissionManager.subjects.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Samuel on 2016-03-20.
 */
@Plugin(id = "permissionmanager", name = "Permission Manager", version = "1.0", dependencies = @Dependency(id = "djxycore"))
public class Main {

    private static Translator translatorInstance;

    public static Translator getTranslatorInstance(){
        return translatorInstance;
    }

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path path;

    private Logger logger = LoggerFactory.getLogger("PermissionManager");

    private PlayerFile players;
    private GroupFile groups;
    private ConfigFile config;
    private PromotionFile promotions;
    private Translator translator;
    private ArrayList<TranslationsFile> translationsFiles;

    @Listener
    public void onGamePreInitializationEvent(GamePreInitializationEvent event) throws IOException {
        if(isClassLoaded("net.foxdenstudio.sponge.foxguard.plugin.FGManager"))
            RegionService.getInstance().addRegionContainer(new FoxGuardRegionContainer());
        if(isClassLoaded("br.net.fabiozumbi12.redprotect.API.RedProtectAPI")){
            RegionService.getInstance().addRegionContainer(new RedProtectRegionContainer());
            HomeService.getInstance().addHomeContainer(new RedProtectHomeContainer());
        }

        Path languagesPath = path.getParent().resolve("languages");
        File languagesFolder = languagesPath.toFile();
        translator = new Translator();
        translatorInstance = translator;
        translationsFiles = new ArrayList<>();

        for(File language : languagesFolder.listFiles()) {
            try {
                TranslationsFile translationsFile = new TranslationsFile(languagesPath, language.getName().substring(0, language.getName().indexOf('.')), translator);

                translationsFile.load();
                translationsFiles.add(translationsFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            players = new PlayerFile(path.getParent());

            players.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            groups = new GroupFile(path.getParent());

            groups.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            promotions = new PromotionFile(path.getParent());

            promotions.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            config = new ConfigFile(path.getParent(), this, players, groups, promotions);

            config.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(PermissionManager.getInstance().getDefaultGroup() == null)
            PermissionManager.getInstance().setDefaultGroup(PermissionManager.getInstance().getOrCreateGroup("default"));

        PermissionManager.getInstance().getOrCreateCustomSubject("DedicatedServer");
        Sponge.getCommandManager().register(this, createCommand(), config.getCommandAlias());
        Sponge.getServiceManager().setProvider(this, PermissionService.class, PMService.getInstance());
    }

    @Listener
    public void onGameStoppingServerEvent(GameStoppingServerEvent event){
        try {
            players.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            promotions.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            groups.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Listener
    public void onJoin(ClientConnectionEvent.Join event){
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
                .addNode(new FileManagerNode("file", config, groups, players, promotions)
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
