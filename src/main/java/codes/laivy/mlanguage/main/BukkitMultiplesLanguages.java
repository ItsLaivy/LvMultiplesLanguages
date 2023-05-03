package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.LvMultiplesLanguages;
import codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.IBukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.natives.InjectionManager;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BukkitMultiplesLanguages extends JavaPlugin implements Platform<ItemStack, Plugin, Player, BaseComponent>, Listener {

    public static @NotNull BukkitMultiplesLanguages multiplesLanguagesBukkit() {
        return JavaPlugin.getPlugin(BukkitMultiplesLanguages.class);
    }

    public BukkitMultiplesLanguages() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();

        BukkitItemTranslator translator = new BukkitItemTranslator();
        LvMultiplesLanguages.setApi(new BukkitMultiplesLanguagesAPI(this, translator, new InjectionManager(translator)));
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

    @Override
    public void log(@NotNull BaseComponent component) {
        getServer().getConsoleSender().sendMessage("§8[§6" + getDescription().getName() + "§8]§7" + " " + ComponentUtils.getText(component));
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        if (!getApi().isLoaded()) getApi().load();
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
