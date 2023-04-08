package codes.laivy.mlanguage.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SerializedData {

    private final @NotNull JsonElement data;
    private final int version;
    private final @NotNull MethodSupplier deserializator;

    public SerializedData(@NotNull JsonElement data, int version, @NotNull MethodSupplier deserializator) {
        this.data = data;
        this.version = version;
        this.deserializator = deserializator;
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
    public int getVersion() {
        return version;
    }

    /**
     * The deserializator method supplier
     * @return the deserializator
     */
    public @NotNull MethodSupplier getDeserializator() {
        return deserializator;
    }

    public @NotNull JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.add("Data", getData());
        object.addProperty("Version", getVersion());
        object.add("Deserializator", getDeserializator().serialize());

        return object;
    }
    public static @NotNull SerializedData deserialize(@NotNull JsonObject object) {
        try {
            JsonElement data = object.get("Data");
            int version = object.get("Version").getAsInt();
            MethodSupplier supplier = MethodSupplier.deserialize(object.getAsJsonObject("Deserializator"));

            return new SerializedData(data, version, supplier);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes the object using the deserializator method supplier
     * @return the object
     * @param <T> the object type
     */
    public <T> @NotNull T get(@Nullable Object instance) {
        return getDeserializator().invoke(instance, this);
    }
}
