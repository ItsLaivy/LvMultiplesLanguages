package codes.laivy.mlanguage.api.item;

import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagCompound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TranslatableBukkitItemStack extends ItemStack {
    public TranslatableBukkitItemStack(ItemStack stack) throws IllegalArgumentException {
        super(stack);
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        if (stack instanceof TranslatableBukkitItemStack) {
            NBTTagCompound thisCompound = codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack(this).getTag();
            NBTTagCompound otherCompound = codes.laivy.mlanguage.reflection.classes.item.ItemStack.getNMSItemStack(stack).getTag();
        }
        
        return super.isSimilar(stack);
    }
}
