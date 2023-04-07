package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.MultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.lang.Locale;
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
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.*;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

/**
 * The default api of the Bukkit LvMultiplesLanguages
 */
public class BukkitMultiplesLanguagesAPI extends MultiplesLanguagesAPI implements Listener {

    private @NotNull InjectionManager injectionManager;
    private @Nullable BukkitTask task;

    private @Nullable Version version;

    public BukkitMultiplesLanguagesAPI(@NotNull InjectionManager injectionManager, @NotNull BukkitMultiplesLanguages platform, @NotNull BukkitItemTranslator itemTranslator) {
        super(platform, itemTranslator);
        this.injectionManager = injectionManager;
    }

    @Override
    public @NotNull BukkitMultiplesLanguages getPlatform() {
        return (BukkitMultiplesLanguages) super.getPlatform();
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
        getLanguages().add(storage);

        BukkitMessageStorage.deserialize(storage.serialize());

        getPlatform().log(new TextComponent("§aLoaded " + getLanguages().size() + " language" + (getLanguages().size() == 1 ? "" : "s") + "."));
        //

        Bukkit.getPluginManager().registerEvents(this, multiplesLanguagesBukkit());

        LocaleTracker runnable = new LocaleTracker();
        //noinspection FunctionalExpressionCanBeFolded
        task = Bukkit.getScheduler().runTaskTimer(multiplesLanguagesBukkit(), runnable::run, 20, 20);
        loaded = true;
    }

    @Override
    public void unload() {
        loaded = false;

        messageStorages = null;
        defaultLocale = null;

        if (task != null) {
            task.cancel();
        }

        // Unregistering events
        // TODO: 06/04/2023 this
        PlayerGameModeChangeEvent.getHandlerList().unregister(this);
        InventoryOpenEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    private void gameModeChange(@NotNull PlayerGameModeChangeEvent e) {
        Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> multiplesLanguagesBukkit().getApi().getItemTranslator().translateInventory(e.getPlayer()), 1);
    }
    @EventHandler
    private void inventoryOpen(@NotNull InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player) {
            Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> multiplesLanguagesBukkit().getApi().getItemTranslator().translateInventory((Player) e.getPlayer()), 1);
        }
    }
    @EventHandler
    private void inventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> multiplesLanguagesBukkit().getApi().getItemTranslator().translateInventory((Player) e.getWhoClicked()), 5);
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
            return multiplesLanguagesBukkit().getApi().getVersion().getPlayerMinecraftLocale(player);
        }
        return null;
    }

    @Override
    public void setLocale(@NotNull UUID user, @Nullable Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BukkitItemTranslator getItemTranslator() {
        return (BukkitItemTranslator) Objects.requireNonNull(super.getItemTranslator());
    }

}
