package codes.laivy.mlanguage.api.item;

import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.TranslatableItem;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagByte;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagString;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TranslatableBukkitItem implements TranslatableItem<ItemStack> {

    private final @NotNull ItemStack item;
    private final @Nullable Message name;
    private final @Nullable Message lore;

    public TranslatableBukkitItem(@NotNull ItemStack item, @Nullable Message name, @Nullable Message lore) {
        this.name = name;
        this.lore = lore;

        codes.laivy.mlanguage.reflection.classes.item.ItemStack nmsItem = codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack(item);
        NBTTagCompound compound = nmsItem.getTag();

        compound.set("Translatable", new NBTTagByte((byte) 1));
        if (getName() != null) {
            compound.set("NameTranslation", new NBTTagString(getName().serialize().serialize().toString()));
        }
        if (getLore() != null) {
            compound.set("LoreTranslation", new NBTTagString(getLore().serialize().serialize().toString()));
        }

        this.item = new ItemStack(nmsItem.getCraftItemStack().getItemStack()) {
            @Override
            public boolean isSimilar(ItemStack stack) {
                return super.isSimilar(stack);
            }
        };
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
