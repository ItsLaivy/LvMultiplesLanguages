package codes.laivy.mlanguage.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * This is a method that can be executed with just a simple method and can be saved into the database using Json data
 */
public final class MethodSupplier {

    private final @NotNull Method method;

    public MethodSupplier(@NotNull Method method) {
        this.method = method;
        getMethod().setAccessible(true);
    }

    public @NotNull Method getMethod() {
        return method;
    }

    public <T> @UnknownNullability T invoke(@Nullable Object object, @NotNull Object... parameters) {
        try {
            //noinspection unchecked
            return (T) method.invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("Class", getMethod().getDeclaringClass().getName());
        object.addProperty("Name", getMethod().getName());
        object.addProperty("Return", getMethod().getReturnType().getName());

        JsonArray parameters = new JsonArray();
        for (Parameter parameter : getMethod().getParameters()) {
            parameters.add(new JsonParser().parse("\"" + parameter.getType().getName() + "\""));
        }
        object.add("Parameters", parameters);

        return object;
    }
    public static @NotNull MethodSupplier deserialize(@NotNull JsonObject object) throws ClassNotFoundException, NoSuchMethodException {
        JsonArray parametersArray = object.get("Parameters").getAsJsonArray();

        Class<?> declaringClass = Class.forName(object.get("Class").getAsString());
        String name = object.get("Name").getAsString();
        Class<?> returnClass = Class.forName(object.get("Return").getAsString());
        Class<?>[] parameters = new Class[parametersArray.size()];

        int row = 0;
        for (JsonElement parameter : parametersArray) {
            parameters[row] = Class.forName(parameter.getAsString());
            row++;
        }

        Method method = declaringClass.getDeclaredMethod(name, parameters);
        method.setAccessible(true);

        if (returnClass.isAssignableFrom(method.getReturnType())) {
            return new MethodSupplier(method);
        } else {
            throw new IllegalStateException("This method '" + name + "' at the class '" + declaringClass.getName() + "' should return '" + returnClass.getName() + "' but returns '" + method.getReturnType().getName() + "'");
        }
    }

}
