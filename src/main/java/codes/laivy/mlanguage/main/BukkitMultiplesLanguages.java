package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.*;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslatorImpl;
import codes.laivy.mlanguage.injection.InjectionUtils;
import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.utils.Platform;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class BukkitMultiplesLanguages extends JavaPlugin implements Platform, Listener {

    public static @NotNull BukkitMultiplesLanguages multiplesLanguagesBukkit() {
        return JavaPlugin.getPlugin(BukkitMultiplesLanguages.class);
    }

    private @Nullable Version version;
    private @NotNull BukkitMultiplesLanguagesAPI api;

    public BukkitMultiplesLanguages() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();
        this.api = new BukkitMultiplesLanguagesAPI(this, new BukkitItemTranslatorImpl());
    }

    @Override
    public @NotNull BukkitMultiplesLanguagesAPI getApi() {
        return api;
    }

    @Override
    public void setApi(@NotNull IMultiplesLanguagesAPI api) {
        if (api instanceof BukkitMultiplesLanguagesAPI) {
            getApi().unload();
            this.api = (BukkitMultiplesLanguagesAPI) api;
            api.load();
        } else {
            throw new IllegalArgumentException("This API isn't a bukkit API!");
        }
    }

    @EventHandler
    private void click(@NotNull InventoryClickEvent e) {
        Bukkit.broadcastMessage(e.getSlot() + "");
    }

    @Override
    public void log(@NotNull Object message) {
        getServer().getConsoleSender().sendMessage("§8[§6" + getDescription().getName() + "§8]§7" + " " + message);
    }

    public @NotNull Version getVersion() {
        return Objects.requireNonNull(version);
    }

    public void setVersion(@NotNull Version version) {
        this.version = version;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        try {
            //noinspection unchecked
            Class<Version> clazz = (Class<Version>) ReflectionUtils.getNullableClass("codes.laivy.mlanguage.reflection.versions." + ReflectionUtils.getVersionName().toUpperCase());
            if (clazz == null) {
                throw new NullPointerException("Couldn't find this server version's properties (" + ReflectionUtils.getVersionName() + ")");
            }

            Constructor<Version> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            Version version = constructor.newInstance();
            version.loadClasses();
            version.loadMethods();
            version.loadFields();

            log("§7Loaded: " + version.getClasses().size() + " classes, " + version.getMethods().size() + " methods and " + version.getFields().size() + " fields");

            setVersion(version);
        } catch (Throwable e) {
            throw new RuntimeException("Version loading", e);
        }

        getApi().load();
    }

    @EventHandler
    private void login(@NotNull PlayerJoinEvent e) {
        InjectionUtils.injectPlayer(e.getPlayer());
    }
    @EventHandler
    private void quit(@NotNull PlayerQuitEvent e) {
        InjectionUtils.removePlayer(e.getPlayer());
    }

    @EventHandler
    private void chat(@NotNull AsyncPlayerChatEvent e) {
        Bukkit.getScheduler().runTask(this, () -> {
            if (e.getMessage().equals("1")) {
                if (e.getPlayer().getItemInHand() != null) {
                    TranslatableBukkitItem a = new TranslatableBukkitItem(e.getPlayer().getItemInHand(), new BukkitMessage(Objects.requireNonNull(getApi().getLanguage("Nome teste", this)), "Teste1"), new BukkitMessage(Objects.requireNonNull(getApi().getLanguage("Nome teste", this)), "Teste2"));
                    Bukkit.broadcastMessage("Tag: '" + ItemStack.getNMSItemStack(a.getItem()).getTag() + "'");
                    e.getPlayer().getInventory().addItem(a.getItem());
                }
            } else if (e.getMessage().equals("2")) {
                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().getClass().getName());
                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().isSimilar(e.getPlayer().getItemInHand()) + "");
            } else if (e.getMessage().equals("3")) {
                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().getItemMeta().getDisplayName());
                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().getItemMeta().getLore().toString());
            } else if (e.getMessage().equals("4")) {
                Bukkit.broadcastMessage(ComponentSerializer.toString(ItemStack.getNMSItemStack(e.getPlayer().getItemInHand()).getName()));

                @Nullable BaseComponent[] lore = ItemStack.getNMSItemStack(e.getPlayer().getItemInHand()).getLore();
                if (lore != null) {
                    Bukkit.broadcastMessage("Lore:");
                    for (BaseComponent component : lore) {
                        Bukkit.broadcastMessage(ComponentSerializer.toString(component));
                    }
                }
            } else if (e.getMessage().equals("5")) {
                Bukkit.broadcastMessage(ItemStack.getNMSItemStack(e.getPlayer().getItemInHand()).getTag().toString());
            }
        });
    }

    @Override
    public final @NotNull Type getType() {
        return Type.BUKKIT;
    }
}
