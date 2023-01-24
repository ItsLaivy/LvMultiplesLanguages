package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.lang.Language;
import codes.laivy.mlanguage.lang.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface LocaleAPI {

    @Nullable Locale getLocale(@NotNull UUID uuid);
    void setLocale(@NotNull UUID uuid, @NotNull Locale locale);

    @NotNull String getMessage(@NotNull String plugin, @NotNull String key);
    @NotNull String getMessage(@NotNull String plugin, @NotNull String key, @Nullable Locale locale);
    @Nullable String getNullableMessage(@NotNull String plugin, @NotNull String key, @Nullable Locale locale);

    boolean hasKey(@NotNull String plugin, @NotNull String key, @Nullable Locale locale);

    @NotNull Set<@NotNull Language> getLanguages();
    @NotNull Set<@NotNull Language> getLanguages(@NotNull String plugin);

}
