package codes.laivy.mlanguage.api.bungee.provider;

import codes.laivy.mlanguage.api.MessageSerializer;
import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.BungeeMessageStorage;
import codes.laivy.mlanguage.api.bungee.IBungeeMultiplesLanguagesAPI;
import codes.laivy.mlanguage.exceptions.PluginNotFoundException;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.main.BungeeMultiplesLanguages;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.FileUtils;
import codes.laivy.mlanguage.utils.JsonUtils;
import com.google.gson.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.*;

public class BungeeMultiplesLanguagesAPI implements IBungeeMultiplesLanguagesAPI {

    private final @NotNull BungeeMultiplesLanguages plugin;
    private @Nullable Set<BungeeMessageStorage> messageStorages;

    private final @NotNull MessageSerializer<BaseComponent[], BungeeMessage, BungeeMessageStorage> serializer;

    private boolean loaded = false;

    public BungeeMultiplesLanguagesAPI(@NotNull BungeeMultiplesLanguages plugin, @NotNull MessageSerializer<BaseComponent[], BungeeMessage, BungeeMessageStorage> serializer) {
        this.plugin = plugin;
        this.serializer = serializer;
    }

    @Override
    public @NotNull BungeeMultiplesLanguages getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull Set<BungeeMessageStorage> getStorages() {
        if (messageStorages == null) {
            throw new NullPointerException("The API isn't loaded yet");
        }
        return messageStorages;
    }

    @Override
    public @NotNull BaseComponent @NotNull [] getText(@Nullable Locale locale, @NotNull BungeeMessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return messageStorage.getText(locale, id, replaces);
    }

    @Override
    public @NotNull Locale getLocale(@NotNull UUID user) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(user);
        if (player != null) {
            java.util.Locale javaLocale = ProxyServer.getInstance().getPlayer(user).getLocale();
            if (javaLocale != null) {
                return Locale.fromJavaLocale(ProxyServer.getInstance().getPlayer(user).getLocale());
            }
        }
        throw new UnsupportedOperationException("This user isn't online");
    }

    @Override
    public void setLocale(@NotNull UUID user, @NotNull Locale locale) {
        throw new UnsupportedOperationException("The default LvMultiplesLanguages API doesn't supports locale changes because it uses the client's locale");
    }

    @Override
    public @NotNull BungeeMessage createMessage(@NotNull String id, @NotNull Map<@NotNull Locale, BaseComponent @NotNull []> data) {
        return new BungeeMessageProvider(id, data);
    }

    @Override
    public @NotNull MessageSerializer<BaseComponent[], BungeeMessage, BungeeMessageStorage> getSerializer() {
        return serializer;
    }

    @Override
    public @Nullable BungeeMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name) {
        Optional<BungeeMessageStorage> optional = getStorages().stream().filter(s -> s.getName().equals(name) && s.getPluginProperty().getPlugin().equals(plugin)).findFirst();
        return optional.orElse(null);
    }

    @Override
    public @NotNull BungeeMessageStorage createStorage(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale locale, @NotNull Set<BungeeMessage> messages) {
        BungeeMessageStorage storage = null;

        for (BungeeMessageStorage fs : getStorages()) {
            if (fs.getPluginProperty().getPlugin().equals(plugin) && fs.getName().equals(name)) {
                storage = fs;

                BungeeMessageStorage temp = new BungeeMessageStorageProvider(plugin, name, locale, messages);
                if (merge(storage, temp)) {
                    getPlugin().log(new TextComponent("New messages has been added to the '" + fs.getName() + "' message storage of the plugin '" + getPlugin().getDescription().getName() + "'."));
                }
            }
        }

        if (storage == null) {
            storage = new BungeeMessageStorageProvider(plugin, name, locale, messages);
            getStorages().add(storage);
        }

        return storage;
    }

    private boolean merge(@NotNull BungeeMessageStorage to, @NotNull BungeeMessageStorage from) {
        boolean changes = false;

        for (BungeeMessage message : from.getMessages()) {
            Optional<BungeeMessage> toMessage = to.getMessages().stream().filter(m -> m.getId().equals(message.getId())).findFirst();
            if (!toMessage.isPresent()) {
                to.getMessages().add(new BungeeMessageProvider(message.getId(), message.getData(), message.getArrayTexts(), message.getLegacyTexts(), message.getReplacements(), message.getPrefixes(), message.getSuffixes()));

                changes = true;
            }
        }

        return changes;
    }

    @Override
    public void load() {
        if (isLoaded()) {
            return;
        }

        messageStorages = new LinkedHashSet<>();
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(""));
        getPlugin().log(new TextComponent("§7Loading default LvMultiplesLanguages API"));

        // Loading languages
        int loaded = 0;
        List<String> unknown = new ArrayList<>();

        getPlugin().log(new TextComponent("§7Loading message storages..."));
        try {
            for (File file : FileUtils.listFiles(getPlugin().getDataFolder().toString())) {
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                    String line; // ignore
                    StringBuilder content = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line);
                    }

                    try {
                        //noinspection deprecation
                        JsonElement json = new JsonParser().parse(content.toString());
                        if (json.isJsonObject()) {
                            BungeeMessageStorage storage;
                            try {
                                storage = deserializeStorage(json);
                            } catch (PluginNotFoundException e) {
                                unknown.add(e.getPlugin());
                                continue;
                            }

                            getStorages().add(storage);

                            storage.load();

                            messageStorages.add(storage);

                            loaded++;
                        } else {
                            throw new JsonSyntaxException("The content of this file isn't a json object.");
                        }
                    } catch (JsonSyntaxException e) {
                        throw new RuntimeException("Couldn't load the message storage of file '" + file + "', is the json written correctly?", e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load the message storages", e);
        }
        getPlugin().log(new TextComponent("§aLoaded " + loaded + " message storages"));
        if (!unknown.isEmpty()) {
            StringBuilder unknownList = new StringBuilder();
            for (String plugin : unknown) {
                if (unknown.indexOf(plugin) != 0) unknownList.append("§c, ");
                unknownList.append("§6").append(plugin);
            }
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(""));
            getPlugin().log(new TextComponent("§cSome storages couldn't be loaded because the plugin isn't on the server/enabled anymore, here is a full list of them: §6" + unknownList));
        }
        //
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(""));

        this.loaded = true;
    }

    @Override
    public void unload() {
        if (!isLoaded()) {
            return;
        }

        // Unloading storages
        for (BungeeMessageStorage storage : getStorages()) {
            try {
                File rootFile = new File(getPlugin().getDataFolder(), storage.getPluginProperty().getName());
                // Create storage path (if not exists)
                if (!rootFile.exists() && !rootFile.mkdirs()) {
                    throw new IllegalStateException("Cannot create storage '" + storage.getName() + "' of the plugin '" + storage.getPluginProperty().getName() + "' path");
                }

                // Create storage file (if not exists)
                @NotNull File file = new File(rootFile, FileUtils.fileNameTranslate(storage.getName()) + ".json");
                if (!file.exists() && !file.createNewFile()) {
                    throw new IllegalStateException("Cannot create storage file data '" + storage.getPluginProperty().getName() + File.separator + rootFile.getParentFile().getName() + "' file of the storage '" + storage.getPluginProperty().getName() + "' at the plugin '" + storage.getName() + "'");
                }
                if (!file.exists()) {
                    throw new NoSuchFileException("Couldn't get the message storage file '" + storage.getPluginProperty().getName() + File.separator + rootFile.getParentFile().getName() + "'");
                }
                // Write the serialized data into
                JsonElement data = serializeStorage(storage);

                try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                     BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(data));
                }

                storage.unload();
            } catch (Throwable e) {
                e.printStackTrace();
                getPlugin().log(new TextComponent("§cCouldn't save message storage called '" + storage.getName() + "' of the plugin '" + getPlugin().getDescription().getName() + "'"));
            }
        }
        //

        messageStorages = null;
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    // Serializer

    protected @NotNull JsonElement serializeStorage(@NotNull BungeeMessageStorage storage) {
        JsonObject object = new JsonObject();
        JsonObject messages = new JsonObject();

        for (BungeeMessage message : storage.getMessages()) {
            JsonObject messageObj = new JsonObject();
            JsonObject contentObj = new JsonObject();

            JsonArray prefixes = new JsonArray();
            JsonArray suffixes = new JsonArray();

            MessageSerializer<BaseComponent[], BungeeMessage, BungeeMessageStorage> serializer = getPlugin().getApi().getSerializer();
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
                prefixes.add(serializer.serializeObject(prefix));
            }
            for (Object suffix : message.getSuffixes()) {
                suffixes.add(serializer.serializeObject(suffix));
            }

            if (prefixes.size() > 0) {
                messageObj.add("prefixes", prefixes);
            } if (suffixes.size() > 0) {
                messageObj.add("suffixes", suffixes);
            }

            messages.add(message.getId(), messageObj);
        }

        object.addProperty("name", storage.getName());
        object.addProperty("plugin", storage.getPluginProperty().getName());
        object.addProperty("default locale", storage.getDefaultLocale().name());
        object.add("messages", messages);

        return object;
    }
    protected @NotNull BungeeMessageStorage deserializeStorage(@NotNull JsonElement storageElement) {
        JsonObject object = storageElement.getAsJsonObject();

        String name = object.get("name").getAsString();
        String pluginStr = object.get("plugin").getAsString();
        Locale defaultLocale = Locale.valueOf(object.get("default locale").getAsString());
        JsonObject messages = object.getAsJsonObject("messages");

        Optional<Plugin> optional = ProxyServer.getInstance().getPluginManager().getPlugins().stream().filter(p -> p.getDescription().getName().equals(pluginStr)).findFirst();
        @Nullable Plugin plugin = optional.orElse(null);
        if (plugin == null) {
            throw new NullPointerException("Couldn't find plugin '" + pluginStr + "'");
        }

        return getPlugin().getApi().createStorage(plugin, name, defaultLocale, new LinkedHashSet<BungeeMessage>() {{
            for (Map.Entry<String, JsonElement> entry : messages.entrySet()) {
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
                        Set<BaseComponent> array = new LinkedHashSet<>();
                        for (JsonElement line : base.getAsJsonArray()) {
                            String jsonStr = ChatColor.translateAlternateColorCodes('&', line.getAsString());

                            if (!(line.isJsonNull() || jsonStr.equals("")) && JsonUtils.isJson(jsonStr)) { // Check if is array text
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
                            text = new BaseComponent[] { new TextComponent(jsonStr) };

                            // Declare legacy text
                            legacies.add(locale); // Legacy text
                        }

                        data.put(locale, text);
                    }
                }

                BungeeMessage message = getPlugin().getApi().createMessage(id, data);

                message.getLegacyTexts().addAll(legacies);
                message.getArrayTexts().addAll(arrays);

                if (messagesObj.has("prefixes")) {
                    Set<Object> prefixes = new LinkedHashSet<>();
                    JsonArray prefixesObj = messagesObj.getAsJsonArray("prefixes");
                    for (JsonElement prefixElement : prefixesObj) {
                        prefixes.add(serializer.deserializeObject(prefixElement));
                    }
                    message.getPrefixes().addAll(prefixes);
                }
                if (messagesObj.has("suffixes")) {
                    Set<Object> suffixes = new LinkedHashSet<>();
                    JsonArray suffixesObj = messagesObj.getAsJsonArray("suffixes");
                    for (JsonElement suffixElement : suffixesObj) {
                        suffixes.add(serializer.deserializeObject(suffixElement));
                    }
                    message.getPrefixes().addAll(suffixes);
                }

                add(message);
            }
        }});
    }
}
