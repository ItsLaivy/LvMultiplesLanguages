package codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory;

import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class Slot extends ObjectExecutor {
    public Slot(@Nullable Object value) {
        super(value);
    }

    public int getIndex() {
        //noinspection DataFlowIssue
        return (int) getDefApi().getVersion().getFieldExec("Slot:index").invokeInstance(this);
    }
    public @Nullable ItemStack getItem() {
        return new ItemStack(getDefApi().getVersion().getMethodExec("Slot:getItem").invokeInstance(this));
    }

    @Override
    public @NotNull SlotClass getClassExecutor() {
        return (SlotClass) getDefApi().getVersion().getClassExec("Slot");
    }

    public static class SlotClass extends ClassExecutor {
        public SlotClass(@NotNull String className) {
            super(className);
        }
    }
}
