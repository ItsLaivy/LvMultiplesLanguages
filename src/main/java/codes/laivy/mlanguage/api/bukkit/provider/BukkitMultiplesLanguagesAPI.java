package codes.laivy.mlanguage.api.bukkit.provider;

import codes.laivy.mlanguage.api.MessageSerializer;
import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.api.bukkit.IBukkitItemTranslator;
import codes.laivy.mlanguage.api.bukkit.IBukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.exceptions.PluginNotFoundException;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.main.BukkitMultiplesLanguages;
import codes.laivy.mlanguage.utils.FileUtils;
import codes.laivy.mlanguage.utils.Merge;
import com.google.gson.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.*;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;
import static org.bukkit.Bukkit.getServer;

public class BukkitMultiplesLanguagesAPI implements IBukkitMultiplesLanguagesAPI, Listener {

    private final @NotNull BukkitMultiplesLanguages plugin;
    private @Nullable Set<BukkitMessageStorage> messageStorages;

    private final @NotNull MessageSerializer<BaseComponent[], BukkitMessage, BukkitMessageStorage> serializer;

    private boolean loaded = false;

    public BukkitMultiplesLanguagesAPI(@NotNull BukkitMultiplesLanguages plugin, @NotNull MessageSerializer<BaseComponent[], BukkitMessage, BukkitMessageStorage> serializer) {
        this.plugin = plugin;
        this.serializer = serializer;
    }

    @Override
    public @NotNull BukkitMultiplesLanguages getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull Set<BukkitMessageStorage> getStorages() {
        if (messageStorages == null) {
            throw new NullPointerException("The API isn't loaded yet");
        }
        return messageStorages;
    }

    @Override
    public @NotNull BaseComponent @NotNull [] getText(@Nullable Locale locale, @NotNull BukkitMessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return messageStorage.getText(locale, id, replaces);
    }

    @Override
    public @NotNull Locale getLocale(@NotNull UUID user) {
        Player player = Bukkit.getPlayer(user);
        if (player != null && player.isOnline()) {
            try {
                return getPlugin().getVersion().getPlayerMinecraftLocale(player);
            } catch (IllegalArgumentException ignore) {
                return Locale.EN_US;
            }
        }
        throw new UnsupportedOperationException("This user isn't online");
    }

    @Override
    public void setLocale(@NotNull UUID user, @NotNull Locale locale) {
        throw new UnsupportedOperationException("The default LvMultiplesLanguages API doesn't supports locale changes because it uses the client's locale");
    }

    @Override
    public @NotNull BukkitMessage createMessage(@NotNull String id, @NotNull Map<@NotNull Locale, BaseComponent @NotNull []> data) {
        return new BukkitMessageProvider(id, data);
    }

    @Override
    public @NotNull MessageSerializer<BaseComponent[], BukkitMessage, BukkitMessageStorage> getSerializer() {
        return serializer;
    }

    @Override
    public @Nullable IBukkitItemTranslator getItemTranslator() {
        return getPlugin().getInjectionManager().getTranslator();
    }

    @Override
    public @Nullable BukkitMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name) {
        Optional<BukkitMessageStorage> optional = getStorages().stream().filter(s -> s.getName().equals(name) && s.getPluginProperty().getPlugin().equals(plugin)).findFirst();
        return optional.orElse(null);
    }

    @Override
    public @NotNull BukkitMessageStorage createStorage(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale locale, @NotNull Set<BukkitMessage> messages) {
        BukkitMessageStorage storage = null;

        for (BukkitMessageStorage fs : getStorages()) {
            if (fs.getPluginProperty().getPlugin().equals(plugin) && fs.getName().equals(name)) {
                storage = fs;

                Merge merge = merge(storage, new BukkitMessageStorageProvider(plugin, name, locale, messages));

                if (!merge.getMerged().isEmpty()) {
                    getPlugin().log(new TextComponent("§7Has been added §f" + merge.getMerged().size() + " messages §7to the §f'" + fs.getName() + "' §7message storage of the plugin §f'" + plugin.getName() + "'§7."));
                } if (!merge.getUnused().isEmpty()) {
                    StringBuilder messagesStr = new StringBuilder();
                    int row = 0;

                    for (String id : merge.getUnused()) {
                        if (row > 0) messagesStr.append("§r, ");
                        messagesStr.append("§f").append(id);
                        row++;
                    }

                    getPlugin().log(new TextComponent("§7These messages at the §f'" + fs.getName() + "' §7message storage of the plugin §f'" + plugin.getName() + "' §7isn't used by the plugin, you can remove them: " + messagesStr));
                }

                break;
            }
        }

        if (storage == null) {
            storage = new BukkitMessageStorageProvider(plugin, name, locale, messages);
            getStorages().add(storage);
        }

        return storage;
    }

    @Override
    public @NotNull BukkitMessageStorage createStorage(BukkitMessageStorage original) {
        return this.createStorage((Plugin) original.getPluginProperty().getPlugin(), original.getName(), original.getDefaultLocale(), original.getMessages());
    }

    private @NotNull Merge merge(@NotNull BukkitMessageStorage to, @NotNull BukkitMessageStorage from) {
        Set<String> merged = new HashSet<>();
        Set<String> unused = new HashSet<>();

        for (BukkitMessage toMessage : to.getMessages()) {
            Optional<BukkitMessage> fromMessage = from.getMessages().stream().filter(m -> m.getId().equals(toMessage.getId())).findFirst();
            if (!fromMessage.isPresent()) {
                unused.add(toMessage.getId());
            }
        }
        for (BukkitMessage fromMessage : from.getMessages()) {
            Optional<BukkitMessage> toMessage = to.getMessages().stream().filter(m -> m.getId().equals(fromMessage.getId())).findFirst();
            if (!toMessage.isPresent()) {
                to.getMessages().add(fromMessage.clone());
                merged.add(fromMessage.getId());
            }
        }

        return new Merge(merged, unused);
    }

    @Override
    public void load() {
        if (isLoaded()) {
            return;
        }

        messageStorages = new LinkedHashSet<>();
        getServer().getConsoleSender().sendMessage("");
        getPlugin().log(new TextComponent("§7Loading default LvMultiplesLanguages API"));

        Bukkit.getPluginManager().registerEvents(this, multiplesLanguagesBukkit());

        // Loading languages
        int loaded = 0;
        List<String> unknown = new ArrayList<>();

        getPlugin().log(new TextComponent("§7Loading message storages..."));
        try {
            for (File file : FileUtils.listFiles(getPlugin().getDataFolder().toString())) {
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                    String line; // ignore
                    StringBuilder content = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line);
                    }

                    try {
                        String dataStr = ChatColor.translateAlternateColorCodes('&', content.toString());

                        //noinspection deprecation
                        JsonElement json = new JsonParser().parse(dataStr);
                        if (json.isJsonObject()) {
                            BukkitMessageStorage storage;
                            try {
                                storage = serializer.deserializeStorage(json);
                                storage = createStorage((Plugin) storage.getPluginProperty().getPlugin(), storage.getName(), storage.getDefaultLocale(), storage.getMessages());
                            } catch (PluginNotFoundException e) {
                                unknown.add(e.getPlugin());
                                continue;
                            }

                            storage.load();

                            loaded++;
                        } else {
                            throw new JsonSyntaxException("The content of this file isn't a json object.");
                        }
                    } catch (JsonSyntaxException e) {
                        throw new RuntimeException("Couldn't load the message storage of file '" + file + "', is the json written correctly?", e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load the message storages", e);
        }
        getPlugin().log(new TextComponent("§aLoaded " + loaded + " message storages"));

        if (!unknown.isEmpty()) {
            StringBuilder unknownList = new StringBuilder();
            for (String plugin : unknown) {
                if (unknown.indexOf(plugin) != 0) unknownList.append("§c, ");
                unknownList.append("§6").append(plugin);
            }
            getServer().getConsoleSender().sendMessage("");
            getPlugin().log(new TextComponent("§cSome storages couldn't be loaded because the plugin isn't on the server/enabled anymore, here is a full list of them: §6" + unknownList));
        }
        //
        getServer().getConsoleSender().sendMessage("");

        this.loaded = true;
    }

    @Override
    public void unload() {
        if (!isLoaded()) {
            return;
        }

        // Unregistering events
        HandlerList.unregisterAll(this);

        // Unloading storages
        for (BukkitMessageStorage storage : getStorages()) {
            try {
                File rootFile = new File(getPlugin().getDataFolder(), storage.getPluginProperty().getName());
                // Create storage path (if not exists)
                if (!rootFile.exists() && !rootFile.mkdirs()) {
                    throw new IllegalStateException("Cannot create storage '" + storage.getName() + "' of the plugin '" + storage.getPluginProperty().getName() + "' path");
                }

                // Create storage file (if not exists)
                @NotNull File file = new File(rootFile, FileUtils.fileNameTranslate(storage.getName()) + ".json");
                if (!file.exists() && !file.createNewFile()) {
                    throw new IllegalStateException("Cannot create storage file data '" + storage.getPluginProperty().getName() + File.separator + rootFile.getParentFile().getName() + "' file of the storage '" + storage.getName() + "' at the plugin '" + storage.getPluginProperty().getName() + "'");
                }
                if (!file.exists()) {
                    throw new NoSuchFileException("Couldn't get the message storage file '" + storage.getPluginProperty().getName() + File.separator + rootFile.getParentFile().getName() + "'");
                }
                // Write the serialized data into
                JsonElement data = serializer.serializeStorage(storage);
                String dataStr = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(data).replace("§", "&");

                try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                     BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
                    writer.write(dataStr);
                }

                storage.unload();
            } catch (Throwable e) {
                e.printStackTrace();
                getPlugin().log(new TextComponent("§cCouldn't save message storage called '" + storage.getName() + "' of the plugin '" + storage.getPluginProperty().getName() + "'"));
            }
        }
        //

        messageStorages = null;
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    // Events

    @EventHandler
    private void gameModeChange(@NotNull PlayerGameModeChangeEvent e) {
        if (getItemTranslator() != null) {
            Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> getItemTranslator().translateInventory(e.getPlayer()), 1);
        }
    }
    @EventHandler
    private void inventoryOpen(@NotNull InventoryOpenEvent e) {
        if (getItemTranslator() != null) {
            if (e.getPlayer() instanceof Player) {
                Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> getItemTranslator().translateInventory((Player) e.getPlayer()), 1);
            }
        }

    }

    @EventHandler
    private void join(@NotNull PlayerJoinEvent e) {
        getPlugin().getInjectionManager().inject(e.getPlayer());
    }
    @EventHandler
    private void quit(@NotNull PlayerQuitEvent e) {
        getPlugin().getInjectionManager().remove(e.getPlayer());
    }
}
