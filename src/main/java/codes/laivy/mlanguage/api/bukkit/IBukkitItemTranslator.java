package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IBukkitItemTranslator extends ItemTranslator<ItemStack, Player, BaseComponent> {

    /**
     * Translates the whole inventory of a player
     * @param player the player
     */
    void translateInventory(@NotNull Player player);

}
