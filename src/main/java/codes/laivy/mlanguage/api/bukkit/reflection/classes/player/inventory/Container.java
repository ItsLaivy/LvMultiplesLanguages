package codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.versions.V1_17_R1;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
        if (!ReflectionUtils.isCompatible(V1_17_R1.class)) {
            V1_17_R1 v = (V1_17_R1) multiplesLanguagesBukkit().getVersion();

            if (!v.isStateEnabled()) {
                throw new UnsupportedOperationException("This method is only available since 1.17.1");
            }
        }

        //noinspection DataFlowIssue
        return (int) multiplesLanguagesBukkit().getVersion().getFieldExec("Container:stateId").invokeInstance(this);
    }
    public void setStateId(int stateId) {
        if (!ReflectionUtils.isCompatible(V1_17_R1.class)) {
            V1_17_R1 v = (V1_17_R1) multiplesLanguagesBukkit().getVersion();

            if (!v.isStateEnabled()) {
                throw new UnsupportedOperationException("This method is only available since 1.17.1");
            }
        }

        multiplesLanguagesBukkit().getVersion().getFieldExec("Container:stateId").set(this, stateId);
    }

    public @NotNull List<Slot> getSlots() {
        List<Slot> slots = new LinkedList<>();
        //noinspection unchecked
        for (Object slot : (List<Object>) Objects.requireNonNull(multiplesLanguagesBukkit().getVersion().getFieldExec("Container:slots").invokeInstance(this))) {
            slots.add(new Slot(slot));
        }
        return slots;
    }

    public int getSize() {
        return getSlots().size();
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
