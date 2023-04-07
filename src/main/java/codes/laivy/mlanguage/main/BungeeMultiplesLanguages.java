package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BungeeMultiplesLanguages extends Plugin implements Platform {

    public static @NotNull BungeeMultiplesLanguages multiplesLanguagesBungee() {
        return (BungeeMultiplesLanguages) ProxyServer.getInstance().getPluginManager().getPlugin("LvMultiplesLanguages");
    }

    private @NotNull IMultiplesLanguagesAPI api;

    public BungeeMultiplesLanguages() {
        // TODO: 06/04/2023 Bungee Support
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull IMultiplesLanguagesAPI getApi() {
        return api;
    }

    @Override
    public void setApi(@NotNull IMultiplesLanguagesAPI api) {
        this.api = api;
    }

    @Override
    public void log(@NotNull BaseComponent component) {
        ProxyServer.getInstance().getConsole().sendMessage(component);
    }

    @Override
    public @NotNull Type getType() {
        return Type.BUNGEE;
    }


}
