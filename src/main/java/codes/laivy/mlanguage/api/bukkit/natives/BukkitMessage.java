package codes.laivy.mlanguage.api.bukkit.natives;

import codes.laivy.mlanguage.api.bukkit.IBukkitMessage;
import codes.laivy.mlanguage.api.bukkit.IBukkitMessageStorage;
import codes.laivy.mlanguage.data.MethodSupplier;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Map;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class BukkitMessage implements IBukkitMessage {

    private final @NotNull IBukkitMessageStorage messageStorage;
    private final @NotNull String id;
    private final @NotNull Message[] replaces;

    public BukkitMessage(@NotNull IBukkitMessageStorage messageStorage, @NotNull String id, @NotNull Message... replaces) {
        this.messageStorage = messageStorage;
        this.id = id;
        this.replaces = replaces;
    }

    @Override
    public @NotNull IBukkitMessageStorage getStorage() {
        return messageStorage;
    }

    @Override
    public @NotNull Map<@NotNull Locale, @NotNull BaseComponent[]> getData() {
        return getStorage().getData().get(getId());
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull Message[] getReplacements() {
        return replaces;
    }

    @Override
    public @NotNull SerializedData serialize() {
        try {
            // Data
            JsonArray replaces = new JsonArray();
            for (@NotNull Message replace : this.getReplacements()) {
                replaces.add(replace.serialize().serialize());
            }

            JsonObject language = new JsonObject();
            language.addProperty("Name", getStorage().getName());
            language.addProperty("Plugin", ((Plugin) getStorage().getPlugin()).getName());

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

            MessageStorage messageStorage = multiplesLanguagesBukkit().getApi().getStorage(plugin, langName);
            if (messageStorage == null) {
                throw new NullPointerException("Couldn't found the language named '" + langName + "' from plugin '" + langPlugin + "'");
            } else if (!(messageStorage instanceof IBukkitMessageStorage)) {
                throw new IllegalArgumentException("This message storage isn't a bukkit message storage!");
            }

            String id = data.get("Id").getAsString();
            BukkitMessage[] replaces = new BukkitMessage[data.get("Replaces").getAsJsonArray().size()];

            int row = 0;
            for (JsonElement replaceElement : data.get("Replaces").getAsJsonArray()) {
                replaces[row] = SerializedData.deserialize(replaceElement.getAsJsonObject()).get(null);
                row++;
            }

            return new BukkitMessage((IBukkitMessageStorage) messageStorage, id, replaces);
        } else {
            throw new IllegalArgumentException("This SerializedData version '" + serializedData.getVersion() + "' isn't compatible with this deserializator");
        }
    }

}
