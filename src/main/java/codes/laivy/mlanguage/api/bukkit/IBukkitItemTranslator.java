package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IBukkitItemTranslator extends ItemTranslator<ItemStack, Player, BukkitStoredMessage> {

    /**
     * Translates the whole inventory of a player
     * @param player the player
     */
    void translateInventory(@NotNull Player player);

    /**
     * Called everytime the item needs to be reset to the default messages (name/lore) locale
     * @param item the item
     */
    void reset(@NotNull ItemStack item);

}
