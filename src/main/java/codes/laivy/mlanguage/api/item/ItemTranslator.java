package codes.laivy.mlanguage.api.item;

import codes.laivy.mlanguage.reflection.classes.packets.PacketPlayOutSetSlot;
import org.jetbrains.annotations.NotNull;

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
     * @param window the window ID of this item's inventory
     * @param slot the item slot
     */
    @NotNull PacketPlayOutSetSlot translate(@NotNull I item, @NotNull P player, int window, int slot);

}
