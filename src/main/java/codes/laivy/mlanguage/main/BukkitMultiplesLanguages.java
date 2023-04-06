package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.*;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslatorImpl;
import codes.laivy.mlanguage.injection.InjectionUtils;
import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.reflection.classes.player.inventory.Container;
import codes.laivy.mlanguage.reflection.classes.player.inventory.Slot;
import codes.laivy.mlanguage.reflection.versions.V1_9_R1;
import codes.laivy.mlanguage.utils.Platform;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.*;

public class BukkitMultiplesLanguages extends JavaPlugin implements Platform, Listener {

    public static @NotNull BukkitMultiplesLanguages multiplesLanguagesBukkit() {
        return JavaPlugin.getPlugin(BukkitMultiplesLanguages.class);
    }

    public static int MODE = 5;
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

            log(new TextComponent("§7Loaded: " + version.getClasses().size() + " classes, " + version.getMethods().size() + " methods and " + version.getFields().size() + " fields."));

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
    private void chat(@NotNull AsyncPlayerChatEvent e) {
        if (e.getMessage().equals("change")) {
            e.setCancelled(true);
            CHANGE = !CHANGE;
            return;
        }
        if (e.getMessage().equals("test1")) {
            e.setCancelled(true);
            for (int row = 0; row < e.getPlayer().getOpenInventory().countSlots(); row++) {
                e.getPlayer().getOpenInventory().setItem(row, new ItemStack(Material.DIAMOND, row + 1));
            }
            return;
        }
        if (e.getMessage().equals("test2")) {
            e.setCancelled(true);
            for (int row = 0; row < 50; row++) {
                EntityPlayer.getEntityPlayer(e.getPlayer()).getConnection().sendPacket(version.createSetSlotPacket(0, row, -1, codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack(new ItemStack(Material.DIAMOND, row + 1))));
            }
            return;
        }

        try {
            EntityPlayer.getEntityPlayer(e.getPlayer()).getConnection().sendPacket(version.createSetSlotPacket(0, Integer.parseInt(e.getMessage()), -1, codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack(new ItemStack(Material.DIAMOND))));
            //MODE = Integer.parseInt(e.getMessage());
        } catch (NumberFormatException ignore) {
            Bukkit.getScheduler().runTask(this, () -> {
                if (e.getMessage().equals("get")) {
                    BukkitMessageStorage storage = (BukkitMessageStorage) Objects.requireNonNull(getApi().getLanguage("Nome teste", this));

                    e.getPlayer().getInventory().setHelmet(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                    e.getPlayer().getInventory().setChestplate(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_CHESTPLATE), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                    e.getPlayer().getInventory().setLeggings(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                    e.getPlayer().getInventory().setBoots(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                    if (ReflectionUtils.isCompatible(V1_9_R1.class)) {
                        e.getPlayer().getInventory().setItem(45, new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.DIAMOND_PICKAXE), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                    }
                    Set<org.bukkit.inventory.ItemStack> items = new LinkedHashSet<>();
                    for (org.bukkit.inventory.ItemStack ignore2 : e.getPlayer().getInventory()) {
                        items.add(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.WOOL, new Random().nextInt(64) + 1, (short) 0, (byte) new Random().nextInt(16)), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                    }
                    e.getPlayer().getInventory().setContents(items.toArray(new org.bukkit.inventory.ItemStack[0]));
                }
            });
        }
    }

    @Override
    public final @NotNull Type getType() {
        return Type.BUKKIT;
    }
}
