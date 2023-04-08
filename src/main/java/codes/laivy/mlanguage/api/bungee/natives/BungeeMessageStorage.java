package codes.laivy.mlanguage.api.bungee.natives;

import codes.laivy.mlanguage.api.bungee.IBungeeMessage;
import codes.laivy.mlanguage.api.bungee.IBungeeMessageStorage;
import codes.laivy.mlanguage.data.MethodSupplier;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.utils.ComponentUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class BungeeMessageStorage implements IBungeeMessageStorage {

    private final @NotNull Plugin plugin;
    private final @NotNull String name;
    private final @NotNull Locale defaultLocale;
    private final @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components;

    public BungeeMessageStorage(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components) {
        this.plugin = plugin;
        this.name = name;
        this.defaultLocale = defaultLocale;
        this.components = components;
    }

    @Override
    public @NotNull IBungeeMessage[] getMessages() {
        Set<IBungeeMessage> bungeeMessages = new LinkedHashSet<>();
        for (String id : getData().keySet()) {
            bungeeMessages.add(new BungeeMessage(this, id));
        }
        return bungeeMessages.toArray(new IBungeeMessage[0]);
    }

    @Override
    public @NotNull Map<@NotNull String, Map<@NotNull Locale, @NotNull BaseComponent[]>> getData() {
        return components;
    }

    @Override
    public @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        locale = (locale == null ? getDefaultLocale() : locale);

        if (getData().containsKey(id)) {
            BaseComponent[] components;
            if (getData().get(id).containsKey(locale)) {
                components = getData().get(id).get(locale);
            } else if (getData().get(id).containsKey(getDefaultLocale())) {
                components = getData().get(id).get(getDefaultLocale());
            } else {
                throw new NullPointerException("This message id '" + id + "' at message storage named '" + getName() + "' from plugin '" + getPlugin() + "' doesn't exists at this locale '" + locale.name() + "', and not exists on the default locale too '" + getDefaultLocale().name() + "'");
            }

            for (BaseComponent component : components) {
                if (component instanceof TextComponent) {
                    component.duplicate();

                    TextComponent text = (TextComponent) component;
                    text.setText(replace(locale, ComponentUtils.getText(text), replaces));
                }
                if (component.getExtra() != null) {
                    for (BaseComponent extra : component.getExtra()) {
                        extra = extra.duplicate();

                        if (extra instanceof TextComponent) {
                            TextComponent text = (TextComponent) extra;
                            text.setText(replace(locale, ComponentUtils.getText(text), replaces));
                        }
                    }
                }
            }

            return components;
        } else {
            throw new NullPointerException("Couldn't find the message id '" + id + "' at message storage named '" + getName() + "' from plugin '" + getPlugin() + "'");
        }
    }

    @Override
    public @NotNull IBungeeMessage get(@NotNull String id, @NotNull Message... replaces) {
        return new BungeeMessage(this, id, replaces);
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull Locale getDefaultLocale() {
        return defaultLocale;
    }

    @Override
    public @NotNull SerializedData serialize() {
        try {
            // Data
            JsonObject data = new JsonObject();
            data.addProperty("Default locale", getDefaultLocale().getCode());
            data.addProperty("Plugin", getPlugin().getDescription().getName());
            data.addProperty("Name", getName());
            // Components
            JsonObject components = new JsonObject();
            for (Map.Entry<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> entry : getData().entrySet()) {
                JsonObject localizedComponents = new JsonObject();

                for (Map.Entry<Locale, @NotNull BaseComponent[]> entry2 : entry.getValue().entrySet()) {
                    String message = ComponentUtils.serialize(entry2.getValue()).toString();
                    if (message != null) {
                        localizedComponents.addProperty(entry2.getKey().name(), message);
                    }
                }

                components.add(entry.getKey(), localizedComponents);
            }
            data.add("Components", components);
            // Method serialization
            Method method = getClass().getDeclaredMethod("deserialize", SerializedData.class);
            method.setAccessible(true);
            // Serialized Data
            return new SerializedData(data, 0, new MethodSupplier(method));
        } catch (Throwable e) {
            throw new RuntimeException("BungeeMessageStorage serialization", e);
        }
    }

    public static @NotNull BungeeMessageStorage deserialize(@NotNull SerializedData serializedData) {
        if (serializedData.getVersion() == 0) {
            JsonObject data = serializedData.getData().getAsJsonObject();

            Locale defaultLocale = Locale.valueOf(data.get("Default locale").getAsString().toUpperCase());
            String name = data.get("Name").getAsString();
            String pluginName = data.get("Plugin").getAsString();

            Plugin plugin = ProxyServer.getInstance().getPluginManager().getPlugin(pluginName);
            if (plugin == null) {
                throw new NullPointerException("Couldn't find the plugin '" + pluginName + "'");
            }

            @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : data.get("Components").getAsJsonObject().asMap().entrySet()) {
                String key = entry.getKey();
                Map<Locale, BaseComponent[]> localizedComponents = new LinkedHashMap<>();

                for (Map.Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().asMap().entrySet()) {
                    Locale locale;
                    try {
                        locale = Locale.valueOf(entry2.getKey().toUpperCase());
                    } catch (IllegalArgumentException ignore) {
                        throw new IllegalArgumentException("Couldn't find a locale named '" + entry2.getKey() + "'");
                    }

                    localizedComponents.put(locale, ComponentSerializer.parse(entry2.getValue().toString()));
                }

                components.put(key, localizedComponents);
            }

            return new BungeeMessageStorage(plugin, name, defaultLocale, components);
        } else {
            throw new IllegalArgumentException("This SerializedData version '" + serializedData.getVersion() + "' isn't compatible with this deserializator");
        }
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
        getData().clear();
    }

    @Override
    public boolean merge(@NotNull MessageStorage from) {
        boolean changes = false;

        f1:
        for (Message fromMessage : from.getMessages()) {
            for (Message toMessage : this.getMessages()) {
                if (toMessage.getId().equals(fromMessage.getId())) {
                    continue f1;
                }
            }

            this.getData().put(fromMessage.getId(), new LinkedHashMap<Locale, BaseComponent[]>() {{
                putAll(fromMessage.getData());
            }});
            changes = true;
        }

        return changes;
    }
}
