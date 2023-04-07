package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.translator.IBukkitItemTranslator;
import org.jetbrains.annotations.Nullable;

public interface IBukkitMultiplesLanguagesAPI extends IMultiplesLanguagesAPI {

    @Override
    @Nullable IBukkitItemTranslator getItemTranslator();
    
}
