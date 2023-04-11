package codes.laivy.mlanguage.api.bukkit.events;

import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import net.md_5.bungee.api.chat.BaseComponent;
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
    private @Nullable Message<BaseComponent> name;
    private @Nullable Message<BaseComponent> lore;

    private @Nullable Object[] nameReplaces;
    private @Nullable Object[] loreReplaces;

    public ItemTranslateEvent(boolean async, @NotNull ItemStack item, @NotNull Player player, @Nullable Locale locale, @Nullable Message<BaseComponent> name, @Nullable Message<BaseComponent> lore, @Nullable Object[] nameReplaces, @Nullable Object[] loreReplaces) {
        super(async, item);

        this.nameReplaces = nameReplaces;
        this.loreReplaces = loreReplaces;

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
    public @Nullable Locale getLocale() {
        if (getPlayer().getGameMode() == GameMode.CREATIVE) {
            return null;
        } else {
            return locale;
        }
    }

    /**
     * @param locale if null, the name and lore's language will be the storage default.
     */
    public void setLocale(@Nullable Locale locale) {
        if (getPlayer().getGameMode() == GameMode.CREATIVE) {
            throw new UnsupportedOperationException("The player's gamemode is creative, the locale will be the default of the name/lore storage.");
        } else {
            this.locale = locale;
        }
    }

    public @Nullable Message<BaseComponent> getName() {
        return name;
    }
    public void setName(@Nullable Message<BaseComponent> name) {
        this.name = name;
    }

    public @Nullable Message<BaseComponent> getLore() {
        return lore;
    }
    public void setLore(@Nullable Message<BaseComponent> lore) {
        this.lore = lore;
    }

    public @Nullable Object[] getNameReplaces() {
        return nameReplaces;
    }

    public void setNameReplaces(@Nullable Object[] nameReplaces) {
        this.nameReplaces = nameReplaces;
    }

    public @Nullable Object[] getLoreReplaces() {
        return loreReplaces;
    }

    public void setLoreReplaces(@Nullable Object[] loreReplaces) {
        this.loreReplaces = loreReplaces;
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
