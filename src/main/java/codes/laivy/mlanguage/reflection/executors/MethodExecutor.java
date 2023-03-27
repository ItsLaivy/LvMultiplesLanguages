package codes.laivy.mlanguage.reflection.executors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

/**
 * The MethodExec class are an extension of the Java reflect Method.
 */
public class MethodExecutor implements Executor {

    public static boolean hasMethodWithReturn(Class<?> methodClass, String methodName, Class<?> methodReturn, Class<?>... parameters) {
        try {
            Method method = methodClass.getMethod(methodName, parameters);
            return method.getReturnType() == methodReturn;
        } catch (NoSuchMethodException ignore) {
        }
        return false;
    }

    // ---/-/--- //

    private final @NotNull ClassExecutor methodClass;
    private final @NotNull ClassExecutor returnType;
    private final @NotNull ClassExecutor[] parameters;

    private final @NotNull String name;
    private final @NotNull String means;

    private @Nullable Method method;

    public MethodExecutor(@NotNull ClassExecutor methodClass, @NotNull ClassExecutor returnType, @NotNull String name, @NotNull String means, @NotNull ClassExecutor... parameters) {
        this.methodClass = methodClass;
        this.returnType = returnType;
        this.name = name;
        this.means = means;
        this.parameters = parameters;
    }

    public @NotNull ClassExecutor getMethodClass() {
        return methodClass;
    }

    public @NotNull ClassExecutor getReturnType() {
        return returnType;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getMeans() {
        return means;
    }

    public @NotNull ClassExecutor[] getParameters() {
        return parameters;
    }

    public @Nullable Method getMethod() {
        return method;
    }

    public boolean isStatic() {
        if (!isLoaded()) {
            throw new NullPointerException("This MethodExecutor isn't loaded yet.");
        } assert this.getMethod() != null;

        return Modifier.isStatic(this.getMethod().getModifiers());
    }

    @Nullable
    public Object invokeInstance(@NotNull ObjectExecutor instance, @Nullable ObjectExecutor... methodParameters) {
        if (!isLoaded()) {
            throw new NullPointerException("This MethodExecutor isn't loaded yet.");
        } assert this.getMethod() != null;

        try {
            if (instance.getValue() == null) {
                throw new NullPointerException("This value's instance is null.");
            }
            return getMethod().invoke(instance.getValue(), ObjectExecutor.toObjectArray(methodParameters));
        } catch (Exception e) {
            throw new RuntimeException(getMeans(), e);
        }
    }
    @Nullable
    public Object invokeStatic(@Nullable ObjectExecutor... methodParameters) {
        if (!isLoaded()) {
            throw new NullPointerException("This MethodExecutor isn't loaded yet.");
        } assert this.getMethod() != null;

        try {
            return getMethod().invoke(null, ObjectExecutor.toObjectArray(methodParameters));
        } catch (Exception e) {
            throw new RuntimeException(getMeans(), e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodExecutor that = (MethodExecutor) o;
        return Objects.equals(getMethodClass(), that.getMethodClass()) && Objects.equals(getName(), that.getName()) && Arrays.equals(getParameters(), that.getParameters()) && Objects.equals(getMeans(), that.getMeans()) && Objects.equals(getMethod(), that.getMethod());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getMethodClass(), getName(), getMeans(), method);
        result = 31 * result + Arrays.hashCode(getParameters());
        return result;
    }

    @Override
    public void load() {
        if (isLoaded()) {
            return;
        }
        if (!getMethodClass().isLoaded()) {
            getMethodClass().load();
        }
        if (!getReturnType().isLoaded()) {
            getReturnType().load();
        }

        try {
            try {
                this.method = this.getMethodClass().getDeclaredMethod(name, parameters);
            } catch (NoSuchMethodException ignore) {
                this.method = this.getMethodClass().getMethod(name, parameters);
            } assert this.getMethod() != null;

            this.getMethod().setAccessible(true);
            Class<?> r = returnType.getReflectionClass();

            if (!r.isAssignableFrom(this.getMethod().getReturnType())) {
                throw new NoSuchMethodException("This method's return type is '" + this.getMethod().getReturnType().getName() + "', and the requested is '" + r.getName() + "'");
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot create the MethodExecutor named '" + getName() + "', from class '" + getMethodClass().getName() + "' with return type '" + getReturnType().getName() + "', meaning '" + getMeans() + "'", e);
        }
    }

    @Override
    public boolean isLoaded() {
        return method != null;
    }
}
