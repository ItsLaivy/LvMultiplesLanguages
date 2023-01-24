package codes.laivy.mlanguage.lang;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.utils.ChatColor;
import com.avaje.ebean.validation.NotEmpty;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;

import static codes.laivy.mlanguage.LvMultiplesLanguages.getApi;
import static codes.laivy.mlanguage.LvMultiplesLanguages.getPluginFolder;

public class Language {

    public static final @NotNull Map<@NotNull Locale, @NotNull Map<@NotNull String /* plugin */, @NotNull Set<@NotNull Language>>> LANGUAGES = new HashMap<>();
    public static final @NotNull Set<@NotNull Language> LANGUAGES_ALL = new LinkedHashSet<>();

    public static @NotNull Locale DEFAULT_LANGUAGE_CODE = Locale.EN_US;
    private static @NotNull String UNKNOWN_TRANSLATION_MESSAGE = "<unknown translation: '%key%', lang: '%language%'>";

    public static String getUnknownTranslationMessage(@NotNull String key, @NotNull Locale language) {
        return UNKNOWN_TRANSLATION_MESSAGE.replace("%key%", key).replace("%language%", language.name());
    }
    public static void setUnknownTranslationMessage(@NotNull String unknownTranslationMessage) {
        UNKNOWN_TRANSLATION_MESSAGE = unknownTranslationMessage;
    }

    private @NotNull String name;
    private @Nullable String description;

    private final @NotNull String plugin;
    private final @NotNull @NotEmpty Set<@NotNull LanguagePack> packs;

    private @Nullable File folder;
    private @Nullable File file;

    public Language(@NotNull String name, @Nullable String description, @NotNull String plugin, @NotNull LinkedHashSet<@NotNull LanguagePack> packs) {
        this.name = name;
        this.description = description;
        this.plugin = plugin;
        this.packs = packs;

        if (this.name.length() > 64) {
            throw new IllegalArgumentException("The name's length needs to be less than or equal to 64 characters");
        } else if (packs.size() == 0) {
            throw new IllegalArgumentException("You cannot provide a empty packs list!");
        }

        loadFiles(false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadFiles(boolean create) {
        this.folder = new File(getPluginFolder(), plugin);
        if (create) this.folder.mkdirs();

        if (getFolder() != null) {
            try {
                File directory = new File(getFolder().getPath());
                this.file = new File(directory, name + ".json");

                if (!file.exists() && create) {
                    Files.createDirectories(directory.toPath());
                    file.createNewFile();
                }
            } catch (Throwable e) {
                throw new RuntimeException("Cannot create file from language '" + getName() + "' of the plugin '" + getPlugin() + "'", e);
            }
        }
    }

    public void register() {
        for (Locale locale : getLocales()) {
            LANGUAGES.get(locale).putIfAbsent(getPlugin(), new LinkedHashSet<>());
            LANGUAGES.get(locale).get(getPlugin()).add(this);
        }
        LANGUAGES_ALL.add(this);

        loadFiles(true);

        saveFile();

        // Verify all the messages
        for (LanguagePack pack : packs) {
            if (!getDefaultPack().equals(pack)) {
                for (String message : pack.getMessages().keySet()) {
                    if (!getDefaultPack().getMessages().containsKey(message)) {
                        throw new RuntimeException("The pack '" + pack.getName() + "' contains a illegal message ID '" + message + "'. Packs cannot have messages IDs that doesn't is included at the default pack (Default pack: '" + getDefaultPack().getName() + "') at the translation '" + getName() + "' of the plugin '" + plugin + "'");
                    }
                }
            }
        }
        //

        // Verify pack names
        for (LanguagePack pack1 : packs) {
            for (LanguagePack pack2 : packs) {
                if (!pack1.equals(pack2)) {
                    if (pack1.getName().equals(pack2.getName())) {
                        throw new RuntimeException("A pack with that name '" + pack1.getName() + "' already exists at the translation '" + getName() + "' of the plugin '" + plugin + "'");
                    }
                }
            }
        }
        //
    }

    public void saveFile() {
        if (getFile() != null && getFile().exists()) {
            @Nullable Language oldLanguage;

            try (Reader reader = new FileReader(getFile())) {
                oldLanguage = Language.deserialize(getPlugin(), JsonParser.parseReader(reader).getAsJsonObject());

                if (merge(oldLanguage, this)) {
                    LvMultiplesLanguages.log(Level.WARNING, "New messages has been added to the '" + getName() + "' language pack of the plugin '" + getPlugin() + "'. (File: \"" + getFile() + "\")");
                }
                //
            } catch (IllegalStateException ignore) {
                oldLanguage = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(getFile().toPath()), StandardCharsets.UTF_8))) {
                writer.write(gson.toJson(oldLanguage != null ? oldLanguage.serialize() : serialize()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public @NotNull LanguagePack getDefaultPack() {
        return new LinkedList<>(getPacks()).getFirst();
    }

    public @Nullable File getFolder() {
        return folder;
    }
    public @Nullable File getFile() {
        return file;
    }

    public @Nullable String getIfContains(@NotNull Locale locale, @NotNull String key) {
        for (LanguagePack pack : getPacks()) {
            for (Locale fLocale : pack.getLocales()) {
                if (fLocale.equals(locale)) {
                    String msg;
                    if ((msg = pack.get(key)) != null) {
                        return msg;
                    }
                }
            }
        }

        if (!getLocales().contains(DEFAULT_LANGUAGE_CODE)) {
            if (LANGUAGES.get(DEFAULT_LANGUAGE_CODE).containsKey(getPlugin())) {
                for (Language language : LANGUAGES.get(DEFAULT_LANGUAGE_CODE).get(getPlugin())) {
                    return language.get(false, DEFAULT_LANGUAGE_CODE, key);
                }
            }
        }

        return null;
    }
    public @NotNull String get(@NotNull UUID player, @NotNull String key, @NotNull Object... replaces) {
        return get(true, player, key, replaces);
    }
    public @NotNull String get(boolean safe, @NotNull UUID player, @NotNull String key, @NotNull Object... replaces) {
        return get(safe, LvMultiplesLanguages.getApi().getLocaleAPI().getLocale(player), key, replaces);
    }

    public @NotNull String get(@Nullable Locale locale, @NotNull String key, @NotNull Object... replaces) {
        return get(true, locale, key, replaces);
    }
    public @NotNull String get(boolean safe, @Nullable Locale locale, @NotNull String key, @NotNull Object... replaces) {
        if (locale == null) locale = Language.DEFAULT_LANGUAGE_CODE;

        String msg = getIfContains(locale, key);
        if (msg == null) {
            if (safe) {
                msg = getDefaultPack().get(key);
            }

            if (msg == null) {
                return getUnknownTranslationMessage(key, locale);
            }
        }

        for (Object replace : replaces) {
            if (replace instanceof LanguageMessage) {
                LanguageMessage eMessage = (LanguageMessage) replace;
                msg = msg.replaceFirst("%s", Matcher.quoteReplacement(eMessage.get(locale)));
            } else {
                msg = msg.replaceFirst("%s", Matcher.quoteReplacement(replace.toString()));
            }
        }

        return msg;
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

    public @NotNull String getPlugin() {
        return plugin;
    }

    public @NotNull @NotEmpty Set<@NotNull LanguagePack> getPacks() {
        return new LinkedHashSet<>(packs);
    }
    public void addPack(@NotNull LanguagePack pack) {
        for (String key : pack.getMessages().keySet()) {
            if (getDefaultPack().get(key) == null) {
                throw new RuntimeException("The pack '" + pack.getName() + "' contains a illegal message ID '" + key + "'. Packs cannot have messages IDs that doesn't is included at the default pack (Default pack: '" + getDefaultPack().getName() + "') at the translation '" + getName() + "' of the plugin '" + getPlugin() + "'");
            }
        }
        for (LanguagePack fPack : getPacks()) {
            if (fPack.getName().equals(pack.getName())) {
                throw new RuntimeException("A pack with that name '" + pack.getName() + "' already exists at the translation '" + getName() + "' of the plugin '" + getPlugin() + "'");
            }
        }

        this.packs.add(pack);
    }
    public void removePack(@NotNull LanguagePack pack) {
        this.packs.remove(pack);
    }

    public @NotNull Set<@NotNull Locale> getLocales() {
        Set<Locale> locales = new LinkedHashSet<>();
        for (LanguagePack pack : getPacks()) {
            locales.addAll(pack.getLocales());
        }
        return locales;
    }

    public @NotNull JsonObject serialize() {
        JsonObject object = new JsonObject();

        // Packs
        JsonArray packs = new JsonArray();
        for (LanguagePack pack : getPacks()) {
            packs.add(pack.serialize());
        }
        //

        object.addProperty("name", getName());
        if (getDescription() != null) {
            object.addProperty("description", getDescription());
        }
        object.add("packs", packs);

        return object;
    }
    public static @NotNull Language deserialize(@NotNull String plugin, @NotNull JsonObject object) {
        String name = object.get("name").getAsString();
        String description = null;
        if (object.has("description")) {
            description = object.get("description").getAsString();
        }
        LinkedHashSet<LanguagePack> packs = new LinkedHashSet<>();

        for (JsonElement element : object.getAsJsonArray("packs")) {
            packs.add(LanguagePack.deserialize(element.getAsJsonObject()));
        }

        return new Language(name, description, plugin, packs);
    }

    public static @NotNull Map<@NotNull String, @NotNull String> getMessagesAtLanguageFile(@NotNull InputStream resource) {
        Reader defConfigStream = new InputStreamReader(resource, StandardCharsets.UTF_8);
        JsonObject object = JsonParser.parseReader(defConfigStream).getAsJsonObject();

        Map<String, String> map = new HashMap<>();

        for (JsonElement element : object.getAsJsonArray("packs")) {
            JsonObject pack = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> message : pack.getAsJsonObject("messages").entrySet()) {
                map.put(message.getKey(), ChatColor.translateAlternateColorCodes('&', message.getValue().getAsString()));
            }
        }

        return map;
    }

    /**
     * Creates a new language with the provided {@link InputStream}.
     * The language will not be created if a language with that properties already exists for this plugin.
     *
     * @param plugin the plugin name of that language
     * @param resource the Resource, you can see a example at the plugin's <b>languages/example_format_2.json</b> resources
     * @return the created language
     */
    public static @NotNull Language createByResourceStream(@NotNull String plugin, @NotNull InputStream resource) {
        Reader defConfigStream = new InputStreamReader(resource, StandardCharsets.UTF_8);
        Language streamLanguage = Language.deserialize(plugin, JsonParser.parseReader(defConfigStream).getAsJsonObject());

        for (Language language : getApi().getLocaleAPI().getLanguages(plugin)) {
            if (language.getName().equalsIgnoreCase(streamLanguage.getName())) {
                merge(language, streamLanguage);
                return language;
            }
        }

        File file = new File(getPluginFolder() + File.separator + plugin, streamLanguage.getName() + ".json");
        if (file.exists()) {
            try (Reader current = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                Language fileLang = Language.deserialize(plugin, JsonParser.parseReader(current).getAsJsonObject());
                merge(fileLang, streamLanguage);
                streamLanguage = fileLang;
            } catch (Exception e) {
                throw new RuntimeException("Couldn't read translation file '" + streamLanguage.getName() + "' of the plugin '" + plugin + "'", e);
            }
        }

        streamLanguage.register();
        return streamLanguage;
    }

    private static boolean merge(@NotNull Language to, @NotNull Language from) {
        boolean updated = false;
        Set<String> defaultKeys = new LinkedHashSet<>();

        f1:
        for (LanguagePack pack : from.getPacks()) {
            for (LanguagePack sPack : to.getPacks()) {
                if (sPack.getName().equals(pack.getName())) {

                    for (String key : pack.getMessages().keySet()) {
                        if (!sPack.getMessages().containsKey(key)) {
                            if (pack.isDefault(from)) {
                                defaultKeys.add(key);
                            } else if (!defaultKeys.contains(key)) {
                                continue;
                            }

                            sPack.getMessages().put(key, pack.getMessages().get(key));
                            updated = true;
                        }
                    }

                    continue f1;
                }
            }

            to.addPack(pack);
            updated = true;
        }

        return updated;
    }

}
