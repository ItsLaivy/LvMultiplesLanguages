package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.MultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.utils.Platform;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class BukkitMultiplesLanguagesAPI extends MultiplesLanguagesAPI implements Listener {

    private @Nullable BukkitTask task;

    public BukkitMultiplesLanguagesAPI(@NotNull Platform platform, @NotNull BukkitItemTranslator itemTranslator) {
        super(platform, itemTranslator);
    }

    @Override
    public void load() {
        super.load();

        Bukkit.getPluginManager().registerEvents(this, multiplesLanguagesBukkit());

        LocaleTracker runnable = new LocaleTracker();
        //noinspection FunctionalExpressionCanBeFolded
        task = Bukkit.getScheduler().runTaskTimer(multiplesLanguagesBukkit(), runnable::run, 20, 20);
    }

    @Override
    public void unload() {
        super.unload();

        if (task != null) {
            task.cancel();
        }

        // Unregistering events
        // TODO: 06/04/2023 this
        PlayerGameModeChangeEvent.getHandlerList().unregister(this);
        InventoryOpenEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    protected void gameModeChange(@NotNull PlayerGameModeChangeEvent e) {
        Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> {
            multiplesLanguagesBukkit().getApi().getItemTranslator().translateInventory(e.getPlayer());
        }, 1);
    }
    @EventHandler
    protected void inventoryOpen(@NotNull InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player) {
            Bukkit.getScheduler().runTaskLater(multiplesLanguagesBukkit(), () -> {
                multiplesLanguagesBukkit().getApi().getItemTranslator().translateInventory((Player) e.getPlayer());
            }, 1);
        }
    }

    @Override
    public @Nullable Locale getLocale(@NotNull UUID user) {
        Player player = Bukkit.getPlayer(user);
        if (player != null && player.isOnline()) {
            return multiplesLanguagesBukkit().getVersion().getPlayerMinecraftLocale(player);
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
