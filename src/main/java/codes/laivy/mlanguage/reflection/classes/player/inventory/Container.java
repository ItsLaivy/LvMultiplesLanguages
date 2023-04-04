package codes.laivy.mlanguage.reflection.classes.player.inventory;

import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.reflection.objects.IntegerObjExec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class Container extends ObjectExecutor {
    public Container(@Nullable Object value) {
        super(value);
    }

    public int getId() {
        //noinspection DataFlowIssue
        return (int) multiplesLanguagesBukkit().getVersion().getFieldExec("Container:windowId").invokeInstance(this);
    }

    public int getStateId() {
        //noinspection DataFlowIssue
        return (int) multiplesLanguagesBukkit().getVersion().getFieldExec("Container:stateId").invokeInstance(this);
    }
    public void setStateId(int stateId) {
        multiplesLanguagesBukkit().getVersion().getFieldExec("Container:stateId").set(this, stateId);
    }

    @Override
    public @NotNull ContainerClass getClassExecutor() {
        return (ContainerClass) multiplesLanguagesBukkit().getVersion().getClassExec("Container");
    }

    public static class ContainerClass extends ClassExecutor {
        public ContainerClass(@NotNull String className) {
            super(className);
        }
    }
}
