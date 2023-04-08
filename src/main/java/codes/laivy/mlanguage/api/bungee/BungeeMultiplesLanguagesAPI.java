package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bungee.natives.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.natives.BungeeMessageStorage;
import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.main.BungeeMultiplesLanguages;
import codes.laivy.mlanguage.utils.FileUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The default api of the Bungee LvMultiplesLanguages
 */
public class BungeeMultiplesLanguagesAPI implements IMultiplesLanguagesAPI<Plugin> {

    private final @NotNull BungeeMultiplesLanguages plugin;
    private @Nullable Set<MessageStorage> messageStorages;
    private boolean loaded = false;

    public BungeeMultiplesLanguagesAPI(@NotNull BungeeMultiplesLanguages plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        messageStorages = new LinkedHashSet<>();
        getPlugin().log(new TextComponent("§7Loading default LvMultiplesLanguages API"));

        // Loading languages
        int loaded = 0;
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
                            MessageStorage storage = SerializedData.deserialize(json.getAsJsonObject()).get(null);
                            storage.load();

                            messageStorages.add(storage);

                            loaded++;
                        } else {
                            throw new JsonSyntaxException("The content of this file isn't a json object.");
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        getPlugin().log(new TextComponent("§cCouldn't load the message storage of file '" + file + "', is the json written correctly?"));
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            getPlugin().log(new TextComponent("§cCouldn't load the message storages"));
            return;
        }
        getPlugin().log(new TextComponent("§aLoaded " + loaded + " message storages"));
        //

        this.loaded = true;
    }

    @Override
    public void unload() {
        // Unloading storages
        for (MessageStorage storage : getStorages()) {
            try {
                File rootFile = new File(getPlugin().getDataFolder(), getPlugin().getDescription().getName() + File.separator);
                // Create storage path (if not exists)
                if (!rootFile.exists() && !rootFile.mkdirs()) {
                    throw new IllegalStateException("Cannot create storage '" + storage.getName() + "' of the plugin '" + getPlugin().getDescription().getName() + "' path");
                }
                // Create storage file (if not exists)
                @NotNull File file = new File(rootFile, FileUtils.fileNameTranslate(storage.getName()) + ".json");
                if (!file.exists() && !file.createNewFile()) {
                    throw new IllegalStateException("Cannot create storage file data '" + getPlugin().getDescription().getName() + File.separator + rootFile.getParentFile().getName() + "' file of the storage '" + getPlugin().getDescription().getName() + "' at the plugin '" + storage.getName() + "'");
                }
                if (!file.exists()) {
                    throw new NoSuchFileException("Couldn't get the message storage file '" + getPlugin().getDescription().getName() + File.separator + rootFile.getParentFile().getName() + "'");
                }
                // Write the serialized data into
                JsonElement data = storage.serialize().serialize();

                try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                     BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(data));
                }

                storage.unload();
            } catch (Throwable e) {
                e.printStackTrace();
                getPlugin().log(new TextComponent("§cCouldn't save message storage called '" + storage.getName() + "' of the plugin '" + getPlugin().getDescription().getName() + "'"));
            }
        }
        // Unload variables
        messageStorages = null;
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public @NotNull BungeeMultiplesLanguages getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull Set<MessageStorage> getStorages() {
        if (messageStorages == null) {
            throw new NullPointerException("The API isn't loaded yet");
        }
        return messageStorages;
    }

    @Override
    public @NotNull MessageStorage create(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components) {
        MessageStorage storage = null;

        for (MessageStorage fs : getStorages()) {
            if (fs.getPlugin().equals(plugin) && fs.getName().equals(name)) {
                storage = fs;

                IBungeeMessageStorage temp = new BungeeMessageStorage(plugin, name, defaultLocale, components);
                if (storage.merge(temp)) {
                    getPlugin().log(new TextComponent("New messages has been added to the '" + fs.getName() + "' message storage of the plugin '" + getPlugin().getDescription().getName() + "'."));
                }
            }
        }

        if (storage == null) {
            storage = new BungeeMessageStorage(plugin, name, defaultLocale, components);
        }

        return storage;
    }

    @Override
    public @NotNull BaseComponent[] get(@Nullable Locale locale, @NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return messageStorage.get(locale, id, replaces);
    }

    @Override
    public @NotNull Message get(@NotNull MessageStorage messageStorage, @NotNull String id, @NotNull Message... replaces) {
        if (!(messageStorage instanceof BungeeMessageStorage)) {
            throw new UnsupportedOperationException("The message storage needs to be an instance of the bungee message storage");
        } else if (!(replaces instanceof BungeeMessage[])) {
            throw new UnsupportedOperationException("The messages replaces array needs to be an instance of the bungee message array");
        }

        return new BungeeMessage((BungeeMessageStorage) messageStorage, id, (BungeeMessage[]) replaces);
    }

    @Override
    public @NotNull Locale getLocale(@NotNull UUID user) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(user);
        if (player != null) {
            return Locale.fromJavaLocale(ProxyServer.getInstance().getPlayer(user).getLocale());
        }
        throw new UnsupportedOperationException("This user isn't online");
    }

    @Override
    public void setLocale(@NotNull UUID user, @Nullable Locale locale) {
        throw new UnsupportedOperationException("The default LvMultiplesLanguages API doesn't supports locale changes because it uses the client's locale");
    }

    @Override
    public @Nullable ItemTranslator<?, ?> getItemTranslator() {
        throw new UnsupportedOperationException("This bungee API doesn't support item translations");
    }
}
