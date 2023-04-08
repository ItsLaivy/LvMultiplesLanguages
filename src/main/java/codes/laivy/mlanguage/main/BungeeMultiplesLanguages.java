package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.bungee.BungeeMultiplesLanguagesAPI;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BungeeMultiplesLanguages extends Plugin implements Platform<Plugin> {

    public static @NotNull BungeeMultiplesLanguages multiplesLanguagesBungee() {
        return (BungeeMultiplesLanguages) ProxyServer.getInstance().getPluginManager().getPlugin("LvMultiplesLanguages");
    }

    private @NotNull BungeeMultiplesLanguagesAPI api;
    private boolean serverLoaded = false;

    public BungeeMultiplesLanguages() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();

        this.api = new BungeeMultiplesLanguagesAPI(this);
    }

    @Override
    public void onEnable() {
        // On server loads, load the API too
        serverLoaded = true;
        getApi().load();
    }

    @Override
    public void onDisable() {
        getApi().unload();
    }

    @Override
    public @NotNull BungeeMultiplesLanguagesAPI getApi() {
        return api;
    }
    public void setApi(@NotNull BungeeMultiplesLanguagesAPI api) {
        if (getApi().isLoaded()) getApi().unload();
        this.api = api;
        if (isServerLoaded()) api.load();
    }

    private boolean isServerLoaded() {
        return serverLoaded;
    }

    @Override
    public void log(@NotNull BaseComponent component) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§8[§6" + getDescription().getName() + "§8]§7" + " " + ComponentUtils.getText(component)));
    }

    @Override
    public @NotNull Type getType() {
        return Type.BUNGEE;
    }

}
