package codes.laivy.mlanguage.api.bukkit.natives;

import codes.laivy.mlanguage.api.bukkit.IBukkitItemTranslator;
import codes.laivy.mlanguage.api.bukkit.IBukkitMessage;
import codes.laivy.mlanguage.lang.TranslatableItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class TranslatableBukkitItem implements TranslatableItem<ItemStack> {

    private final @NotNull ItemStack item;
    private final @Nullable IBukkitMessage name;
    private final @Nullable IBukkitMessage lore;

    public TranslatableBukkitItem(@NotNull ItemStack item, @Nullable IBukkitMessage name, @Nullable IBukkitMessage lore) {
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
    public @Nullable IBukkitMessage getName() {
        return name;
    }

    @Override
    public @Nullable IBukkitMessage getLore() {
        return lore;
    }
}
