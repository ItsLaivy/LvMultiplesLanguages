package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.api.bungee.IBungeeMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bungee.BungeeMessage;
import codes.laivy.mlanguage.api.bungee.BungeeMessageStorage;
import codes.laivy.mlanguage.api.bungee.provider.BungeeMessageSerializerProvider;
import codes.laivy.mlanguage.api.bungee.provider.BungeeMultiplesLanguagesAPI;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BungeeMultiplesLanguages extends Plugin implements Platform<Plugin, BaseComponent[], BungeeMessage, BungeeMessageStorage> {

    public static @NotNull BungeeMultiplesLanguages multiplesLanguagesBungee() {
        return (BungeeMultiplesLanguages) ProxyServer.getInstance().getPluginManager().getPlugin("LvMultiplesLanguages");
    }

    public BungeeMultiplesLanguages() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();

        LvMultiplesLanguages.setApi(new BungeeMultiplesLanguagesAPI(this, new BungeeMessageSerializerProvider()));
    }

    @Override
    public void onEnable() {
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

    @Override
    public void log(@NotNull BaseComponent @NotNull ... components) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§8[§6" + getDescription().getName() + "§8]§7" + " " + ComponentUtils.getText(components)));
    }

    @Override
    public void broadcast(@NotNull BungeeMessage message, @NotNull Object[] replaces) {
        for (@NotNull ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(message.getText(player, replaces));
        }
    }

    public void setApi(@NotNull IBungeeMultiplesLanguagesAPI api) {
        if (api != getApi()) {
            if (getApi().isLoaded()) getApi().unload();
            LvMultiplesLanguages.setApi(api);
            api.load();
        }
    }

    @Override
    @Contract(pure = true)
    public @NotNull Type getType() {
        return Type.BUNGEE;
    }

}
