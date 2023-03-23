package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.MultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.item.ItemTranslatorBukkitImpl;
import codes.laivy.mlanguage.api.item.TranslatableBukkitItem;
import codes.laivy.mlanguage.injection.InjectionUtils;
import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.utils.Platform;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    private @NotNull IMultiplesLanguagesAPI api;

    public BukkitMultiplesLanguages() {
        this.api = new MultiplesLanguagesAPI(this, new ItemTranslatorBukkitImpl());
    }

    @Override
    public @NotNull IMultiplesLanguagesAPI getApi() {
        return api;
    }

    @Override
    public void setApi(@NotNull IMultiplesLanguagesAPI api) {
        this.api = api;
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
            if (e.getPlayer().getItemInHand() != null) {
                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().getType().name());
                ItemStack item = ItemStack.getNMSItemStack(e.getPlayer().getItemInHand());
                Bukkit.broadcastMessage(item.getTag().getValue().toString());

                TranslatableBukkitItem a = new TranslatableBukkitItem(item.getCraftItemStack().getItemStack(), null, null);
                Bukkit.broadcastMessage("Tag: '" + ItemStack.getNMSItemStack(a.getItem()).getTag() + "'");
                e.getPlayer().getInventory().addItem(a.getItem());
            }
        });
    }

    @Override
    public final @NotNull Type getType() {
        return Type.BUKKIT;
    }
}
