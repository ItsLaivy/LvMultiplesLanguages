package codes.laivy.mlanguage.config;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class JsonConfigUtils {

    public static boolean saveResource(String resourceName, Path path) {
        return saveResource(resourceName, path, false);
    }
    public static boolean saveResource(String resourceName, Path path, boolean replace) {
        try (@Nullable InputStream stream = LvMultiplesLanguages.class.getResourceAsStream(resourceName)) {
            if (stream != null) {
                File file = new File(path.toFile(), resourceName);
                if (file.exists()) {
                    if (!replace) return false;
                } else {
                    if (!file.createNewFile()) {
                        throw new IllegalStateException("Couldn't create '" + file + "'.");
                    }
                }

                JsonObject resourceConfig = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
                Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.write(gson.toJson(resourceConfig));
                    return true;
                }
            } else {
                throw new NullPointerException("Cannot get this resource");
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot get the default configuration file", e);
        }
    }

    public static @NotNull JsonElement safeGet(String resourceName, Path path) throws JsonSyntaxException {
        try {
             return get(resourceName, path);
        } catch (JsonSyntaxException ignore) {
            saveResource(resourceName, path, true);
        }
        return get(resourceName, path);
    }
    public static @NotNull JsonElement get(String resourceName, Path path) throws JsonSyntaxException {
        saveResource(resourceName, path, false);

        File file = new File(path.toFile(), resourceName);
        try {
            return JsonParser.parseReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
