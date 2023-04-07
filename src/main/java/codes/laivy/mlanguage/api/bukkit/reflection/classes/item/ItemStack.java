package codes.laivy.mlanguage.api.bukkit.reflection.classes.item;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.api.bukkit.reflection.objects.ItemStackObjExec;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class ItemStack extends ObjectExecutor {
    @NotNull
    public static ItemStack getNMSItemStack(@NotNull org.bukkit.inventory.ItemStack item) {
        return new ItemStack(multiplesLanguagesBukkit().getApi().getVersion().getMethodExec("CraftItemStack:asNMSCopy").invokeStatic(new ItemStackObjExec(item)));
    }

    public ItemStack(@Nullable Object value) {
        super(value);
    }

    public @Nullable NBTTagCompound getTag() {
        Object object = multiplesLanguagesBukkit().getApi().getVersion().getFieldExec("ItemStack:tag").invokeInstance(this);
        if (object == null) {
            return null;
        }
        return new NBTTagCompound(object);
    }
    public void setTag(@Nullable NBTTagCompound compound) {
        if (compound != null) {
            multiplesLanguagesBukkit().getApi().getVersion().getFieldExec("ItemStack:tag").set(this, compound.getValue());
        } else {
            multiplesLanguagesBukkit().getApi().getVersion().getFieldExec("ItemStack:tag").set(this, null);
        }
    }

    public void setName(@Nullable BaseComponent name) {
        multiplesLanguagesBukkit().getApi().getVersion().setItemDisplayName(this, name);
    }

    public void setLore(@Nullable BaseComponent[] lore) {
        multiplesLanguagesBukkit().getApi().getVersion().setItemLore(this, lore);
    }

    public @NotNull CraftItemStack getCraftItemStack() {
        return new CraftItemStack(Objects.requireNonNull(multiplesLanguagesBukkit().getApi().getVersion().getMethodExec("CraftItemStack:asCraftMirror").invokeStatic(this)));
    }

    @Override
    public @NotNull ItemStackClass getClassExecutor() {
        return (ItemStackClass) multiplesLanguagesBukkit().getApi().getVersion().getClassExec("ItemStack");
    }

    public static class ItemStackClass extends ClassExecutor {
        public ItemStackClass(@NotNull String className) {
            super(className);
        }
    }
}
