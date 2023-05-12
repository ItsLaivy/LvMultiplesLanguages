package codes.laivy.mlanguage.api.bungee.provider;

import codes.laivy.mlanguage.api.MessageSerializer;
import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.BungeeMessageStorage;
import codes.laivy.mlanguage.api.bungee.IBungeeMultiplesLanguagesAPI;
import codes.laivy.mlanguage.lang.Locale;
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
                            BungeeMessageStorage storage = getPlugin().getApi().getSerializer().deserializeStorage(json);
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
        //

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
                JsonElement data = getPlugin().getApi().getSerializer().serializeStorage(storage);

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
}
