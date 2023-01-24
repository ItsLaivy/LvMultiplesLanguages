package codes.laivy.mlanguage.main.bungee;

import codes.laivy.data.sql.SQLTable;
import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.config.JsonConfigUtils;
import codes.laivy.mlanguage.lang.Language;
import codes.laivy.mlanguage.lang.Locale;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import static codes.laivy.mlanguage.LvMultiplesLanguages.getApi;
import static codes.laivy.mlanguage.LvMultiplesLanguages.setup;

public class LvMultiplesLanguagesBungee extends Plugin {

    // ---/- Short the API use -/--- //
    public static @NotNull String getMessage(@NotNull Plugin plugin, @NotNull String key, @NotNull ProxiedPlayer player) {
        return getApi().getLocaleAPI().getMessage(plugin.getDescription().getName(), key, getApi().getLocaleAPI().getLocale(player.getUniqueId()));
    }
    public static @NotNull String getMessage(@NotNull Plugin plugin, @NotNull String key) {
        return getApi().getLocaleAPI().getMessage(plugin.getDescription().getName(), key);
    }
    public static @NotNull String getMessage(@NotNull Plugin plugin, @NotNull String key, @NotNull Locale locale) {
        return getApi().getLocaleAPI().getMessage(plugin.getDescription().getName(), key, locale);
    }
    public static @NotNull Locale getLocale(@NotNull ProxiedPlayer player) {
        Locale locale = getApi().getLocaleAPI().getLocale(player.getUniqueId());
        if (locale == null) locale = Language.DEFAULT_LANGUAGE_CODE;
        return locale;
    }
    // ---/- Short the API use -/--- //

    public static @NotNull LvMultiplesLanguagesBungee plugin() {
        return (LvMultiplesLanguagesBungee) ProxyServer.getInstance().getPluginManager().getPlugin("LvMultiplesLanguages");
    }

    private final @NotNull JsonObject configuration;
    private final @NotNull SQLTable table;

    public LvMultiplesLanguagesBungee() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();
        configuration = JsonConfigUtils.safeGet("/config.json", getDataFolder().toPath()).getAsJsonObject();
        table = setup(configuration);
    }

    @Override
    public void onEnable() {
        LvMultiplesLanguages.getApi().setMenusAPI(player -> null);
    }

    public @NotNull SQLTable getTable() {
        return table;
    }

    public @NotNull JsonObject getConfiguration() {
        return configuration;
    }
}
