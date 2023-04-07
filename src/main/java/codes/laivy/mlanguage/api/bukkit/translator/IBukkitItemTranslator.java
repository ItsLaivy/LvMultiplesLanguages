package codes.laivy.mlanguage.api.bukkit.translator;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IBukkitItemTranslator extends ItemTranslator<ItemStack, Player> {

    /**
     * Translates the whole inventory of a player
     * @param player the player
     */
    void translateInventory(@NotNull Player player);

}
