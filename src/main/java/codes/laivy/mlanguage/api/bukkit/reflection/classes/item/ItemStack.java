package codes.laivy.mlanguage.api.bukkit.reflection.classes.item;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.api.bukkit.reflection.objects.ItemStackObjExec;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class ItemStack extends ObjectExecutor {
    @NotNull
    public static ItemStack getNMSItemStack(@NotNull org.bukkit.inventory.ItemStack item) {
        return new ItemStack(getDefApi().getVersion().getMethodExec("CraftItemStack:asNMSCopy").invokeStatic(new ItemStackObjExec(item)));
    }

    public ItemStack(@Nullable Object value) {
        super(value);
    }

    public @Nullable NBTTagCompound getTag() {
        Object object = getDefApi().getVersion().getFieldExec("ItemStack:tag").invokeInstance(this);
        if (object == null) {
            return null;
        }
        return new NBTTagCompound(object);
    }
    public void setTag(@Nullable NBTTagCompound compound) {
        if (compound != null) {
            getDefApi().getVersion().getFieldExec("ItemStack:tag").set(this, compound.getValue());
        } else {
            getDefApi().getVersion().getFieldExec("ItemStack:tag").set(this, null);
        }
    }

    public void setName(@Nullable BaseComponent name) {
        getDefApi().getVersion().setItemDisplayName(this, name);
    }

    public void setLore(@Nullable BaseComponent[] lore) {
        getDefApi().getVersion().setItemLore(this, lore);
    }

    public @NotNull CraftItemStack getCraftItemStack() {
        return new CraftItemStack(Objects.requireNonNull(getDefApi().getVersion().getMethodExec("CraftItemStack:asCraftMirror").invokeStatic(this)));
    }

    @Override
    public @NotNull ItemStackClass getClassExecutor() {
        return (ItemStackClass) getDefApi().getVersion().getClassExec("ItemStack");
    }

    public static class ItemStackClass extends ClassExecutor {
        public ItemStackClass(@NotNull String className) {
            super(className);
        }
    }
}
