package codes.laivy.mlanguage.api.bukkit.reflection.classes.item;

import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.objects.ItemStackObjExec;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class ItemStack extends ObjectExecutor {
    @NotNull
    public static ItemStack getNMSItemStack(@NotNull org.bukkit.inventory.ItemStack item) {
        return new ItemStack(multiplesLanguagesBukkit().getVersion().getMethodExec("CraftItemStack:asNMSCopy").invokeStatic(new ItemStackObjExec(item)));
    }

    public ItemStack(@Nullable Object value) {
        super(value);
    }

    public @Nullable NBTTagCompound getTag() {
        Object object = multiplesLanguagesBukkit().getVersion().getFieldExec("ItemStack:tag").invokeInstance(this);
        if (object == null) {
            return null;
        }
        return new NBTTagCompound(object);
    }
    public void setTag(@Nullable NBTTagCompound compound) {
        if (compound != null) {
            multiplesLanguagesBukkit().getVersion().getFieldExec("ItemStack:tag").set(this, compound.getValue());
        } else {
            multiplesLanguagesBukkit().getVersion().getFieldExec("ItemStack:tag").set(this, null);
        }
    }

    public void setName(@Nullable BaseComponent name) {
        multiplesLanguagesBukkit().getVersion().setItemDisplayName(this, name);
    }

    public void setLore(@NotNull BaseComponent... lore) {
        multiplesLanguagesBukkit().getVersion().setItemLore(this, lore);
    }

    public @NotNull CraftItemStack getCraftItemStack() {
        return new CraftItemStack(Objects.requireNonNull(multiplesLanguagesBukkit().getVersion().getMethodExec("CraftItemStack:asCraftMirror").invokeStatic(this)));
    }

    @Override
    public @NotNull ItemStackClass getClassExecutor() {
        return (ItemStackClass) multiplesLanguagesBukkit().getVersion().getClassExec("ItemStack");
    }

    public static class ItemStackClass extends ClassExecutor {
        public ItemStackClass(@NotNull String className) {
            super(className);
        }
    }
}
