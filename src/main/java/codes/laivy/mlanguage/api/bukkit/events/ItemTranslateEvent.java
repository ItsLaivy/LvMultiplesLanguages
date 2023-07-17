package codes.laivy.mlanguage.api.bukkit.events;

import codes.laivy.mlanguage.api.bukkit.BukkitStoredMessage;
import codes.laivy.mlanguage.lang.Locale;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemTranslateEvent extends ItemEvent implements Cancellable {

    private boolean cancelled = false;

    private final @NotNull Player player;

    private @Nullable Locale locale;
    private @Nullable BukkitStoredMessage name;
    private @Nullable BukkitStoredMessage lore;

    public ItemTranslateEvent(boolean async, @NotNull ItemStack item, @NotNull Player player, @Nullable Locale locale, @Nullable BukkitStoredMessage name, @Nullable BukkitStoredMessage lore) {
        super(async, item);

        this.player = player;
        this.locale = locale;
        this.name = name;
        this.lore = lore;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * The Locale of the translation
     * @return if null, the name and lore's language will be the storage default.
     */
    public final @Nullable Locale getLocale() {
        if (getPlayer().getGameMode() == GameMode.CREATIVE) {
            return null;
        } else {
            return locale;
        }
    }

    /**
     * @param locale if null, the name and lore's language will be the storage default.
     */
    public final void setLocale(@Nullable Locale locale) {
        if (getPlayer().getGameMode() == GameMode.CREATIVE) {
            throw new UnsupportedOperationException("The player's gamemode is creative, the locale will be the default of the name/lore storage.");
        } else {
            this.locale = locale;
        }
    }

    public @Nullable BukkitStoredMessage getName() {
        return name;
    }
    public void setName(@Nullable BukkitStoredMessage name) {
        this.name = name;
    }

    public @Nullable BukkitStoredMessage getLore() {
        return lore;
    }
    public void setLore(@Nullable BukkitStoredMessage lore) {
        this.lore = lore;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
