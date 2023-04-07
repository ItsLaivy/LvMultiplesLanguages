package codes.laivy.mlanguage.api.bukkit.reflection.executors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * The FieldExec class are an extension of the Java reflect Field.
 */
public class FieldExecutor implements Executor {

    private @Nullable Field field;

    private final @NotNull ClassExecutor fieldClass;
    private final @NotNull ClassExecutor returnType;
    private final @NotNull String name;
    private final @NotNull String means;

    private final boolean notDeclared;
    private final boolean declared;

    public FieldExecutor(@NotNull ClassExecutor fieldClass, @NotNull ClassExecutor returnType, @NotNull String name, @NotNull String means) {
        this(fieldClass, returnType, name, means, true, true);
    }
    public FieldExecutor(@NotNull ClassExecutor fieldClass, @NotNull ClassExecutor returnType, @NotNull String name, @NotNull String means, boolean notDeclared, boolean declared) {
        this.fieldClass = fieldClass;
        this.returnType = returnType;
        this.name = name;
        this.means = means;

        this.declared = declared;
        this.notDeclared = notDeclared;
    }

    public @NotNull ClassExecutor getFieldClass() {
        return fieldClass;
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

    @Nullable
    public Field getField() {
        return field;
    }

    public boolean isStatic() {
        if (!isLoaded()) {
            throw new NullPointerException("This FieldExecutor isn't loaded yet.");
        } assert getField() != null;

        return Modifier.isStatic(getField().getModifiers());
    }

    /**
     * The invokeInstance method will invoke the Java reflect field and return the value
     * @param instance the object instance (you can use null if the field is static or {@link #invokeStatic()})
     * @return the return of the method
     */
    @Nullable
    public Object invokeInstance(@Nullable ObjectExecutor instance) {
        if (!isLoaded()) {
            throw new NullPointerException("This FieldExecutor isn't loaded yet.");
        } assert getField() != null;

        if (instance == null && !isStatic()) { // Trying to execute object field as static
            throw new RuntimeException("Cannot invoke this field as static field because it is an object field");
        } else if (instance != null && isStatic()) { // Trying to execute static field as object
            throw new RuntimeException("Cannot invoke this field as object field because it is an static field");
        } else {
            try {
                if (instance != null && instance.getValue() == null) {
                    throw new NullPointerException("This value's instance is null!");
                }
                return getField().get((instance != null ? instance.getValue() : null));
            } catch (Exception e) {
                throw new RuntimeException(getMeans(), e);
            }
        }
    }

    /**
     * Same as {@link #invokeInstance(ObjectExecutor)}, but that method executes as static and doesn't need an instance
     * @return the return of the method
     */
    @Nullable
    public Object invokeStatic() {
        return this.invokeInstance(null);
    }

    public void set(@NotNull ObjectExecutor instance, @Nullable Object value) {
        if (!isLoaded()) {
            throw new NullPointerException("This FieldExecutor isn't loaded yet.");
        } assert getField() != null;

        try {
            this.getField().set(instance.getValue(), value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldExecutor fieldExecutor = (FieldExecutor) o;
        return isStatic() == fieldExecutor.isStatic() && Objects.equals(getFieldClass(), fieldExecutor.getFieldClass()) && Objects.equals(getReturnType(), fieldExecutor.getReturnType()) && Objects.equals(getName(), fieldExecutor.getName()) && Objects.equals(getMeans(), fieldExecutor.getMeans()) && Objects.equals(getField(), fieldExecutor.getField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFieldClass(), getReturnType(), getName(), getMeans(), getField(), isStatic());
    }

    @Override
    public void load() {
        if (isLoaded()) {
            return;
        }

        if (!getFieldClass().isLoaded()) {
            getFieldClass().load();
        }
        if (!getReturnType().isLoaded()) {
            getReturnType().load();
        }

        try {
            if (declared) {
                try {
                    this.field = this.getFieldClass().getDeclaredField(name);
                } catch (NoSuchFieldException e) {
                    if (notDeclared) {
                        this.field = this.getFieldClass().getField(name);
                    } else {
                        throw e;
                    }
                }
            } else if (notDeclared) {
                this.field = this.getFieldClass().getField(name);
            } else {
                throw new NullPointerException("'declared' and 'notDeclared' couldn't be both false!");
            }

            field.setAccessible(true);
            Class<?> r = returnType.getReflectionClass();

            if (!r.isAssignableFrom(field.getType())) {
                throw new NoSuchFieldException("This field's type is '" + field.getType().getName() + "', the requested is '" + returnType.getName() + "'");
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot create the FieldExecutor named '" + getName() + "' from class '" + getFieldClass().getName() + "' with return type '" + getReturnType().getName() + "', meaning '" + getMeans() + "'", e);
        }
    }

    @Override
    public boolean isLoaded() {
        return field != null;
    }
}
