package codes.laivy.mlanguage.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MenusAPI {

    @Nullable Object getLanguageInventory(@NotNull UUID playerUuid);

}
