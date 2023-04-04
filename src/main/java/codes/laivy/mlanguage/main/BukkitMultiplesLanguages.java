package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.*;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslatorImpl;
import codes.laivy.mlanguage.injection.InjectionUtils;
import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.utils.Platform;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class BukkitMultiplesLanguages extends JavaPlugin implements Platform, Listener {

    public static @NotNull BukkitMultiplesLanguages multiplesLanguagesBukkit() {
        return JavaPlugin.getPlugin(BukkitMultiplesLanguages.class);
    }

    public static int MODE = 0;
    public static boolean CHANGE = true;

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

    @Override
    public void log(@NotNull BaseComponent component) {
        getServer().getConsoleSender().sendMessage("§8[§6" + getDescription().getName() + "§8]§7" + " " + component.toLegacyText());
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
                throw new NullPointerException("Couldn't find this server version's properties (" + ReflectionUtils.getVersionName() + "), this plugin version isn't compatible with this version yet.");
            }

            Constructor<Version> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            Version version = constructor.newInstance();
            version.loadClasses();
            version.loadMethods();
            version.loadFields();

            log(new TextComponent("§7Loaded: " + version.getClasses().size() + " classes, " + version.getMethods().size() + " methods and " + version.getFields().size() + " fields"));

            setVersion(version);
        } catch (Throwable e) {
            throw new RuntimeException("Version loading", e);
        }

        getApi().load();
    }

    @EventHandler
    private void gameModeChange(@NotNull PlayerGameModeChangeEvent e) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            getApi().getItemTranslator().translateInventory(e.getPlayer());
        }, 1);
    }

    @EventHandler
    private void login(@NotNull PlayerJoinEvent e) {
        InjectionUtils.injectPlayer(e.getPlayer());

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (e.getPlayer().isOnline() && CHANGE) {
                if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    e.getPlayer().setGameMode(GameMode.SURVIVAL);
                } else {
                    e.getPlayer().setGameMode(GameMode.CREATIVE);
                }
            }
        }, 50, 50);
    }
    @EventHandler
    private void quit(@NotNull PlayerQuitEvent e) {
        InjectionUtils.removePlayer(e.getPlayer());
    }

    @EventHandler
    private void click(@NotNull InventoryClickEvent e) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            for (int row = 0; row < 41; row++) {
                ItemStack item = e.getWhoClicked().getInventory().getItem(row);

                if (item != null && item.getType() != Material.AIR) {
                    Bukkit.broadcastMessage("Slot: '" + row + "', item: '" + item.getType().name() + "'");
                }
            }
            Bukkit.broadcastMessage("---=---");
        }, 10);
    }

    @EventHandler
    private void chat(@NotNull AsyncPlayerChatEvent e) {
        if (e.getMessage().equals("change")) {
            CHANGE = !CHANGE;
        } else {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§aMode changed.");
            MODE = Integer.parseInt(e.getMessage());
        }
//        Bukkit.getScheduler().runTask(this, () -> {
//            if (e.getMessage().equals("1")) {
//                if (e.getPlayer().getItemInHand() != null) {
//                    TranslatableBukkitItem a = new TranslatableBukkitItem(e.getPlayer().getItemInHand(), new BukkitMessage(Objects.requireNonNull(getApi().getLanguage("Nome teste", this)), "Teste1"), new BukkitMessage(Objects.requireNonNull(getApi().getLanguage("Nome teste", this)), "Teste2"));
//                    Bukkit.broadcastMessage("Tag: '" + ItemStack.getNMSItemStack(a.getItem()).getTag() + "'");
//                    e.getPlayer().getInventory().addItem(a.getItem());
//                }
//            } else if (e.getMessage().equals("2")) {
//                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().getClass().getName());
//                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().isSimilar(e.getPlayer().getItemInHand()) + "");
//            } else if (e.getMessage().equals("3")) {
//                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().getItemMeta().getDisplayName());
//                Bukkit.broadcastMessage(e.getPlayer().getItemInHand().getItemMeta().getLore().toString());
//            } else if (e.getMessage().equals("5")) {
//                Bukkit.broadcastMessage(ItemStack.getNMSItemStack(e.getPlayer().getItemInHand()).getTag().toString());
//            } else if (e.getMessage().equals("6")) {
//                ItemStack nmsItem = ItemStack.getNMSItemStack(e.getPlayer().getItemInHand());
//                nmsItem.setName(new TextComponent("ATaa teste"));
//                org.bukkit.inventory.ItemStack bukkitItem = nmsItem.getCraftItemStack().getItemStack();
//
//                e.getPlayer().getInventory().addItem(bukkitItem);
//            }
//        });
    }

    @Override
    public final @NotNull Type getType() {
        return Type.BUKKIT;
    }
}
