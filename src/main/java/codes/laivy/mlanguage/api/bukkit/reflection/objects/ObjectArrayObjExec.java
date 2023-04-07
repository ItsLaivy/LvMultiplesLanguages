package codes.laivy.mlanguage.api.bukkit.reflection.objects;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectArrayObjExec extends ObjectExecutor {
    public ObjectArrayObjExec(@Nullable Object[] value) {
        super(value);
    }

    @Override
    public @NotNull ClassExecutor getClassExecutor() {
        return ClassExecutor.OBJECT_ARRAY;
    }
}
