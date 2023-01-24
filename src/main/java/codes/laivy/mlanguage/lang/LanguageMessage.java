package codes.laivy.mlanguage.lang;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LanguageMessage {

    private final @NotNull Language language;
    private final @NotNull String key;
    private final @NotNull Object[] replaces;

    public LanguageMessage(@NotNull Language language, @NotNull String key, @NotNull Object... replaces) {
        this.language = language;
        this.key = key;
        this.replaces = replaces;
    }

    public @NotNull String get(@Nullable Locale locale, @NotNull Object... replaces) {
        return get(true, locale, replaces);
    }
    public @NotNull String get(boolean safe, @Nullable Locale locale, @NotNull Object... replaces) {
        String message = getLanguage().get(safe, (locale != null ? locale : Language.DEFAULT_LANGUAGE_CODE), key);

        for (@NotNull Object replace : getReplaces()) {
            if (replace instanceof LanguageMessage) {
                LanguageMessage eMessage = (LanguageMessage) replace;
                message = message.replaceFirst("%s", eMessage.get(safe, locale));
            } else message = message.replaceFirst("%s", replace.toString());
        }
        for (@NotNull Object replace : replaces) {
            if (replace instanceof LanguageMessage) {
                LanguageMessage eMessage = (LanguageMessage) replace;
                message = message.replaceFirst("%s", eMessage.get(safe, locale));
            } else message = message.replaceFirst("%s", replace.toString());
        }

        return message;
    }

    public @NotNull Object[] getReplaces() {
        return replaces;
    }
    public @NotNull Language getLanguage() {
        return language;
    }
    public @NotNull String getKey() {
        return key;
    }

    public @NotNull JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("language_name", getLanguage().getName());
        object.addProperty("plugin", getLanguage().getPlugin());
        object.addProperty("key", getKey());

        JsonArray replaces = new JsonArray();
        for (Object replace : getReplaces()) replaces.add(replace.toString());
        object.add("replaces", replaces);

        return object;
    }
    public static @NotNull LanguageMessage deserialize(@NotNull JsonObject object) {
        String languageName = object.get("language_name").getAsString();
        String plugin = object.get("plugin").getAsString();
        String key = object.get("key").getAsString();

        String[] replaces = new String[object.getAsJsonArray("replaces").size()];
        int row = 0;
        for (JsonElement element : object.getAsJsonArray("replaces")) {
            replaces[row] = element.getAsString();
            row++;
        }

        Language language = null;
        for (Language fLang : Language.LANGUAGES_ALL) {
            if (fLang.getName().equals(languageName) && fLang.getPlugin().equals(plugin)) {
                language = fLang;
                break;
            }
        }

        if (language == null) {
            throw new NullPointerException("Couldn't find a language named '" + languageName + "' of the plugin '" + plugin + "'");
        }

        return new LanguageMessage(language, key, (Object[]) replaces);
    }

}
