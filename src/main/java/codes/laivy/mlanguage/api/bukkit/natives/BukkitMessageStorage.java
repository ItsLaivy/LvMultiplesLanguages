package codes.laivy.mlanguage.api.bukkit.natives;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.api.bukkit.IBukkitArrayMessage;
import codes.laivy.mlanguage.api.bukkit.IBukkitMessage;
import codes.laivy.mlanguage.api.bukkit.IBukkitMessageStorage;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.plugin.BukkitPluginProperty;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Method;
import java.util.*;

public class BukkitMessageStorage implements IBukkitMessageStorage {

    @ApiStatus.Internal
    private final @NotNull Map<@NotNull String, @NotNull Map<@NotNull Locale, @NotNull BaseComponent[][]>> components;
    private final @NotNull String name;
    private final @NotNull BukkitPluginProperty plugin;

    private final @NotNull Locale defaultLocale;

    private final @NotNull Map<Locale, Set<String>> legaciesTexts;
    private final @NotNull Map<Locale, Set<String>> arrayTexts;

    protected BukkitMessageStorage(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[][]>> components, @NotNull Map<Locale, Set<String>> legaciesTexts, @NotNull Map<Locale, Set<String>> arrayTexts) {
        this.legaciesTexts = legaciesTexts;
        this.arrayTexts = arrayTexts;

        this.plugin = new BukkitPluginProperty(plugin);
        this.name = name;
        this.defaultLocale = defaultLocale;
        this.components = components;

        // Check if the components have the default locale messages
        for (String key : getData().keySet()) {
            if (!getData().get(key).containsKey(getDefaultLocale())) {
                throw new IllegalStateException("Couldn't find the default locale (" + getDefaultLocale().name() + ") translation for message id '" + key + "'");
            }
        }
    }
    public BukkitMessageStorage(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[][]>> components) {
        this(plugin, name, defaultLocale, components, new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    @Override
    public @NotNull BaseComponent[] getText(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        locale = (locale == null ? getDefaultLocale() : locale);

        if (getData().containsKey(id)) {
            if (!getData().get(id).containsKey(locale)) {
                locale = getDefaultLocale();
            }

            BaseComponent[] components;
            if (getData().get(id).containsKey(locale)) {
                components = getData().get(id).get(locale);
            } else {
                throw new NullPointerException("This message id '" + id + "' at message storage named '" + getName() + "' from plugin '" + getPluginProperty().getName() + "' doesn't exists at this locale '" + locale.name() + "', and not exists on the default locale too '" + getDefaultLocale().name() + "'");
            }

            for (BaseComponent component : components) {
                component = component.duplicate();

                if (component instanceof TextComponent) {
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
            throw new NullPointerException("Couldn't find the message id '" + id + "' at message storage named '" + getName() + "' from plugin '" + getPluginProperty().getName() + "'");
        }
    }

    @Override
    public @NotNull BaseComponent[] getText(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces) {
        if (LvMultiplesLanguages.getApi() != null) {
            return this.getText(LvMultiplesLanguages.getApi().getLocale(uuid), id, replaces);
        }
        throw new NullPointerException("Couldn't find the multiples languages API");
    }

    @Override
    public @NotNull List<@NotNull BaseComponent[]> getTextArray(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces) {
        locale = (locale == null ? getDefaultLocale() : locale);

        if (getData().containsKey(id)) {
            if (!getData().get(id).containsKey(locale)) {
                locale = getDefaultLocale();
            }

            List<BaseComponent[]> components = new LinkedList<>();
            BaseComponent[] componentArray;

            if (getData().get(id).containsKey(locale)) {
                componentArray = getData().get(id).get(locale);
            } else {
                throw new NullPointerException("This message id '" + id + "' at message storage named '" + getName() + "' from plugin '" + getPluginProperty().getName() + "' doesn't exists at this locale '" + locale.name() + "', and not exists on the default locale too '" + getDefaultLocale().name() + "'");
            }

            if (!isArray(id, locale)) {
                throw new UnsupportedOperationException("This text with id '" + id + "' and locale '" + locale.name() + "' isn't an array text, use #getText instead.");
            }

            for (BaseComponent component : componentArray) {
                components.add(new BaseComponent[] {
                        component
                });
            }

            return components;
        } else {
            throw new NullPointerException("Couldn't find the message id '" + id + "' at message storage named '" + getName() + "' from plugin '" + getPluginProperty().getName() + "'");
        }
    }

    @Override
    public @NotNull List<@NotNull BaseComponent[]> getTextArray(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces) {
        if (LvMultiplesLanguages.getApi() != null) {
            return this.getTextArray(LvMultiplesLanguages.getApi().getLocale(uuid), id, replaces);
        }
        throw new NullPointerException("Couldn't find the multiples languages API");
    }

    @Override
    public @NotNull IBukkitMessage getMessage(@NotNull String id, @NotNull Object... replaces) {
        return new BukkitMessage(this, id, replaces);
    }

    @Override
    public @NotNull IBukkitArrayMessage getMessageArray(@NotNull String id, @NotNull Object... replaces) {
        return new BukkitArrayMessage(this, id, replaces);
    }

    @Override
    public @NotNull Locale getDefaultLocale() {
        return defaultLocale;
    }

    @Override
    public boolean isArray(@NotNull String id, @Nullable Locale locale) {
        if (components.containsKey(id)) {
            if (locale == null || !components.get(id).containsKey(locale)) {
                locale = getDefaultLocale();
            }

            if (components.get(id).containsKey(locale)) {
                return getArrayTexts().containsKey(locale) && getArrayTexts().get(locale).contains(id);
            }
        }
        throw new NullPointerException("Couldn't find a component with this id '" + id + "' at this message storage '" + getName() + "' of plugin '" + getPluginProperty().getName() + "'");
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull BukkitPluginProperty getPluginProperty() {
        return plugin;
    }

    @Override
    public @NotNull SerializedData serialize() {
        try {
            // Data
            JsonObject data = new JsonObject();
            data.addProperty("Default locale", getDefaultLocale().getCode());
            data.addProperty("Plugin", getPluginProperty().getName());
            data.addProperty("Name", getName());
            // Components
            JsonObject components = new JsonObject();
            for (Map.Entry<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> entry : getData().entrySet()) {
                JsonObject componentObj = new JsonObject();

                String id = entry.getKey();
                for (Map.Entry<Locale, @NotNull BaseComponent[]> entry2 : entry.getValue().entrySet()) {
                    Locale locale = entry2.getKey();
                    BaseComponent[] component = entry2.getValue();

                    if (isArray(id, locale)) { // Is array
                        JsonArray array = new JsonArray();
                        for (BaseComponent line : component) {
                            if (isLegacyText(id, locale)) {
                                array.add(ComponentUtils.getText(line));
                            } else {
                                array.add(ComponentUtils.serialize(line));
                            }
                        }
                        componentObj.add(locale.name(), array);
                    } else { // Not array
                        if (isLegacyText(id, locale)) {
                            componentObj.addProperty(locale.name(), ComponentUtils.getText(component));
                        } else {
                            componentObj.addProperty(locale.name(), ComponentUtils.serialize(component));
                        }
                    }

                }

                components.add(entry.getKey(), componentObj);
            }
            data.add("Components", components);
            // Method serialization
            Method method = getClass().getDeclaredMethod("deserialize", SerializedData.class);
            method.setAccessible(true);
            // Serialized Data
            return new SerializedData(data, 0, method);
        } catch (Throwable e) {
            throw new RuntimeException("BukkitMessageStorage serialization", e);
        }
    }

    /**
     * The default deserializator of Multiples Language's default API
     *
     * @param serializedData the serialized data
     * @return the storage deserialized
     */
    @ApiStatus.Internal
    public static @NotNull BukkitMessageStorage deserialize(@NotNull SerializedData serializedData) {
        if (serializedData.getVersion() == 0) {
            JsonObject data = serializedData.getData().getAsJsonObject();

            Locale defaultLocale = Locale.valueOf(data.get("Default locale").getAsString().toUpperCase());
            String name = data.get("Name").getAsString();
            String pluginName = data.get("Plugin").getAsString();

            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (plugin == null) {
                throw new NullPointerException("Couldn't find the plugin '" + pluginName + "'");
            }

            Map<Locale, Set<String>> legaciesTexts = new LinkedHashMap<>();
            Map<Locale, Set<String>> arrayTexts = new LinkedHashMap<>();
            @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[][]>> components = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : data.get("Components").getAsJsonObject().entrySet()) {
                Map<Locale, BaseComponent[][]> localizedComponents = new LinkedHashMap<>();
                String key = entry.getKey();

                for (Map.Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {
                    Locale locale;
                    try {
                        locale = Locale.valueOf(entry2.getKey().toUpperCase());
                    } catch (IllegalArgumentException ignore) {
                        throw new IllegalArgumentException("Couldn't find a locale named '" + entry2.getKey() + "'");
                    }

                    JsonElement base = entry2.getValue();
                    if (base.isJsonArray()) { // Is array
                        List<BaseComponent[]> array = new LinkedList<>();
                        for (JsonElement line : base.getAsJsonArray()) {
                            String jsonStr = ChatColor.translateAlternateColorCodes('&', line.getAsString());

                            if (!(line.isJsonNull() || jsonStr.equals("")) && JsonUtils.isJson(jsonStr)) { // Check if is array text
                                array.add(ComponentSerializer.parse(jsonStr));
                                continue;
                            }

                            array.add(new BaseComponent[] { new TextComponent(jsonStr) });

                            // Declare legacy text
                            legaciesTexts.putIfAbsent(locale, new LinkedHashSet<>());
                            legaciesTexts.get(locale).add(key); // Legacy text
                        }
                        localizedComponents.put(locale, array.toArray(new BaseComponent[0][]));

                        // Declare array text
                        arrayTexts.putIfAbsent(locale, new LinkedHashSet<>());
                        arrayTexts.get(locale).add(key);
                    } else { // Not array
                        List<BaseComponent[]> array = new LinkedList<>();

                        String jsonStr = ChatColor.translateAlternateColorCodes('&', base.getAsString());
                        if (JsonUtils.isJson(jsonStr)) {
                            array.add(ComponentSerializer.parse(jsonStr));
                        } else {
                            array.add(new BaseComponent[] { new TextComponent(jsonStr) });

                            // Declare legacy text
                            legaciesTexts.putIfAbsent(locale, new LinkedHashSet<>());
                            legaciesTexts.get(locale).add(key); // Legacy text
                        }

                        localizedComponents.put(locale, array.toArray(new BaseComponent[0][]));
                    }
                }

                components.put(key, localizedComponents);
            }

            return new BukkitMessageStorage(plugin, name, defaultLocale, components, legaciesTexts, arrayTexts);
        } else {
            throw new IllegalArgumentException("This SerializedData version '" + serializedData.getVersion() + "' isn't compatible with this deserializator");
        }
    }

    @Override
    public boolean merge(@NotNull MessageStorage<BaseComponent> from) {
        boolean changes = false;

        f1:
        for (Message<BaseComponent> fromMessage : from.getMessages()) {
            for (Message<BaseComponent> toMessage : this.getMessages()) {
                if (toMessage.getId().equals(fromMessage.getId())) {
                    continue f1;
                }
            }

            String id = fromMessage.getId();

            components.put(id, new LinkedHashMap<Locale, BaseComponent[][]>() {{
                for (Locale locale : fromMessage.getLocales()) {
                    if (fromMessage.isArray(locale)) {
                        Set<BaseComponent> lines = new LinkedHashSet<>(Arrays.asList(fromMessage.getText(locale)));

                        components.put(id, new LinkedHashMap<Locale, BaseComponent[][]>() {{
                            BaseComponent[][] componentArray = new BaseComponent[lines.size()][lines.size()];

                            int row = 0;
                            for (BaseComponent component : lines) {
                                componentArray[row] = new BaseComponent[] { component };
                                row++;
                            }

                            put(locale, componentArray);
                        }});
                    } else {
                        components.put(id, new LinkedHashMap<Locale, BaseComponent[][]>() {{
                            put(locale, new BaseComponent[][] {
                                    fromMessage.getText(locale)
                            });
                        }});
                    }
                }
            }});
            changes = true;
        }

        return changes;
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    @Override
    public @NotNull IBukkitMessage[] getMessages() {
        Set<IBukkitMessage> bukkitMessageSet = new LinkedHashSet<>();
        for (String id : getData().keySet()) {
            bukkitMessageSet.add(new BukkitMessage(this, id));
        }
        return bukkitMessageSet.toArray(new IBukkitMessage[0]);
    }

    @Override
    @Unmodifiable
    public @NotNull Map<@NotNull String, Map<@NotNull Locale, @NotNull BaseComponent[]>> getData() {
        Map<String, Map<Locale, BaseComponent[]>> map = new LinkedHashMap<>();

        for (String key : components.keySet()) {
            map.put(key, new LinkedHashMap<Locale, BaseComponent[]>() {{
                for (Map.Entry<Locale, @NotNull BaseComponent[][]> entry : components.get(key).entrySet()) {
                    Locale locale = entry.getKey();

                    if (isArray(key, locale)) {
                        Set<BaseComponent> components = new LinkedHashSet<>();

                        for (BaseComponent[] componentArray : entry.getValue()) {
                            components.add(ComponentUtils.merge(componentArray));
                        }

                        put(entry.getKey(), components.toArray(new BaseComponent[0]));
                    } else {
                        put(entry.getKey(), entry.getValue()[0]);
                    }
                }
            }});
        }

        return Collections.unmodifiableMap(map);
    }

    @Override
    public boolean isLegacyText(@NotNull String id, @NotNull Locale locale) {
        return getLegaciesTexts().containsKey(locale) && getLegaciesTexts().get(locale).contains(id);
    }

    /**
     * Retrieves the legacy texts associated with the current object.
     * A legacy text will be serialized as text, not as a base component json
     *
     * @return A map of legacy texts associated with the current object.
     */
    public @NotNull Map<Locale, Set<String>> getLegaciesTexts() {
        return legaciesTexts;
    }

    /**
     * @return the array texts map of this storage
     */
    public @NotNull Map<Locale, Set<String>> getArrayTexts() {
        return arrayTexts;
    }
}
