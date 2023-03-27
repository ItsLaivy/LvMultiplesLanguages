package codes.laivy.mlanguage.api.item;

import codes.laivy.mlanguage.lang.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is responsible to translate the items
 * @param <I> the item class
 * @param <P> the player class
 */
public interface ItemTranslator<I, P> {

    boolean isTranslatable(@NotNull I item);

    /**
     * Translates a item
     * @param item the item
     * @param player the player
     */
    void translate(@NotNull I item, @NotNull P player);

    /**
     * Gets the item name if applicable
     * @param item the item
     * @return the item message name or null if not applicable
     */
    @Nullable Message getName(@NotNull I item);

    /**
     * Gets the item lore if applicable
     * @param item the item
     * @return the item message lore or null if not applicable
     */
    @Nullable Message getLore(@NotNull I item);

}
