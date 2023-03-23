package codes.laivy.mlanguage.reflection.objects;

import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;

public class LongObjExec extends ObjectExecutor {
    public LongObjExec(long value) {
        super(value);
    }

    @Override
    public @NotNull ClassExecutor getClassExecutor() {
        return ClassExecutor.LONG;
    }
}
