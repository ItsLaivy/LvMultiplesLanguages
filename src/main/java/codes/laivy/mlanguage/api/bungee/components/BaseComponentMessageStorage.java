package codes.laivy.mlanguage.api.bungee.components;

import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface BaseComponentMessageStorage<M extends BaseComponentMessage> extends MessageStorage<BaseComponent[], M> {
    @NotNull List<@NotNull BaseComponent[]> getTextArray(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces);
    @NotNull BaseComponent[] getText(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces);

    @NotNull String getLegacyText(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces);
    @NotNull String getLegacyText(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces);

    @NotNull List<@NotNull String> getLegacyTextArray(@NotNull UUID uuid, @NotNull String id, @NotNull Object... replaces);
    @NotNull List<@NotNull String> getLegacyTextArray(@Nullable Locale locale, @NotNull String id, @NotNull Object... replaces);

    boolean isLegacy(@NotNull String id, @NotNull Locale locale);
    default boolean isLegacy(@NotNull Message<BaseComponent[]> message, @NotNull Locale locale) {
        return isLegacy(message.getId(), locale);
    }
}
