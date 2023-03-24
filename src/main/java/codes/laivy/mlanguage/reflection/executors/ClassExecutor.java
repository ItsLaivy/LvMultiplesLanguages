package codes.laivy.mlanguage.reflection.executors;

import codes.laivy.mlanguage.utils.ClassUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * Creates a class instance simply and easier
 */
public class ClassExecutor implements Executor {

    public static final ClassExecutor VOID = new ClassExecutor(void.class) {
        @Override
        public @NotNull Class<Void> getReflectionClass() {
            return void.class;
        }
    };

    public static final ClassExecutor STRING = new ClassExecutor(String.class) {
        @Override
        public @NotNull Class<String> getReflectionClass() {
            return String.class;
        }
    };

    public static final ClassExecutor STRING_ARRAY = new ClassExecutor(String[].class) {
        @Override
        public @NotNull Class<String[]> getReflectionClass() {
            return String[].class;
        }
    };

    public static final ClassExecutor ENUM = new ClassExecutor(Enum.class) {
        @SuppressWarnings("unchecked")
        @Override
        public @NotNull Class<Enum<?>> getReflectionClass() {
            return (Class<Enum<?>>) super.getReflectionClass();
        }
    };

    public static final ClassExecutor ENUM_ARRAY = new ClassExecutor(Enum[].class) {
        @SuppressWarnings("unchecked")
        @Override
        public @NotNull Class<Enum<?>[]> getReflectionClass() {
            return (Class<Enum<?>[]>) super.getReflectionClass();
        }
    };

    public static final ClassExecutor UUID = new ClassExecutor(UUID.class) {
        @Override
        public @NotNull Class<UUID> getReflectionClass() {
            return UUID.class;
        }
    };

    public static final ClassExecutor COLLECTION = new ClassExecutor(Collection.class) {
        @SuppressWarnings("unchecked")
        @Override
        public @NotNull Class<Collection<?>> getReflectionClass() {
            return (Class<Collection<?>>) super.getReflectionClass();
        }
    };

    public static final ClassExecutor ITEMSTACK = new ClassExecutor(ItemStack.class) {
        @Override
        public @NotNull Class<ItemStack> getReflectionClass() {
            return ItemStack.class;
        }
    };

    public static final ClassExecutor OBJECT = new ClassExecutor(Object.class) {
        @Override
        public @NotNull Class<Object> getReflectionClass() {
            return Object.class;
        }
    };

    public static final ClassExecutor DOUBLE = new ClassExecutor(double.class) {
        @Override
        public @NotNull Class<Double> getReflectionClass() {
            return double.class;
        }
    };
    public static final ClassExecutor DOUBLE_ARRAY = new ClassExecutor(double[].class) {
        @Override
        public @NotNull Class<double[]> getReflectionClass() {
            return double[].class;
        }
    };
    public static final ClassExecutor FLOAT = new ClassExecutor(float.class) {
        @Override
        public @NotNull Class<Float> getReflectionClass() {
            return float.class;
        }
    };
    public static final ClassExecutor FLOAT_ARRAY = new ClassExecutor(float[].class) {
        @Override
        public @NotNull Class<float[]> getReflectionClass() {
            return float[].class;
        }
    };
    public static final ClassExecutor INT = new ClassExecutor(int.class) {
        @Override
        public @NotNull Class<Integer> getReflectionClass() {
            return int.class;
        }
    };
    public static final ClassExecutor INT_ARRAY = new ClassExecutor(int[].class) {
        @Override
        public @NotNull Class<int[]> getReflectionClass() {
            return int[].class;
        }
    };
    public static final ClassExecutor BOOLEAN = new ClassExecutor(boolean.class) {
        @Override
        public @NotNull Class<Boolean> getReflectionClass() {
            return boolean.class;
        }
    };
    public static final ClassExecutor BOOLEAN_ARRAY = new ClassExecutor(boolean[].class) {
        @Override
        public @NotNull Class<boolean[]> getReflectionClass() {
            return boolean[].class;
        }
    };
    public static final ClassExecutor BYTE = new ClassExecutor(byte.class) {
        @Override
        public @NotNull Class<Byte> getReflectionClass() {
            return byte.class;
        }
    };
    public static final ClassExecutor BYTE_ARRAY = new ClassExecutor(byte[].class) {
        @Override
        public @NotNull Class<byte[]> getReflectionClass() {
            return byte[].class;
        }
    };
    public static final ClassExecutor SHORT = new ClassExecutor(short.class) {
        @Override
        public @NotNull Class<Short> getReflectionClass() {
            return short.class;
        }
    };
    public static final ClassExecutor LONG = new ClassExecutor(long.class) {
        @Override
        public @NotNull Class<Long> getReflectionClass() {
            return long.class;
        }
    };

    public static Class<?>[] toClassArray(ClassExecutor[] classExecutors) {
        Class<?>[] classes = new Class[classExecutors.length];
        int row = 0;
        for (ClassExecutor exec : classExecutors) {
            classes[row] = exec.getReflectionClass();
            row++;
        }
        return classes;
    }

    @Nullable
    protected Class<?> reflectionClass;

    private final @NotNull String name;
    private final boolean array;

    public ClassExecutor(@NotNull String className) {
        this(className, false);
    }
    public ClassExecutor(@NotNull Class<?> reflectionClass) {
        this.reflectionClass = reflectionClass;

        this.name = getName();
        this.array = isArray();
    }
    public ClassExecutor(@NotNull Class<?> reflectionClass, boolean array) {
        this(reflectionClass.getName(), array);
    }

    public ClassExecutor(@NotNull String name, boolean array) {
        this.name = name;
        this.array = array;
    }

    @NotNull
    public Class<?> getReflectionClass() {
        if (!isLoaded()) {
            throw new NullPointerException("This ClassExecutor isn't loaded yet.");
        }
        return Objects.requireNonNull(reflectionClass);
    }

    @NotNull
    public Method getMethod(@NotNull String name, @NotNull ClassExecutor... parameters) throws NoSuchMethodException {
        return getReflectionClass().getMethod(name, toClassArray(parameters));
    }
    @NotNull
    public Method getDeclaredMethod(@NotNull String name, @NotNull ClassExecutor... parameters) throws NoSuchMethodException {
        return getReflectionClass().getDeclaredMethod(name, toClassArray(parameters));
    }

    @NotNull
    public Field getField(@NotNull String name) throws NoSuchFieldException {
        return getReflectionClass().getField(name);
    }
    @NotNull
    public Field getDeclaredField(@NotNull String name) throws NoSuchFieldException {
        return getReflectionClass().getDeclaredField(name);
    }

    public boolean isReflectiveInstance(@NotNull Object object) {
        return ClassUtils.isInstanceOf(getReflectionClass(), object.getClass());
    }
    public boolean isReflectiveInstance(@NotNull ObjectExecutor object) {
        Object value = object.getValue();
        if (value != null) {
            return this.isReflectiveInstance(value);
        }
        return false;
    }

    public @NotNull String getName() {
        if (isLoaded()) {
            return getReflectionClass().getName();
        } else {
            return name;
        }
    }
    public boolean isArray() {
        if (isLoaded()) {
            return getReflectionClass().isArray();
        } else {
            return array;
        }
    }

    @NotNull
    public Object cast(@NotNull Object object) {
        return getReflectionClass().cast(object);
    }
    @NotNull
    public Object cast(@NotNull ObjectExecutor object) {
        if (object.getValue() == null) {
            throw new NullPointerException("This '" + object.getClassExecutor().getName() + "' executor's value is null!");
        }

        return this.cast(object.getValue());
    }

    @NotNull
    public ClassConstructor getConstructor(@NotNull ClassExecutor... parameters) {
        try {
            Constructor<?> constructor = getReflectionClass().getConstructor(toClassArray(parameters));
            constructor.setAccessible(true);

            return new ClassConstructor(constructor);
        } catch (NoSuchMethodException ignore) {
            try {
                Constructor<?> constructor = getReflectionClass().getDeclaredConstructor(toClassArray(parameters));
                constructor.setAccessible(true);

                return new ClassConstructor(constructor);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Getting the constructor's instance", e);
            }
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void load() {
        try {
            reflectionClass = Class.forName((isArray() ? "[L" : "") + getName() + (isArray() ? ";" : ""));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isLoaded() {
        return reflectionClass != null;
    }

    public static final class BrokenClassExecutor extends ClassExecutor {
        public BrokenClassExecutor() {
            super("");
        }

        @Override
        public void load() {
        }

        @Override
        public @NotNull Class<?> getReflectionClass() {
            return getClass();
        }

        @Override
        public boolean isLoaded() {
            return true;
        }
    }

}
