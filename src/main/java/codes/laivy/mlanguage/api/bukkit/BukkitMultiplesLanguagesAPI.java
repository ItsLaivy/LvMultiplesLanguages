package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.main.BukkitMultiplesLanguages;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
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

    private final @NotNull BukkitMultiplesLanguages platform;
    private final @NotNull BukkitItemTranslator itemTranslator;

    private @Nullable Set<MessageStorage> messageStorages;
    private @Nullable Locale defaultLocale;

    private boolean loaded = false;

    private @NotNull InjectionManager injectionManager;

    private @Nullable Version version;

    public BukkitMultiplesLanguagesAPI(@NotNull InjectionManager injectionManager, @NotNull BukkitMultiplesLanguages platform, @NotNull BukkitItemTranslator itemTranslator) {
        this.platform = platform;
        this.itemTranslator = itemTranslator;
        this.injectionManager = injectionManager;
    }

    @Override
    public @NotNull BukkitMultiplesLanguages getPlatform() {
        return platform;
    }

    @Override
    public @NotNull Set<MessageStorage> getStorages() {
        if (messageStorages == null) {
            throw new NullPointerException("The API isn't loaded yet");
        }
        return messageStorages;
    }

    @Override
    public @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return messageStorage.get(locale, id, replaces);
    }

    @Override
    public @NotNull Message get(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Message... replaces) {
        if (!(messageStorage instanceof BukkitMessageStorage)) {
            throw new UnsupportedOperationException("The message storage needs to be an instance of the bukkit message storage");
        } else if (!(replaces instanceof BukkitMessage[])) {
            throw new UnsupportedOperationException("The messages replaces array needs to be an instance of the bukkit message array");
        }

        return new BukkitMessage((BukkitMessageStorage) messageStorage, id, (BukkitMessage[]) replaces);
    }

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

    @Override
    public void load() {
        // TODO: 06/04/2023 Loading system
        defaultLocale = Locale.EN_US;
        messageStorages = new LinkedHashSet<>();

        getPlatform().log(new TextComponent("§7Loading default LvMultiplesLanguages API"));

        try {
            //noinspection unchecked
            Class<Version> clazz = (Class<Version>) ReflectionUtils.getNullableClass("codes.laivy.mlanguage.api.bukkit.reflection.versions." + ReflectionUtils.getVersionName().toUpperCase());
            if (clazz == null) {
                getPlatform().log(new TextComponent("§cCouldn't find this server version's properties (§4" + ReflectionUtils.getVersionName() + "§c), this plugin version §nisn't compatible§c with this server version yet."));
            //    Bukkit.getPluginManager().disablePlugin(getPlatform());
                return;
            }

            Constructor<Version> constructor = clazz.getDeclaredConstructor(BukkitMultiplesLanguagesAPI.class);
            constructor.setAccessible(true);

            Version version = constructor.newInstance(this);
            version.loadClasses();
            version.loadMethods();
            version.loadFields();

            getPlatform().log(new TextComponent("§7Loaded: " + version.getClasses().size() + " classes, " + version.getMethods().size() + " methods and " + version.getFields().size() + " fields."));

            this.version = version;
        } catch (Throwable e) {
            throw new RuntimeException("Version loading", e);
        }

        //
        @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> componentMap = new LinkedHashMap<>();
        componentMap.put("Teste1", new LinkedHashMap<Locale, BaseComponent[]>() {{
            TextComponent c = new TextComponent("Test name, just for test");
            c.setColor(ChatColor.RED);
            put(Locale.EN_US, new TextComponent[] {c});

            c = new TextComponent("Nome teste mesmo");
            c.setColor(ChatColor.GREEN);
            put(Locale.PT_BR, new TextComponent[] {c});
        }});
        componentMap.put("Teste2", new LinkedHashMap<Locale, BaseComponent[]>() {{
            TextComponent c = new TextComponent("Cool test lore, just for testing :)");
            c.setColor(ChatColor.RED);
            put(Locale.EN_US, new TextComponent[] {c});

            c = new TextComponent("Lore top só pra testar cara, que legal...");
            c.setColor(ChatColor.GREEN);
            put(Locale.PT_BR, new TextComponent[] {c});
        }});

        BukkitMessageStorage storage = new BukkitMessageStorage(Locale.EN_US, componentMap, "Nome teste", multiplesLanguagesBukkit());
        getStorages().add(storage);

        BukkitMessageStorage.deserialize(storage.serialize());

        getPlatform().log(new TextComponent("§aLoaded " + getStorages().size() + " language" + (getStorages().size() == 1 ? "" : "s") + "."));
        //

        Bukkit.getPluginManager().registerEvents(this, multiplesLanguagesBukkit());

        loaded = true;
    }

    @Override
    public void unload() {
        loaded = false;

        messageStorages = null;
        defaultLocale = null;

        // Unregistering events
        PlayerGameModeChangeEvent.getHandlerList().unregister(this);
        InventoryOpenEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean isLoaded() {
        return loaded;
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

    @Override
    public @Nullable Locale getLocale(@NotNull UUID user) {
        Player player = Bukkit.getPlayer(user);
        if (player != null && player.isOnline()) {
            return getDefApi().getVersion().getPlayerMinecraftLocale(player);
        }
        return null;
    }

    @Override
    public void setLocale(@NotNull UUID user, @Nullable Locale locale) {
        throw new UnsupportedOperationException("The default LvMultiplesLanguages API doesn't supports locale changes because it uses the client's locale");
    }

    @Override
    public @NotNull Locale getDefaultLocale() {
        if (defaultLocale == null) {
            throw new NullPointerException("The API isn't loaded yet");
        }
        return defaultLocale;
    }

    @Override
    public @NotNull BukkitItemTranslator getItemTranslator() {
        return itemTranslator;
    }

}
