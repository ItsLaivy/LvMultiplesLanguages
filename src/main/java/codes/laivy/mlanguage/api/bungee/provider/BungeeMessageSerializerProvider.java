package codes.laivy.mlanguage.api.bungee.provider;

import codes.laivy.mlanguage.api.MessageSerializer;
import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.BungeeMessageStorage;
import codes.laivy.mlanguage.exceptions.PluginNotFoundException;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.JsonUtils;
import com.google.gson.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BungeeMessageSerializerProvider implements MessageSerializer<BaseComponent[], BungeeMessage, BungeeMessageStorage> {

    @Override
    public @NotNull JsonElement serializeComponent(@NotNull BaseComponent @NotNull [] component) {
        return JsonParser.parseString(ComponentUtils.serialize(component));
    }

    @Override
    public @NotNull BaseComponent @NotNull [] deserializeComponent(@NotNull JsonElement component) {
        return ComponentSerializer.parse(component.toString());
    }

    public @NotNull JsonElement serializeObject(@NotNull Object replace) {
        if (replace instanceof BungeeMessage) {
            return serializeMessage((BungeeMessage) replace);
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
    public @NotNull JsonElement serializeMessage(@NotNull BungeeMessage message) {
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
    public @NotNull BungeeMessage deserializeMessage(@NotNull JsonElement message) {
        try {
            JsonObject object = message.getAsJsonObject();

            Set<Locale> arrays = new LinkedHashSet<>();
            Set<Locale> legacies = new LinkedHashSet<>();

            List<Object> replacements = new LinkedList<>();
            List<Object> prefixes = new LinkedList<>();
            List<Object> suffixes = new LinkedList<>();
            Map<Locale, BaseComponent[]> data = new LinkedHashMap<>();

            for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject("data").entrySet()) {
                @NotNull Locale locale;
                try {
                    locale = Locale.valueOf(entry.getKey().toUpperCase());
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

            return new BungeeMessageProvider(id, data, arrays, legacies, replacements, prefixes, suffixes);
        } catch (Throwable e) {
            throw new RuntimeException("This json '" + message + "' isn't a serialized message data", e);
        }
    }

    @Override
    public @NotNull JsonElement serializeStorage(@NotNull BungeeMessageStorage storage) {
        JsonObject object = new JsonObject();
        JsonObject messages = new JsonObject();

        for (BungeeMessage message : storage.getMessages()) {
            JsonObject messageObj = new JsonObject();
            JsonObject contentObj = new JsonObject();

            JsonArray prefixes = new JsonArray();
            JsonArray suffixes = new JsonArray();
            JsonArray replacements = new JsonArray();

            for (Map.Entry<@NotNull Locale, BaseComponent @NotNull []> entry : message.getData().entrySet()) {
                Locale locale = entry.getKey();
                BaseComponent[] component = entry.getValue();

                if (message.isArrayText(locale)) { // Is array
                    JsonArray array = new JsonArray();
                    for (BaseComponent line : component) {
                        if (message.isLegacyText(locale)) {
                            array.add(ComponentUtils.getText(line));
                        } else {
                            array.add(ComponentUtils.serialize(line));
                        }
                    }
                    contentObj.add(locale.name(), array);
                } else { // Not array
                    if (message.isLegacyText(locale)) {
                        contentObj.addProperty(locale.name(), ComponentUtils.getText(component));
                    } else {
                        contentObj.addProperty(locale.name(), ComponentUtils.serialize(component));
                    }
                }
            }

            messageObj.add("content", contentObj);

            for (Object prefix : message.getPrefixes()) {
                prefixes.add(serializeObject(prefix));
            }
            for (Object suffix : message.getSuffixes()) {
                suffixes.add(serializeObject(suffix));
            }
            for (Object replacement : message.getReplacements()) {
                replacements.add(serializeObject(replacement));
            }

            if (prefixes.size() > 0) {
                messageObj.add("prefixes", prefixes);
            } if (suffixes.size() > 0) {
                messageObj.add("suffixes", suffixes);
            } if (replacements.size() > 0) {
                messageObj.add("replacements", replacements);
            }

            messages.add(message.getId(), messageObj);
        }

        object.addProperty("name", storage.getName());
        object.addProperty("plugin", storage.getPluginProperty().getName());
        object.addProperty("default locale", storage.getDefaultLocale().name());
        object.add("messages", messages);

        return object;
    }

    @Override
    public @NotNull BungeeMessageStorage deserializeStorage(@NotNull JsonElement storage) {
        try {
            JsonObject object = storage.getAsJsonObject();

            String name = object.get("name").getAsString();
            String pluginStr = object.get("plugin").getAsString();
            Locale defaultLocale = Locale.valueOf(object.get("default locale").getAsString().toUpperCase());
            JsonObject messagesObject = object.getAsJsonObject("messages");

            Plugin plugin = ProxyServer.getInstance().getPluginManager().getPlugin(pluginStr);
            if (plugin == null) {
                throw new PluginNotFoundException(pluginStr);
            }

            Set<BungeeMessage> messages = new LinkedHashSet<BungeeMessage>() {{
                for (Map.Entry<String, JsonElement> entry : messagesObject.entrySet()) {
                    String id = entry.getKey();
                    JsonObject messagesObj = entry.getValue().getAsJsonObject();
                    JsonObject contentObj = messagesObj.getAsJsonObject("content");

                    Set<Locale> legacies = new LinkedHashSet<>();
                    Set<Locale> arrays = new LinkedHashSet<>();

                    Map<Locale, BaseComponent[]> data = new LinkedHashMap<>();
                    for (Map.Entry<String, JsonElement> entry2 : contentObj.entrySet()) {
                        Locale locale;
                        try {
                            locale = Locale.valueOf(entry2.getKey().toUpperCase());
                        } catch (IllegalArgumentException ignore) {
                            throw new IllegalArgumentException("Couldn't find a locale named '" + entry2.getKey() + "'");
                        }
                        JsonElement base = entry2.getValue();

                        if (base.isJsonArray()) { // Is array
                            List<BaseComponent> array = new LinkedList<>();
                            for (JsonElement line : base.getAsJsonArray()) {
                                String jsonStr = ChatColor.translateAlternateColorCodes('&', line.getAsString());

                                if (!(line.isJsonNull() || jsonStr.equals("")) && JsonUtils.isJson(jsonStr)) { // Check if is not legacy
                                    array.add(new TextComponent(ComponentSerializer.parse(jsonStr)));
                                    continue;
                                }

                                array.add(new TextComponent(jsonStr));

                                // Declare legacy text
                                legacies.add(locale);
                            }
                            data.put(locale, array.toArray(new BaseComponent[0]));

                            // Declare array text
                            arrays.add(locale);
                        } else { // Not array
                            BaseComponent[] text;

                            String jsonStr = ChatColor.translateAlternateColorCodes('&', base.getAsString());
                            if (JsonUtils.isJson(jsonStr)) {
                                text = ComponentSerializer.parse(jsonStr);
                            } else {
                                text = new BaseComponent[]{new TextComponent(jsonStr)};

                                // Declare legacy text
                                legacies.add(locale); // Legacy text
                            }

                            data.put(locale, text);
                        }
                    }

                    BungeeMessage message = new BungeeMessageProvider(id, data);

                    message.getLegacyTexts().addAll(legacies);
                    message.getArrayTexts().addAll(arrays);

                    if (messagesObj.has("prefixes")) {
                        List<Object> prefixes = new LinkedList<>();
                        JsonArray prefixesObj = messagesObj.getAsJsonArray("prefixes");
                        for (JsonElement prefixElement : prefixesObj) {
                            prefixes.add(deserializeObject(prefixElement));
                        }
                        message.getPrefixes().addAll(prefixes);
                    }
                    if (messagesObj.has("suffixes")) {
                        List<Object> suffixes = new LinkedList<>();
                        JsonArray suffixesObj = messagesObj.getAsJsonArray("suffixes");
                        for (JsonElement suffixElement : suffixesObj) {
                            suffixes.add(deserializeObject(suffixElement));
                        }
                        message.getSuffixes().addAll(suffixes);
                    }
                    if (messagesObj.has("replacements")) {
                        List<Object> replaces = new LinkedList<>();
                        JsonArray replacesObj = messagesObj.getAsJsonArray("replacements");
                        for (JsonElement replaceElement : replacesObj) {
                            replaces.add(deserializeObject(replaceElement));
                        }
                        message.getReplacements().addAll(replaces);
                    }

                    add(message);
                }
            }};

            return new BungeeMessageStorageProvider(plugin, name, defaultLocale, messages);
        } catch (PluginNotFoundException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException("This json '" + storage + "' isn't a serialized storage data", e);
        }
    }
}
