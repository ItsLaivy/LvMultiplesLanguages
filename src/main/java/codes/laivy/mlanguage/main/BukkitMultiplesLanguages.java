package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.api.bukkit.IBukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.provider.BukkitMessageSerializerProvider;
import codes.laivy.mlanguage.api.bukkit.provider.BukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.provider.InjectionManager;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.Platform;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

public class BukkitMultiplesLanguages extends JavaPlugin implements Platform<Plugin, BaseComponent[], BukkitMessage, BukkitMessageStorage> {

    public static @NotNull BukkitMultiplesLanguages multiplesLanguagesBukkit() {
        return JavaPlugin.getPlugin(BukkitMultiplesLanguages.class);
    }

    private @NotNull InjectionManager injectionManager;
    private @Nullable Version version;

    public BukkitMultiplesLanguages() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();

        // Version
        try {
            //noinspection unchecked
            Class<Version> clazz = (Class<Version>) ReflectionUtils.getNullableClass("codes.laivy.mlanguage.api.bukkit.reflection.versions." + ReflectionUtils.getVersionName().toUpperCase());
            if (clazz == null) {
                throw new NullPointerException("Couldn't find this server version's properties (" + ReflectionUtils.getVersionName() + "), this plugin version isn't compatible with this server version yet.");
            }

            Constructor<Version> constructor = clazz.getDeclaredConstructor(BukkitMultiplesLanguages.class);
            constructor.setAccessible(true);

            this.version = constructor.newInstance(this);
        } catch (Throwable e) {
            throw new RuntimeException("Couldn't load the version properties", e);
        }
        // Version

        BukkitItemTranslator translator = new BukkitItemTranslator(this);
        injectionManager = new InjectionManager(translator);
        LvMultiplesLanguages.setApi(new BukkitMultiplesLanguagesAPI(this, new BukkitMessageSerializerProvider()));
    }

    @Override
    public @NotNull IBukkitMultiplesLanguagesAPI getApi() {
        if (LvMultiplesLanguages.getApi() == null) {
            throw new UnsupportedOperationException("The API isn't inicialized yet");
        } else if (!(LvMultiplesLanguages.getApi() instanceof IBukkitMultiplesLanguagesAPI)) {
            throw new UnsupportedOperationException("This API type isn't valid");
        }
        return (IBukkitMultiplesLanguagesAPI) LvMultiplesLanguages.getApi();
    }

    public void setApi(@NotNull IBukkitMultiplesLanguagesAPI api) {
        if (api != getApi()) {
            if (getApi().isLoaded()) getApi().unload();
            LvMultiplesLanguages.setApi(api);
            if (!getApi().isLoaded()) api.load();
        }
    }

    public @NotNull InjectionManager getInjectionManager() {
        return injectionManager;
    }
    public void setInjectionManager(@NotNull InjectionManager injectionManager) {
        this.injectionManager = injectionManager;
    }

    public @NotNull Version getVersion() {
        if (version != null) {
            return version;
        } else {
            throw new UnsupportedOperationException("The plugin doesn't supports this");
        }
    }
    public void setVersion(@NotNull Version version) {
        this.version = version;
    }

    @Override
    public void log(@NotNull BaseComponent @NotNull ... components) {
        getServer().getConsoleSender().sendMessage("§8[§6" + getDescription().getName() + "§8]§7" + " " + ComponentUtils.getText(components));
    }

    @Override
    public void broadcast(@NotNull BukkitMessage message, @NotNull Object[] replaces) {
        for (@NotNull Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(message.getText(player, replaces));
        }
    }

    @Override
    public void onEnable() {
        // Reflections
        if (version != null) {
            getVersion().loadClasses();
            getVersion().loadMethods();
            getVersion().loadFields();

            log(new TextComponent("§eReflections §b- §7Loaded: " + getVersion().getClasses().size() + " classes, " + getVersion().getMethods().size() + " methods and " + getVersion().getFields().size() + " fields."));
        }
        // Api
        getApi().load();
    }

    @Override
    public void onDisable() {
        getApi().unload();
    }

    @Override
    @Contract(pure = true)
    public final @NotNull Type getType() {
        return Type.BUKKIT;
    }
}
