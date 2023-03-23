package codes.laivy.mlanguage.reflection.objects;

import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackObjExec extends ObjectExecutor {
    public ItemStackObjExec(@Nullable ItemStack value) {
        super(value);
    }

    @Override
    public @NotNull ClassExecutor getClassExecutor() {
        return ClassExecutor.ITEMSTACK;
    }
}
