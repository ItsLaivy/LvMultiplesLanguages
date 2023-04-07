package codes.laivy.mlanguage.api.bukkit.reflection.classes.others;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.objects.ObjectArrayObjExec;
import codes.laivy.mlanguage.api.bukkit.reflection.objects.ObjectObjExec;
import codes.laivy.mlanguage.api.bukkit.reflection.versions.V1_17_R1;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NonNullList extends ObjectExecutor {

    static {
        if (!ReflectionUtils.isCompatible(V1_17_R1.class)) {
            throw new UnsupportedOperationException("This class is compatible only with 1.17+");
        }
    }

    public static @NotNull NonNullList create(@Nullable Object d, @NotNull List<Object> list) {
        return new NonNullList(getDefApi().getVersion().getMethodExec("NonNullList:create").invokeStatic(new ObjectObjExec(d), new ObjectArrayObjExec(list.toArray(new Object[0]))));
    }

    public NonNullList(@Nullable Object value) {
        super(value);
    }

    public @NotNull List<?> getList() {
        return (List<?>) Objects.requireNonNull(getDefApi().getVersion().getFieldExec("NonNullList:list").invokeInstance(this));
    }
    public @Nullable Object getDefault() {
        return getDefApi().getVersion().getFieldExec("NonNullList:default").invokeInstance(this);
    }

    @Override
    public @NotNull NonNullListClass getClassExecutor() {
        return (NonNullListClass) getDefApi().getVersion().getClassExec("NonNullList");
    }

    public static class NonNullListClass extends ClassExecutor {
        public NonNullListClass(@NotNull String className) {
            super(className);
        }
    }
}
