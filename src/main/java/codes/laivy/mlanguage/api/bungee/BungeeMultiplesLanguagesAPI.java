package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.bungee.natives.BungeeArrayMessage;
import codes.laivy.mlanguage.api.bungee.natives.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.natives.BungeeMessageStorage;
import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.data.SerializedData;
import codes.laivy.mlanguage.lang.Locale;
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
import java.util.*;

/**
 * The default api of the Bungee LvMultiplesLanguages
 */
public class BungeeMultiplesLanguagesAPI implements IBungeeMultiplesLanguagesAPI {

    private final @NotNull BungeeMultiplesLanguages plugin;
    private @Nullable Set<MessageStorage<BaseComponent>> messageStorages;
    private boolean loaded = false;

    public BungeeMultiplesLanguagesAPI(@NotNull BungeeMultiplesLanguages plugin) {
        this.plugin = plugin;
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
                            MessageStorage<BaseComponent> storage = SerializedData.deserialize(json.getAsJsonObject()).get();
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
        if (!isLoaded()) {
            return;
        }

        // Unloading storages
        for (MessageStorage<BaseComponent> storage : getStorages()) {
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
                JsonElement data = storage.serialize().serialize();

                try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                     BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(data));
                }

                storage.unload();
            } catch (Throwable e) {
                e.printStackTrace();
                getPlugin().log(new TextComponent("§cCouldn't save message storage called '" + storage.getName() + "' of the plugin '" + storage.getPluginProperty().getName() + "'"));
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
    public @NotNull Set<MessageStorage<BaseComponent>> getStorages() {
        if (messageStorages == null) {
            throw new NullPointerException("The API isn't loaded yet");
        }
        return messageStorages;
    }

    @Override
    public @Nullable IBungeeMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name) {
        for (MessageStorage<BaseComponent> messageStorage : getStorages()) {
            if (messageStorage.getName().equals(name) && messageStorage.getPluginProperty().getPlugin().equals(plugin)) {
                if (messageStorage instanceof IBungeeMessageStorage) {
                    return (IBungeeMessageStorage) messageStorage;
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull IBungeeMessageStorage create(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[][]>> components) {
        IBungeeMessageStorage storage = null;

        for (MessageStorage<BaseComponent> fs : getStorages()) {
            if (fs.getPluginProperty().getPlugin().equals(plugin) && fs.getName().equals(name)) {
                if (fs instanceof IBungeeMessageStorage) {
                    storage = (IBungeeMessageStorage) fs;

                    IBungeeMessageStorage temp = new BungeeMessageStorage(plugin, name, defaultLocale, components);
                    if (storage.merge(temp)) {
                        getPlugin().log(new TextComponent("New messages has been added to the '" + fs.getName() + "' message storage of the plugin '" + storage.getPluginProperty() + "'."));
                    }
                }
            }
        }

        if (storage == null) {
            storage = new BungeeMessageStorage(plugin, name, defaultLocale, components);
        }

        return storage;
    }

    @Override
    public @NotNull BaseComponent[] getText(@Nullable Locale locale, @NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return messageStorage.getText(locale, id, replaces);
    }

    @Override
    public @NotNull IBungeeMessage getMessage(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return this.getMessage(messageStorage, id, new LinkedList<>(), new LinkedList<>(), replaces);
    }

    @Override
    public @NotNull IBungeeMessage getMessage(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces) {
        if (!(messageStorage instanceof BungeeMessageStorage)) {
            throw new UnsupportedOperationException("The message storage needs to be an instance of the bungee message storage");
        }

        return new BungeeMessage((BungeeMessageStorage) messageStorage, id, prefixes, suffixes, replaces);
    }

    @Override
    public @NotNull IBungeeArrayMessage getMessageArray(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return this.getMessageArray(messageStorage, id, new LinkedList<>(), new LinkedList<>(), replaces);
    }

    @Override
    public @NotNull IBungeeArrayMessage getMessageArray(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces) {
        if (!(messageStorage instanceof BungeeMessageStorage)) {
            throw new UnsupportedOperationException("The message storage needs to be an instance of the bungee message storage");
        }
        return new BungeeArrayMessage((IBungeeMessageStorage) messageStorage, id, prefixes, suffixes, replaces);
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
    public void setLocale(@NotNull UUID user, @Nullable Locale locale) {
        throw new UnsupportedOperationException("The default LvMultiplesLanguages API doesn't supports locale changes because it uses the client's locale");
    }

    @Override
    public @Nullable ItemTranslator<Void, ProxiedPlayer, BaseComponent> getItemTranslator() {
        throw new UnsupportedOperationException("This bungee API doesn't support item translations");
    }
}
