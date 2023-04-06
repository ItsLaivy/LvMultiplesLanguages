package codes.laivy.mlanguage.reflection.classes.player.inventory;

import codes.laivy.mlanguage.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class Slot extends ObjectExecutor {
    public Slot(@Nullable Object value) {
        super(value);
    }

    public int getIndex() {
        //noinspection DataFlowIssue
        return (int) multiplesLanguagesBukkit().getVersion().getFieldExec("Slot:index").invokeInstance(this);
    }
    public @Nullable ItemStack getItem() {
        return new ItemStack(multiplesLanguagesBukkit().getVersion().getMethodExec("Slot:getItem").invokeInstance(this));
    }

    @Override
    public @NotNull SlotClass getClassExecutor() {
        return (SlotClass) multiplesLanguagesBukkit().getVersion().getClassExec("Slot");
    }

    public static class SlotClass extends ClassExecutor {
        public SlotClass(@NotNull String className) {
            super(className);
        }
    }
}
