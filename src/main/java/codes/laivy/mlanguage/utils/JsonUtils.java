package codes.laivy.mlanguage.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.NotNull;

import java.io.StringReader;

public class JsonUtils {

    public static boolean isJson(@NotNull String json) {
        try {
            JsonReader reader = new JsonReader(new StringReader(json));
            reader.setLenient(false);
            Streams.parse(reader);

            JsonElement element = JsonParser.parseString(json);
            if (element.isJsonObject() || element.isJsonArray()) {
                return true;
            }
        } catch (JsonSyntaxException ignore) {
        }
        return false;
    }

}
