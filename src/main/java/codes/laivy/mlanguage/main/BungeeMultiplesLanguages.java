package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.api.bungee.BungeeMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bungee.IBungeeMultiplesLanguagesAPI;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BungeeMultiplesLanguages extends Plugin implements Platform<Void, Plugin, ProxiedPlayer, BaseComponent> {

    public static @NotNull BungeeMultiplesLanguages multiplesLanguagesBungee() {
        return (BungeeMultiplesLanguages) ProxyServer.getInstance().getPluginManager().getPlugin("LvMultiplesLanguages");
    }

    private boolean serverLoaded = false;

    public BungeeMultiplesLanguages() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();

        LvMultiplesLanguages.setApi(new BungeeMultiplesLanguagesAPI(this));
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
    public @NotNull IBungeeMultiplesLanguagesAPI getApi() {
        if (LvMultiplesLanguages.getApi() == null) {
            throw new UnsupportedOperationException("The API isn't inicialized yet");
        } else if (!(LvMultiplesLanguages.getApi() instanceof IBungeeMultiplesLanguagesAPI)) {
            throw new UnsupportedOperationException("This API type isn't valid");
        }
        return (IBungeeMultiplesLanguagesAPI) LvMultiplesLanguages.getApi();
    }

    public void setApi(@NotNull IBungeeMultiplesLanguagesAPI api) {
        if (api != getApi()) {
            if (getApi().isLoaded()) getApi().unload();
            LvMultiplesLanguages.setApi(api);
            if (!getApi().isLoaded() && isServerLoaded()) api.load();
        }
    }

    private boolean isServerLoaded() {
        return serverLoaded;
    }

    @Override
    public void log(@NotNull BaseComponent component) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§8[§6" + getDescription().getName() + "§8]§7" + " " + ComponentUtils.getText(component)));
    }

    @Override
    @Contract(pure = true)
    public @NotNull Type getType() {
        return Type.BUNGEE;
    }

}
