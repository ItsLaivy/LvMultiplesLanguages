package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.*;
import codes.laivy.mlanguage.api.bukkit.reflection.versions.V1_9_R1;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.api.bukkit.InjectionManager;
import codes.laivy.mlanguage.utils.Platform;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class BukkitMultiplesLanguages extends JavaPlugin implements Platform, Listener {

    public static @NotNull BukkitMultiplesLanguages multiplesLanguagesBukkit() {
        return JavaPlugin.getPlugin(BukkitMultiplesLanguages.class);
    }

    private @NotNull BukkitMultiplesLanguagesAPI api;
    private boolean serverLoaded = false;

    public BukkitMultiplesLanguages() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();

        BukkitItemTranslator translator = new BukkitItemTranslator();
        this.api = new BukkitMultiplesLanguagesAPI(new InjectionManager(translator), this, translator);
    }

    @Override
    public @NotNull BukkitMultiplesLanguagesAPI getApi() {
        return api;
    }

    @Override
    public void setApi(@NotNull IMultiplesLanguagesAPI api) {
        if (api instanceof BukkitMultiplesLanguagesAPI) {
            if (getApi().isLoaded()) getApi().unload();
            this.api = (BukkitMultiplesLanguagesAPI) api;
            if (isServerLoaded()) api.load();
        } else {
            throw new IllegalArgumentException("This API isn't a bukkit API!");
        }
    }

    @Override
    public void log(@NotNull BaseComponent component) {
        getServer().getConsoleSender().sendMessage("§8[§6" + getDescription().getName() + "§8]§7" + " " + component.toLegacyText());
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // On server loads, load the API too
        Bukkit.getScheduler().runTaskLater(this, () -> {
            serverLoaded = true;
            getApi().load();
        }, 1);
    }

    @EventHandler
    private void chat(@NotNull AsyncPlayerChatEvent e) {
        Bukkit.getScheduler().runTask(this, () -> {
            if (e.getMessage().equals("get")) {
                BukkitMessageStorage storage = (BukkitMessageStorage) Objects.requireNonNull(getApi().getLanguage("Nome teste", this));

                if (ReflectionUtils.isCompatible(V1_9_R1.class)) {
                    e.getPlayer().getInventory().setItem(45, new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.DIAMOND_PICKAXE), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                }
                Set<ItemStack> items = new LinkedHashSet<>();
                for (org.bukkit.inventory.ItemStack ignore2 : e.getPlayer().getInventory()) {
                    int random = new Random().nextInt(5);
                    Material material = null;
                    if (random == 0) {
                        material = Material.DIAMOND;
                    } else if (random == 1) {
                        material = Material.REDSTONE;
                    } else if (random == 2) {
                        material = Material.COAL;
                    } else if (random == 3) {
                        material = Material.GOLD_INGOT;
                    } else {
                        material = Material.IRON_INGOT;
                    }

                    items.add(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(material, new Random().nextInt(64) + 1), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                }
                e.getPlayer().getInventory().setContents(items.toArray(new org.bukkit.inventory.ItemStack[0]));

                e.getPlayer().getInventory().setHelmet(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                e.getPlayer().getInventory().setChestplate(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_CHESTPLATE), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                e.getPlayer().getInventory().setLeggings(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
                e.getPlayer().getInventory().setBoots(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
            }
        });
    }

    @EventHandler
    private void login(@NotNull PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (e.getPlayer().isOnline()) {
                if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    e.getPlayer().setGameMode(GameMode.SURVIVAL);
                } else {
                    e.getPlayer().setGameMode(GameMode.CREATIVE);
                }
            }
        }, 50, 50);
    }

    private boolean isServerLoaded() {
        return serverLoaded;
    }

    @Override
    public final @NotNull Type getType() {
        return Type.BUKKIT;
    }
}
