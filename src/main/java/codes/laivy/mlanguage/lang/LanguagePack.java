package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.utils.ChatColor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class LanguagePack {

    private @NotNull String name;
    private @Nullable String description;
    private final @NotNull Map<@NotNull String, @NotNull String> messages;
    private final @NotNull Set<Locale> locales;

    public LanguagePack(@NotNull LinkedHashSet<Locale> locales, @NotNull String name, @NotNull String description) {
        this(locales, name, description, new LinkedHashMap<>());
    }
    public LanguagePack(@NotNull LinkedHashSet<Locale> locales, @NotNull String name, @Nullable String description, @NotNull LinkedHashMap<@NotNull String, @NotNull String> messages) {
        this.name = name;
        this.description = description;
        this.messages = messages;
        this.locales = locales;
    }

    public boolean isDefault(@NotNull Language language) {
        return language.getDefaultPack().getName().equals(getName());
    }

    public @Nullable String get(@NotNull String key) {
        if (getMessages().containsKey(key)) {
            return getMessages().get(key);
        }
        return null;
    }

    public @NotNull String getName() {
        return name;
    }
    public @Nullable String getDescription() {
        return description;
    }
    public void setName(@NotNull String name) {
        this.name = name;
    }
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public @NotNull Map<@NotNull String, @NotNull String> getMessages() {
        return messages;
    }

    public @NotNull Set<Locale> getLocales() {
        return new LinkedHashSet<>(locales);
    }

    public @NotNull JsonObject serialize() {
        JsonObject object = new JsonObject();

        // Messages
        JsonObject messages = new JsonObject();
        for (Map.Entry<String, String> entry : getMessages().entrySet()) {
            messages.addProperty(entry.getKey(), entry.getValue().replace("§", "&"));
        }
        //

        // Locales
        JsonArray locales = new JsonArray();
        for (Locale locale : getLocales()) {
            locales.add(locale.name());
        }
        //

        object.addProperty("name", getName());
        object.add("locales", locales);
        object.add("messages", messages);

        if (getDescription() != null) {
            object.addProperty("description", getDescription());
        }

        return object;
    }
    public static @NotNull LanguagePack deserialize(@NotNull JsonObject object) {
        String name = object.get("name").getAsString();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String desc = null;

        if (object.has("description")) {
            desc = object.get("description").getAsString();
        }

        // Locales
        LinkedHashSet<Locale> locales = new LinkedHashSet<>();
        for (JsonElement element : object.getAsJsonArray("locales")) {
            locales.add(Locale.valueOf(element.getAsString()));
        }
        //

        for (Map.Entry<String, JsonElement> element : object.getAsJsonObject("messages").entrySet()) {
            map.put(element.getKey(), ChatColor.translateAlternateColorCodes('&', element.getValue().getAsString()));
        }

        return new LanguagePack(locales, name, desc, map);
    }
}
