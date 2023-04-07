package codes.laivy.mlanguage.api.bukkit.reflection.classes.item;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class CraftItemStack extends ObjectExecutor {
    @NotNull
    public static CraftItemStack getCraftItemStack(@NotNull ItemStack item) {
        return new CraftItemStack(item);
    }

    public CraftItemStack(@Nullable Object value) {
        super(value);
    }

    public @NotNull ItemStack getItemStack() {
        if (getValue() == null) {
            throw new NullPointerException("This CraftItemStack is null");
        }
        return (ItemStack) getValue();
    }

    @Override
    public @NotNull CraftItemStackClass getClassExecutor() {
        return (CraftItemStackClass) getDefApi().getVersion().getClassExec("CraftItemStack");
    }

    public static class CraftItemStackClass extends ClassExecutor {
        public CraftItemStackClass(@NotNull String className) {
            super(className);
        }
    }
}
