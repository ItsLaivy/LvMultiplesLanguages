package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.bukkit.natives.BukkitMessage;
import codes.laivy.mlanguage.api.bukkit.natives.BukkitMessageStorage;
import codes.laivy.mlanguage.api.bukkit.natives.InjectionManager;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.main.BukkitMultiplesLanguages;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.utils.FileUtils;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import com.google.gson.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.*;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

/**
 * The default api of the Bukkit LvMultiplesLanguages
 */
public final class BukkitMultiplesLanguagesAPI implements IBukkitMultiplesLanguagesAPI, Listener {

    /**
     * Checks if the server is running the default api
     * @return true if the server is running the default api (this api class), false otherwise
     */
    public static boolean isRunningDefApi() {
        IBukkitMultiplesLanguagesAPI currentApi = multiplesLanguagesBukkit().getApi();
        return currentApi instanceof BukkitMultiplesLanguagesAPI;
    }

    /**
     * Returns the default api instance, or an exception if the default api isn't being used.
     * @return the default api or an exception case not is being used
     */
    @ApiStatus.Internal
    public static @NotNull BukkitMultiplesLanguagesAPI getDefApi() {
        if (isRunningDefApi()) {
            return (BukkitMultiplesLanguagesAPI) multiplesLanguagesBukkit().getApi();
        } else {
            throw new UnsupportedOperationException("This server needs to be running the default api to perform this action");
        }
    }

    private final @NotNull BukkitMultiplesLanguages plugin;

    private final @NotNull BukkitItemTranslator itemTranslator;
    private @Nullable Set<MessageStorage> messageStorages;

    private boolean loaded = false;

    private @NotNull InjectionManager injectionManager;

    private @Nullable Version version;

    public BukkitMultiplesLanguagesAPI(@NotNull BukkitMultiplesLanguages plugin, @NotNull BukkitItemTranslator itemTranslator, @NotNull InjectionManager injectionManager) {
        this.plugin = plugin;
        this.itemTranslator = itemTranslator;
        this.injectionManager = injectionManager;
    }

    @Override
    public void load() {
        messageStorages = new LinkedHashSet<>();
        getPlugin().log(new TextComponent("§7Loading default LvMultiplesLanguages API"));

        try {
            //noinspection unchecked
            Class<Version> clazz = (Class<Version>) ReflectionUtils.getNullableClass("codes.laivy.mlanguage.api.bukkit.reflection.versions." + ReflectionUtils.getVersionName().toUpperCase());
            if (clazz == null) {
                getPlugin().log(new TextComponent("§cCouldn't find this server version's properties (§4" + ReflectionUtils.getVersionName() + "§c), this plugin version §nisn't compatible§c with this server version yet."));
                Bukkit.getPluginManager().disablePlugin(getPlugin());
                return;
            }

            Constructor<Version> constructor = clazz.getDeclaredConstructor(BukkitMultiplesLanguagesAPI.class);
            constructor.setAccessible(true);

            Version version = constructor.newInstance(this);
            version.loadClasses();
            version.loadMethods();
            version.loadFields();

            getPlugin().log(new TextComponent("§eReflections §b- §7Loaded: " + version.getClasses().size() + " classes, " + version.getMethods().size() + " methods and " + version.getFields().size() + " fields."));

            this.version = version;
        } catch (Throwable e) {
            e.printStackTrace();
            getPlugin().log(new TextComponent("§cCouldn't load the version properties"));
            Bukkit.getPluginManager().disablePlugin(getPlugin());
            return;
        }

        Bukkit.getPluginManager().registerEvents(this, multiplesLanguagesBukkit());

        // Loading languages
        int loaded = 0;
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
                        //noinspection deprecation
                        JsonElement json = new JsonParser().parse(content.toString());
                        if (json.isJsonObject()) {
                            MessageStorage storage = SerializedData.deserialize(json.getAsJsonObject()).get(null);
                            storage.load();

                            messageStorages.add(storage);

                            loaded++;
                        } else {
                            throw new JsonSyntaxException("The content of this file isn't a json object.");
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        getPlugin().log(new TextComponent("§cCouldn't load the message storage of file '" + file + "', is the json written correctly?"));
                        Bukkit.getPluginManager().disablePlugin(getPlugin());
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            getPlugin().log(new TextComponent("§cCouldn't load the message storages"));
            Bukkit.getPluginManager().disablePlugin(getPlugin());
            return;
        }
        getPlugin().log(new TextComponent("§aLoaded " + loaded + " message storages"));
        //

        this.loaded = true;
    }

    @Override
    public void unload() {
        // Unregistering events
        PlayerGameModeChangeEvent.getHandlerList().unregister(this);
        InventoryOpenEvent.getHandlerList().unregister(this);
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);

        // Unloading storages
        for (MessageStorage storage : getStorages()) {
            try {
                File rootFile = new File(getPlugin().getDataFolder(), getPlugin().getName() + File.separator);
                // Create storage path (if not exists)
                if (!rootFile.exists() && !rootFile.mkdirs()) {
                    throw new IllegalStateException("Cannot create storage '" + storage.getName() + "' of the plugin '" + getPlugin().getName() + "' path");
                }
                // Create storage file (if not exists)
                @NotNull File file = new File(rootFile, FileUtils.fileNameTranslate(storage.getName()) + ".json");
                if (!file.exists() && !file.createNewFile()) {
                    throw new IllegalStateException("Cannot create storage file data '" + getPlugin().getName() + File.separator + rootFile.getParentFile().getName() + "' file of the storage '" + getPlugin().getName() + "' at the plugin '" + storage.getName() + "'");
                }
                if (!file.exists()) {
                    throw new NoSuchFileException("Couldn't get the message storage file '" + getPlugin().getName() + File.separator + rootFile.getParentFile().getName() + "'");
                }
                // Write the serialized data into
                JsonElement data = storage.serialize().serialize();

                try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                     BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(data));
                }

                storage.unload();
            } catch (Throwable e) {
                e.printStackTrace();
                getPlugin().log(new TextComponent("§cCouldn't save message storage called '" + storage.getName() + "' of the plugin '" + getPlugin().getName() + "'"));
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

    @Override
    public @NotNull BukkitMultiplesLanguages getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull Set<MessageStorage> getStorages() {
        if (messageStorages == null) {
            throw new NullPointerException("The API isn't loaded yet");
        }
        return messageStorages;
    }

    @Override
    public @NotNull IBukkitMessageStorage create(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components) {
        IBukkitMessageStorage storage = null;

        for (MessageStorage fs : getStorages()) {
            if (fs.getPlugin().equals(plugin) && fs.getName().equals(name)) {
                if (fs instanceof IBukkitMessageStorage) {
                    storage = (IBukkitMessageStorage) fs;

                    BukkitMessageStorage temp = new BukkitMessageStorage(plugin, name, defaultLocale, components);
                    if (storage.merge(temp)) {
                        getPlugin().log(new TextComponent("New messages has been added to the '" + fs.getName() + "' message storage of the plugin '" + getPlugin().getName() + "'."));
                    }
                }
            }
        }

        if (storage == null) {
            storage = new BukkitMessageStorage(plugin, name, defaultLocale, components);
        }

        return storage;
    }

    @Override
    public @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return messageStorage.get(locale, id, replaces);
    }

    @Override
    public @NotNull IBukkitMessage get(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        if (!(messageStorage instanceof BukkitMessageStorage)) {
            throw new UnsupportedOperationException("The message storage needs to be an instance of the bukkit message storage");
        }

        return new BukkitMessage((BukkitMessageStorage) messageStorage, id, replaces);
    }

    @Override
    public @Nullable IBukkitMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name) {
        for (MessageStorage messageStorage : getStorages()) {
            if (messageStorage.getName().equals(name) && messageStorage.getPlugin().equals(plugin)) {
                if (messageStorage instanceof IBukkitMessageStorage) {
                    return (IBukkitMessageStorage) messageStorage;
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull Locale getLocale(@NotNull UUID user) {
        Player player = Bukkit.getPlayer(user);
        if (player != null && player.isOnline()) {
            return getDefApi().getVersion().getPlayerMinecraftLocale(player);
        }
        throw new UnsupportedOperationException("This user isn't online");
    }

    @Override
    public void setLocale(@NotNull UUID user, @Nullable Locale locale) {
        throw new UnsupportedOperationException("The default LvMultiplesLanguages API doesn't supports locale changes because it uses the client's locale");
    }

    @Override
    public @NotNull BukkitItemTranslator getItemTranslator() {
        return itemTranslator;
    }

    // Bukkit API methods

    public @NotNull Version getVersion() {
        if (version == null) {
            throw new NullPointerException("The API isn't loaded yet.");
        }
        return version;
    }
    public @NotNull InjectionManager getInjectionManager() {
        return injectionManager;
    }

    public void setInjectionManager(@NotNull InjectionManager injectionManager) {
        this.injectionManager = injectionManager;
    }

    @EventHandler
    private void gameModeChange(@NotNull PlayerGameModeChangeEvent e) {
        Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> getItemTranslator().translateInventory(e.getPlayer()), 1);
    }
    @EventHandler
    private void inventoryOpen(@NotNull InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player) {
            Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> getItemTranslator().translateInventory((Player) e.getPlayer()), 1);
        }
    }

    @EventHandler
    private void join(@NotNull PlayerJoinEvent e) {
        getInjectionManager().inject(e.getPlayer());
    }
    @EventHandler
    private void quit(@NotNull PlayerQuitEvent e) {
        getInjectionManager().remove(e.getPlayer());
    }

}
