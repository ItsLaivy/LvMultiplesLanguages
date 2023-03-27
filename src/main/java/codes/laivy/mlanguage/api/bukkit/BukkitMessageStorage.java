package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.data.MethodSupplier;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.lang.Locale;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class BukkitMessageStorage implements MessageStorage {

    private final @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components;
    private final @NotNull String name;
    private final @NotNull Plugin plugin;

    private final @NotNull Locale defaultLocale;

    public BukkitMessageStorage(@NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components, @NotNull String name, @NotNull Plugin plugin) {
        this.defaultLocale = defaultLocale;
        this.components = components;
        this.name = name;
        this.plugin = plugin;

        for (Map.Entry<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> entry : getComponents().entrySet()) {
            if (!entry.getValue().containsKey(getDefaultLocale())) {
                throw new IllegalStateException("Couldn't find the default locale (" + getDefaultLocale().name() + ") translation for code '" + entry.getKey() + "'");
            }
        }
    }

    @Override
    public @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        locale = (locale == null ? getDefaultLocale() : locale);

        if (getComponents().containsKey(id)) {
            BaseComponent[] component;
            if (getComponents().get(id).containsKey(locale)) {
                component = getComponents().get(id).get(locale);
            } else if (getComponents().get(id).containsKey(getDefaultLocale())) {
                component = getComponents().get(id).get(getDefaultLocale());
            } else {
                throw new NullPointerException("This message id '" + id + "' at language named '" + getName() + "' from plugin '" + getPlugin() + "' doesn't exists at this locale '" + locale.name() + "', and not exists on the default locale too '" + getDefaultLocale().name() + "'");
            }

            // TODO: 23/03/2023 Replaces
            return component;
        } else {
            throw new NullPointerException("Couldn't find the message id '" + id + "' at language named '" + getName() + "' from plugin '" + getPlugin() + "'");
        }
    }

    @Override
    public @NotNull Locale getDefaultLocale() {
        return defaultLocale;
    }

    public @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> getComponents() {
        return components;
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
    public void unload() {
        // TODO: 23/03/2023 Language unloading
    }

    @Override
    public @NotNull SerializedData serialize() {
        try {
            // Data
            JsonObject data = new JsonObject();
            data.addProperty("Default locale", getDefaultLocale().getCode());
            data.addProperty("Plugin", getPlugin().getName());
            data.addProperty("Name", getName());
            // Method serialization
            Method method = getClass().getDeclaredMethod("deserialize", SerializedData.class);
            method.setAccessible(true);
            // Serialized Data
            return new SerializedData(data, 0, new MethodSupplier(method));
        } catch (Throwable e) {
            throw new RuntimeException("BukkitMessage serialization", e);
        }
    }

    public static @NotNull BukkitMessageStorage deserialize(@NotNull SerializedData serializedData) {
        if (serializedData.getVersion() == 0) {
            JsonObject data = serializedData.getData().getAsJsonObject();

            Locale defaultLocale = Locale.valueOf(data.get("Default locale").getAsString());
            String name = data.get("Name").getAsString();
            String pluginName = data.get("Plugin").getAsString();

            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (plugin == null) {
                throw new NullPointerException("Couldn't find the plugin '" + pluginName + "'");
            }

            @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components = new LinkedHashMap<>();
            // TODO: 27/03/2023 this
//            for (JsonElement element : data.get("Components").getAsJsonArray()) {
//                JsonObject componentJson = element.getAsJsonObject();
//                for (Map.Entry<String, JsonElement> entry : componentJson.entrySet()) {
//                    components.put(entry.getKey(), mergeBaseComponents(ComponentSerializer.parse(entry.getValue().toString())));
//                }
//            }

            return new BukkitMessageStorage(defaultLocale, components, name, plugin);
        } else {
            throw new IllegalArgumentException("This SerializedData version '" + serializedData.getVersion() + "' isn't compatible with this deserializator");
        }
    }

    public static @NotNull TextComponent mergeBaseComponents(BaseComponent[] components) {
        TextComponent mergedComponent = new TextComponent("");
        for (BaseComponent component : components) {
            mergedComponent.addExtra(component);
        }
        return mergedComponent;
    }

}
