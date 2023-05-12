package codes.laivy.mlanguage.api.bukkit.provider;

import codes.laivy.mlanguage.api.MessageSerializer;
import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.utils.ComponentUtils;
import com.google.gson.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class BukkitMessageSerializerProvider implements MessageSerializer<BaseComponent[], BukkitMessage, BukkitMessageStorage> {

    @Override
    public @NotNull JsonElement serializeComponent(@NotNull BaseComponent @NotNull [] component) {
        return JsonParser.parseString(ComponentUtils.serialize(component));
    }

    @Override
    public @NotNull BaseComponent @NotNull [] deserializeComponent(@NotNull JsonElement component) {
        return ComponentSerializer.parse(component.toString());
    }

    public @NotNull JsonElement serializeObject(@NotNull Object replace) {
        if (replace instanceof BukkitMessage) {
            return serializeMessage((BukkitMessage) replace);
        } else if (replace instanceof BaseComponent) {
            return JsonParser.parseString(ComponentUtils.serialize(new BaseComponent[] { (BaseComponent) replace }));
        } else if (replace instanceof BaseComponent[]) {
            return JsonParser.parseString(ComponentUtils.serialize((BaseComponent[]) replace));
        } else {
            return JsonParser.parseString("\"" + replace + "\"");
        }
    }

    public @NotNull Object deserializeObject(@NotNull JsonElement element) {
        try {
            return deserializeMessage(element); // Get as message
        } catch (IllegalArgumentException ignore) {
            try {
                return ComponentSerializer.parse(element.toString()); // Get as BaseComponent or BaseComponent[]
            } catch (JsonSyntaxException syntax) {
                return element.getAsString(); // Get as string
            }
        }
    }

    @Override
    public @NotNull JsonElement serializeMessage(@NotNull BukkitMessage message) {
        JsonObject object = new JsonObject();

        JsonObject data = new JsonObject();
        for (Map.Entry<Locale, BaseComponent[]> entry : message.getData().entrySet()) {
            data.add(entry.getKey().name(), serializeComponent(entry.getValue()));
        }

        JsonArray arrays = new JsonArray();
        for (@NotNull Locale array : message.getArrayTexts()) {
            arrays.add(array.name());
        }

        JsonArray legacies = new JsonArray();
        for (@NotNull Locale array : message.getLegacyTexts()) {
            legacies.add(array.name());
        }

        JsonArray replacements = new JsonArray();
        for (@NotNull Object replace : message.getReplacements()) {
            replacements.add(serializeObject(replace));
        }

        JsonArray prefixes = new JsonArray();
        for (@NotNull Object prefix : message.getPrefixes()) {
            prefixes.add(serializeObject(prefix));
        }

        JsonArray suffixes = new JsonArray();
        for (@NotNull Object suffix : message.getSuffixes()) {
            suffixes.add(serializeObject(suffix));
        }

        object.addProperty("id", message.getId());
        object.add("data", data);
        object.add("arrays", arrays);
        object.add("legacies", legacies);
        object.add("replacements", replacements);
        object.add("prefixes", prefixes);
        object.add("suffixes", suffixes);

        return object;
    }

    @Override
    public @NotNull BukkitMessage deserializeMessage(@NotNull JsonElement message) {
        try {
            JsonObject object = message.getAsJsonObject();

            Set<Locale> arrays = new LinkedHashSet<>();
            Set<Locale> legacies = new LinkedHashSet<>();

            Set<Object> replacements = new LinkedHashSet<>();
            Set<Object> prefixes = new LinkedHashSet<>();
            Set<Object> suffixes = new LinkedHashSet<>();
            Map<Locale, BaseComponent[]> data = new LinkedHashMap<>();

            for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject("data").entrySet()) {
                @NotNull Locale locale;
                try {
                    locale = Locale.valueOf(entry.getKey());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Couldn't find locale '" + entry.getKey() + "'", e);
                }

                data.put(locale, ComponentSerializer.parse(entry.getValue().toString()));
            }

            @NotNull String id = object.get("id").getAsString();

            for (JsonElement array : object.get("arrays").getAsJsonArray()) {
                arrays.add(Locale.valueOf(array.getAsString()));
            }
            for (JsonElement legacy : object.get("legacies").getAsJsonArray()) {
                legacies.add(Locale.valueOf(legacy.getAsString()));
            }

            for (JsonElement replacement : object.get("replacements").getAsJsonArray()) {
                replacements.add(deserializeObject(replacement));
            }
            for (JsonElement prefix : object.get("prefixes").getAsJsonArray()) {
                prefixes.add(deserializeObject(prefix));
            }
            for (JsonElement suffix : object.get("suffixes").getAsJsonArray()) {
                suffixes.add(deserializeObject(suffix));
            }

            return new BukkitMessageProvider(id, data, arrays, legacies, replacements, prefixes, suffixes);
        } catch (Throwable e) {
            throw new RuntimeException("This json '" + message + "' isn't a serialized message data", e);
        }
    }

    @Override
    public @NotNull JsonElement serializeStorage(@NotNull BukkitMessageStorage storage) {
        JsonObject object = new JsonObject();

        JsonArray messages = new JsonArray();
        for (@NotNull BukkitMessage message : storage.getMessages()) {
            messages.add(serializeMessage(message));
        }

        object.addProperty("name", storage.getName());
        object.addProperty("plugin", storage.getPluginProperty().getName());
        object.addProperty("default locale", storage.getDefaultLocale().name());
        object.add("messages", messages);

        return object;
    }

    @Override
    public @NotNull BukkitMessageStorage deserializeStorage(@NotNull JsonElement storage) {
        try {
            JsonObject object = storage.getAsJsonObject();
            JsonArray messagesArray = object.get("messages").getAsJsonArray();

            @NotNull String name = object.get("name").getAsString();
            @NotNull String pluginStr = object.get("plugin").getAsString();
            @NotNull Locale defaultLocale = Locale.valueOf(object.get("default locale").getAsString());
            @NotNull Set<BukkitMessage> messages = new LinkedHashSet<>();

            @Nullable Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginStr);

            if (plugin == null) {
                throw new NullPointerException("Couldn't find plugin '" + pluginStr + "'");
            }

            for (JsonElement messageElement : messagesArray) {
                messages.add(deserializeMessage(messageElement));
            }

            return new BukkitMessageStorageProvider(plugin, name, defaultLocale, messages);
        } catch (Throwable e) {
            throw new RuntimeException("This json '" + storage + "' isn't a serialized storage data", e);
        }
    }
}