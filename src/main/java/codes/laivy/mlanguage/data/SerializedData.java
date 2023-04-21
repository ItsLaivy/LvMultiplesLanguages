package codes.laivy.mlanguage.data;

import codes.laivy.mlanguage.utils.ClassUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class SerializedData {

    private final @NotNull JsonElement data;
    private final int version;
    private final @NotNull Method deserializator;

    public SerializedData(@NotNull JsonElement data, int version, @NotNull Method deserializator) {
        this.data = data;
        this.version = version;
        this.deserializator = deserializator;

        if (!Modifier.isStatic(deserializator.getModifiers())) {
            throw new IllegalArgumentException("The deserializator method needs to be static");
        } else if (!(deserializator.getParameters().length == 1 && SerializedData.class.isAssignableFrom(deserializator.getParameters()[0].getType()))) {
            throw new IllegalArgumentException("The deserializator method needs to have one SerializedData parameter!");
        }
    }

    /**
     * The serialized data
     * @return the data
     */
    public @NotNull JsonElement getData() {
        return data;
    }

    /**
     * The version that this serialized data has created
     * @return the version
     */
    @Contract(pure = true)
    public int getVersion() {
        return version;
    }

    /**
     * The deserializator method supplier
     * @return the deserializator
     */
    @Contract(pure = true)
    public @NotNull Method getDeserializator() {
        return deserializator;
    }

    public @NotNull JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.add("Data", getData());
        object.addProperty("Version", getVersion());
        object.addProperty("Deserializator", getDeserializator().getDeclaringClass().getName() + "#" + getDeserializator().getName());

        return object;
    }
    @Contract(pure = true)
    public static @NotNull SerializedData deserialize(@NotNull JsonObject object) {
        try {
            JsonElement data = object.get("Data");
            int version = object.get("Version").getAsInt();
            Method supplier = stringToMethod(object.get("Deserializator").getAsString());

            return new SerializedData(data, version, supplier);
        } catch (Throwable e) {
            throw new RuntimeException("Cannot deserialize '" + object + "'", e);
        }
    }

    /**
     * Converts a "example.package.Class#Method" into a method
     */
    @Contract(pure = true)
    private static @NotNull Method stringToMethod(@NotNull String str) throws ClassNotFoundException {
        if (str.contains("#")) {
            String className = str.split("#")[0];
            String methodName = str.split("#")[1];

            if (ClassUtils.isPresent(className)) {
                try {
                    Method method = Class.forName(className).getDeclaredMethod(methodName, SerializedData.class);
                    method.setAccessible(true);
                    return method;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Couldn't find method named '" + methodName + "' at class '" + className + "'", e);
                }
            } else {
                throw new ClassNotFoundException("Couldn't find the class named '" + className + "'");
            }
        } else {
            throw new IllegalArgumentException("This string '" + str + "' isn't a package and method name");
        }
    }

    @Contract(pure = true)
    public static boolean isSerializedData(@NotNull JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            return object.has("Data") && object.has("Version") && object.has("Deserializator");
        }
        return false;
    }

    /**
     * Deserializes the object using the static deserializator method supplier
     * @return the object
     */
    public <T> @NotNull T get() {
        try {
            //noinspection unchecked
            return (T) getDeserializator().invoke(null, this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
