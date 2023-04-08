package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public interface IBukkitMultiplesLanguagesAPI extends IMultiplesLanguagesAPI<Plugin> {

    @Override
    @Nullable IBukkitItemTranslator getItemTranslator();
    
}
