package codes.laivy.mlanguage.api.bukkit.reflection.executors;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassConstructor {

    private final Constructor<?> constructor;

    public ClassConstructor(@NotNull Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @NotNull
    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Object newInstance(ObjectExecutor... objectExecutors) {
        try {
            return constructor.newInstance(ObjectExecutor.toObjectArray(objectExecutors));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Constructor's instance creation", e);
        }
    }

}
