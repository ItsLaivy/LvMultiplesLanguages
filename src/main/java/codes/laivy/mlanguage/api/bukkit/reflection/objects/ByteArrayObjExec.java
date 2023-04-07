package codes.laivy.mlanguage.api.bukkit.reflection.objects;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;

public class ByteArrayObjExec extends ObjectExecutor {
    public ByteArrayObjExec(byte[] value) {
        super(value);
    }

    @Override
    public @NotNull ClassExecutor getClassExecutor() {
        return ClassExecutor.BYTE_ARRAY;
    }
}
