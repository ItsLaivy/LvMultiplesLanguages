package codes.laivy.mlanguage.api.bungee.natives;

import codes.laivy.mlanguage.api.bungee.IBungeeMessage;
import codes.laivy.mlanguage.api.bungee.IBungeeMessageStorage;
import codes.laivy.mlanguage.data.MethodSupplier;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Map;

import static codes.laivy.mlanguage.main.BungeeMultiplesLanguages.multiplesLanguagesBungee;

public class BungeeMessage implements IBungeeMessage {

    private final @NotNull IBungeeMessageStorage messageStorage;
    private final @NotNull String id;
    private final @NotNull Message[] replaces;

    public BungeeMessage(@NotNull IBungeeMessageStorage messageStorage, @NotNull String id, @NotNull Message... replaces) {
        this.messageStorage = messageStorage;
        this.id = id;
        this.replaces = replaces;
    }

    @Override
    public @NotNull IBungeeMessageStorage getStorage() {
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
            language.addProperty("Plugin", ((Plugin) getStorage().getPlugin()).getDescription().getName());

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
            throw new RuntimeException("BungeeMessage serialization", e);
        }
    }

    public static @NotNull BungeeMessage deserialize(@NotNull SerializedData serializedData) {
        if (serializedData.getVersion() == 0) {
            JsonObject data = serializedData.getData().getAsJsonObject();

            JsonObject languageData = data.get("Language").getAsJsonObject();
            String langName = languageData.get("Name").getAsString();
            String langPlugin = languageData.get("Plugin").getAsString();

            Plugin plugin = ProxyServer.getInstance().getPluginManager().getPlugin(langPlugin);
            if (plugin == null) {
                throw new NullPointerException("Couldn't find the plugin '" + langPlugin + "'");
            }

            MessageStorage messageStorage = multiplesLanguagesBungee().getApi().getStorage(plugin, langName);
            if (messageStorage == null) {
                throw new NullPointerException("Couldn't found the language named '" + langName + "' from plugin '" + langPlugin + "'");
            } else if (!(messageStorage instanceof IBungeeMessageStorage)) {
                throw new IllegalArgumentException("This message storage isn't a bukkit message storage!");
            }

            String id = data.get("Id").getAsString();
            IBungeeMessage[] replaces = new BungeeMessage[data.get("Replaces").getAsJsonArray().size()];

            int row = 0;
            for (JsonElement replaceElement : data.get("Replaces").getAsJsonArray()) {
                replaces[row] = SerializedData.deserialize(replaceElement.getAsJsonObject()).get(null);
                row++;
            }

            return new BungeeMessage((IBungeeMessageStorage) messageStorage, id, replaces);
        } else {
            throw new IllegalArgumentException("This SerializedData version '" + serializedData.getVersion() + "' isn't compatible with this deserializator");
        }
    }
}
