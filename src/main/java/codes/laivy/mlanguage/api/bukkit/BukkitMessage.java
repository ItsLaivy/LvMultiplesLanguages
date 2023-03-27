package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.data.MethodSupplier;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class BukkitMessage implements Message {

    private final @NotNull MessageStorage messageStorage;
    private final @NotNull String id;
    private final @NotNull BukkitMessage[] replaces;

    public BukkitMessage(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull BukkitMessage... replaces) {
        this.messageStorage = messageStorage;
        this.id = id;
        this.replaces = replaces;
    }

    @Override
    public @NotNull MessageStorage getLanguage() {
        return messageStorage;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull BukkitMessage[] getReplaces() {
        return replaces;
    }

    @Override
    public @NotNull BaseComponent[] get(@Nullable Locale locale) {
        return messageStorage.get(locale, getId(), getReplaces(locale));
    }

    @Override
    public @NotNull SerializedData serialize() {
        try {
            // Data
            JsonArray replaces = new JsonArray();
            for (@NotNull BukkitMessage replace : getReplaces()) {
                replaces.add(replace.serialize().serialize());
            }

            JsonObject language = new JsonObject();
            language.addProperty("Name", getLanguage().getName());
            language.addProperty("Plugin", ((Plugin) getLanguage().getPlugin()).getName());

            JsonObject data = new JsonObject();
            data.add("Language", language);
            data.addProperty("Id", getId());
            data.add("Replaces", replaces);
            // Method serialization
            Method method = getClass().getDeclaredMethod("deserialize", SerializedData.class);
            method.setAccessible(true);
            // Serialized Data
            return new SerializedData(data, 0, new MethodSupplier(method));
        } catch (Throwable e) {
            throw new RuntimeException("BukkitMessage serialization", e);
        }
    }

    public static @NotNull BukkitMessage deserialize(@NotNull SerializedData serializedData) {
        if (serializedData.getVersion() == 0) {
            JsonObject data = serializedData.getData().getAsJsonObject();

            JsonObject languageData = data.get("Language").getAsJsonObject();
            String langName = languageData.get("Name").getAsString();
            String langPlugin = languageData.get("Plugin").getAsString();

            Plugin plugin = Bukkit.getPluginManager().getPlugin(langPlugin);
            if (plugin == null) {
                throw new NullPointerException("Couldn't find the plugin '" + langPlugin + "'");
            }

            MessageStorage messageStorage = multiplesLanguagesBukkit().getApi().getLanguage(langName, plugin);
            if (messageStorage == null) {
                throw new NullPointerException("Couldn't found the language named '" + langName + "' from plugin '" + langPlugin + "'");
            }

            String id = data.get("Id").getAsString();
            BukkitMessage[] replaces = new BukkitMessage[data.get("Replaces").getAsJsonArray().size()];

            int row = 0;
            for (JsonElement replaceElement : data.get("Replaces").getAsJsonArray()) {
                replaces[row] = SerializedData.deserialize(replaceElement.getAsJsonObject()).get(null);
                row++;
            }

            return new BukkitMessage(messageStorage, id, replaces);
        } else {
            throw new IllegalArgumentException("This SerializedData version '" + serializedData.getVersion() + "' isn't compatible with this deserializator");
        }
    }

}
