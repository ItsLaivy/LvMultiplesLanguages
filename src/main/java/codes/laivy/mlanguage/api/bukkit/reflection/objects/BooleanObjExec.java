package codes.laivy.mlanguage.api.bukkit.reflection.objects;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;

public class BooleanObjExec extends ObjectExecutor {
    public BooleanObjExec(boolean value) {
        super(value);
    }

    @Override
    public @NotNull ClassExecutor getClassExecutor() {
        return ClassExecutor.BOOLEAN;
    }
}
