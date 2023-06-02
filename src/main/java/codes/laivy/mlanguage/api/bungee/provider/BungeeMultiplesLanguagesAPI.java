package codes.laivy.mlanguage.api.bungee.provider;

import codes.laivy.mlanguage.api.MessageSerializer;
import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.BungeeMessageStorage;
import codes.laivy.mlanguage.api.bungee.IBungeeMultiplesLanguagesAPI;
import codes.laivy.mlanguage.exceptions.PluginNotFoundException;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.main.BungeeMultiplesLanguages;
import codes.laivy.mlanguage.utils.FileUtils;
import codes.laivy.mlanguage.utils.Merge;
import com.google.gson.*;
import net.md_5.bungee.api.ChatColor;
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

                Merge merge = merge(storage, new BungeeMessageStorageProvider(plugin, name, locale, messages));

                if (!merge.getMerged().isEmpty()) {
                    getPlugin().log(new TextComponent("§7Has been added §f" + merge.getMerged().size() + " messages §7to the §f'" + fs.getName() + "' §7message storage of the plugin §f'" + plugin.getDescription().getName() + "'§7."));
                } if (!merge.getUnused().isEmpty()) {
                    StringBuilder messagesStr = new StringBuilder();
                    int row = 0;

                    for (String id : merge.getUnused()) {
                        if (row > 0) messagesStr.append("§r, ");
                        messagesStr.append("§f").append(id);
                        row++;
                    }

                    getPlugin().log(new TextComponent("§7These messages at the §f'" + fs.getName() + "' §7message storage of the plugin §f'" + plugin.getDescription().getName() + "' §7isn't used by the plugin, you can remove them: " + messagesStr));
                }

                break;
            }
        }

        if (storage == null) {
            storage = new BungeeMessageStorageProvider(plugin, name, locale, messages);
            getStorages().add(storage);
        }

        return storage;
    }

    private @NotNull Merge merge(@NotNull BungeeMessageStorage to, @NotNull BungeeMessageStorage from) {
        Set<String> merged = new HashSet<>();
        Set<String> unused = new HashSet<>();

        for (BungeeMessage toMessage : to.getMessages()) {
            Optional<BungeeMessage> fromMessage = from.getMessages().stream().filter(m -> m.getId().equals(toMessage.getId())).findFirst();
            if (!fromMessage.isPresent()) {
                unused.add(toMessage.getId());
            }
        }
        for (BungeeMessage fromMessage : from.getMessages()) {
            Optional<BungeeMessage> toMessage = to.getMessages().stream().filter(m -> m.getId().equals(fromMessage.getId())).findFirst();
            if (!toMessage.isPresent()) {
                to.getMessages().add(new BungeeMessageProvider(fromMessage.getId(), fromMessage.getData(), fromMessage.getArrayTexts(), fromMessage.getLegacyTexts(), fromMessage.getReplacements(), fromMessage.getPrefixes(), fromMessage.getSuffixes()));
                merged.add(fromMessage.getId());
            }
        }

        return new Merge(merged, unused);
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
                        String dataStr = ChatColor.translateAlternateColorCodes('&', content.toString());

                        //noinspection deprecation
                        JsonElement json = new JsonParser().parse(dataStr);
                        if (json.isJsonObject()) {
                            BungeeMessageStorage storage;
                            try {
                                storage = serializer.deserializeStorage(json);
                                storage = createStorage((Plugin) storage.getPluginProperty().getPlugin(), storage.getName(), storage.getDefaultLocale(), storage.getMessages());
                            } catch (PluginNotFoundException e) {
                                unknown.add(e.getPlugin());
                                continue;
                            }

                            storage.load();

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
                    throw new IllegalStateException("Cannot create storage file data '" + storage.getPluginProperty().getName() + File.separator + rootFile.getParentFile().getName() + "' file of the storage '" + storage.getName() + "' at the plugin '" + storage.getPluginProperty().getName() + "'");
                }
                if (!file.exists()) {
                    throw new NoSuchFileException("Couldn't get the message storage file '" + storage.getPluginProperty().getName() + File.separator + rootFile.getParentFile().getName() + "'");
                }
                // Write the serialized data into
                JsonElement data = serializer.serializeStorage(storage);
                String dataStr = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(data).replace("§", "&");

                try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                     BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {
                    writer.write(dataStr);
                }

                storage.unload();
            } catch (Throwable e) {
                e.printStackTrace();
                getPlugin().log(new TextComponent("§cCouldn't save message storage called '" + storage.getName() + "' of the plugin '" + storage.getPluginProperty().getName() + "'"));
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
