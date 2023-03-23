package codes.laivy.mlanguage.reflection.objects;

import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;

public class IntegerArrayObjExec extends ObjectExecutor {
    public IntegerArrayObjExec(int[] value) {
        super(value);
    }

    @Override
    public @NotNull ClassExecutor getClassExecutor() {
        return ClassExecutor.INT_ARRAY;
    }
}
