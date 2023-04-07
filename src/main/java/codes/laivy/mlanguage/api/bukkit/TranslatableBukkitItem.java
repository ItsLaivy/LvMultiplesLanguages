package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.bukkit.translator.IBukkitItemTranslator;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.TranslatableItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class TranslatableBukkitItem implements TranslatableItem<ItemStack> {

    private final @NotNull ItemStack item;
    private final @Nullable Message name;
    private final @Nullable Message lore;

    public TranslatableBukkitItem(@NotNull ItemStack item, @Nullable Message name, @Nullable Message lore) {
        IBukkitItemTranslator translator = multiplesLanguagesBukkit().getApi().getItemTranslator();
        if (translator == null) {
            throw new UnsupportedOperationException("The current LvMultiplesLanguages api doesn't supports translatable items");
        }

        this.name = name;
        this.lore = lore;
        this.item = translator.setTranslatable(item, getName(), getLore());
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public @Nullable Message getName() {
        return name;
    }

    @Override
    public @Nullable Message getLore() {
        return lore;
    }
}
