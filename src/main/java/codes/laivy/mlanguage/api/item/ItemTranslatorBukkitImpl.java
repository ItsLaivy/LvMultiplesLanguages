package codes.laivy.mlanguage.api.item;

import codes.laivy.mlanguage.lang.Locale;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemTranslatorBukkitImpl implements ItemTranslator<ItemStack> {
    @Override
    public boolean isTranslatable(@NotNull ItemStack item) {
        return false;
    }

    @Override
    public void translate(@NotNull ItemStack item, @NotNull Locale locale) {

    }

    @Override
    public void reset(@NotNull ItemStack item) {

    }
}
