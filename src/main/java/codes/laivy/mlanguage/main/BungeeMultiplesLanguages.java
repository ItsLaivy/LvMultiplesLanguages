package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.MultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.item.ItemTranslatorBukkitImpl;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BungeeMultiplesLanguages extends Plugin implements Platform {

    public static @NotNull BungeeMultiplesLanguages multiplesLanguagesBungee() {
        return (BungeeMultiplesLanguages) ProxyServer.getInstance().getPluginManager().getPlugin("LvMultiplesLanguages");
    }

    private @NotNull IMultiplesLanguagesAPI api;

    public BungeeMultiplesLanguages() {
        this.api = new MultiplesLanguagesAPI(this, new ItemTranslatorBukkitImpl());
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
    public void log(@NotNull Object object) {
        // TODO: 22/03/2023 this
    }

    @Override
    public @NotNull Type getType() {
        return Type.BUNGEE;
    }


}
