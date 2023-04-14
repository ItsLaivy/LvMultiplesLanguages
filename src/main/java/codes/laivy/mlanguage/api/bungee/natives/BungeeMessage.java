package codes.laivy.mlanguage.api.bungee.natives;

import codes.laivy.mlanguage.api.bungee.IBungeeMessage;
import codes.laivy.mlanguage.api.bungee.IBungeeMessageStorage;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.utils.ComponentUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static codes.laivy.mlanguage.main.BungeeMultiplesLanguages.multiplesLanguagesBungee;

public class BungeeMessage implements IBungeeMessage {

    private final @NotNull IBungeeMessageStorage messageStorage;
    private final @NotNull String id;
    private final @NotNull Object[] replaces;

    private final @NotNull List<Object> prefixes;
    private final @NotNull List<Object> suffixes;

    public BungeeMessage(@NotNull IBungeeMessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        this(messageStorage, id, new LinkedList<>(), new LinkedList<>(), replaces);
    }
    public BungeeMessage(@NotNull IBungeeMessageStorage messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces) {
        this.messageStorage = messageStorage;
        this.id = id;
        this.replaces = replaces;

        this.prefixes = prefixes;
        this.suffixes = suffixes;
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
    public @NotNull Object[] getReplacements() {
        return replaces;
    }

    @Override
    @Unmodifiable
    public @NotNull List<@NotNull Object> getPrefixes() {
        return Collections.unmodifiableList(prefixes);
    }

    @Override
    @Unmodifiable
    public @NotNull List<@NotNull Object> getSuffixes() {
        return Collections.unmodifiableList(suffixes);
    }

    @Override
    public @NotNull SerializedData serialize() {
        try {
            // Data
            JsonArray replaces = new JsonArray();
            for (@NotNull Object replace : this.getReplacements()) {
                if (replace instanceof Message) {
                    //noinspection unchecked
                    replaces.add(((Message<BaseComponent>) replace).serialize().serialize());
                } else if (replace instanceof BaseComponent) {
                    replaces.add(ComponentUtils.serialize(new BaseComponent[] { (BaseComponent) replace }));
                } else if (replace instanceof BaseComponent[]) {
                    replaces.add(ComponentUtils.serialize((BaseComponent[]) replace));
                } else {
                    replaces.add(String.valueOf(replace));
                }
            }

            JsonObject language = new JsonObject();
            language.addProperty("Name", getStorage().getName());
            language.addProperty("Plugin", ((Plugin) getStorage().getPluginProperty()).getDescription().getName());

            JsonObject data = new JsonObject();
            data.add("Language", language);
            data.addProperty("Id", getId());
            data.add("Replaces", replaces);
            // Method serialization
            Method method = getClass().getDeclaredMethod("deserialize", SerializedData.class);
            method.setAccessible(true);
            // Serialized Data
            return new SerializedData(data, 0, method);
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

            IBungeeMessageStorage messageStorage = multiplesLanguagesBungee().getApi().getStorage(plugin, langName);
            if (messageStorage == null) {
                throw new NullPointerException("Couldn't found the language named '" + langName + "' from plugin '" + langPlugin + "'");
            }

            String id = data.get("Id").getAsString();
            Object[] replaces = new Object[data.get("Replaces").getAsJsonArray().size()];

            int row = 0;
            for (JsonElement replaceElement : data.get("Replaces").getAsJsonArray()) {
                if (replaceElement.isJsonObject()) {
                    JsonObject object = replaceElement.getAsJsonObject();

                    if (SerializedData.isSerializedData(object)) {
                        replaces[row] = SerializedData.deserialize(object).get();
                    } else {
                        replaces[row] = ComponentSerializer.parse(replaceElement.getAsJsonObject().toString());
                    }
                } else if (replaceElement.isJsonArray()) { // Json array, base component array.
                    replaces[row] = ComponentSerializer.parse(replaceElement.getAsJsonArray().toString());
                } else { // Json string
                    replaces[row] = replaceElement.getAsString();
                }

                row++;
            }

            return new BungeeMessage(messageStorage, id, replaces);
        } else {
            throw new IllegalArgumentException("This SerializedData version '" + serializedData.getVersion() + "' isn't compatible with this deserializator");
        }
    }
}
