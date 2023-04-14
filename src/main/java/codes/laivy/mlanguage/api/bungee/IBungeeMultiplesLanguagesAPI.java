package codes.laivy.mlanguage.api.bungee;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.MessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface IBungeeMultiplesLanguagesAPI extends IMultiplesLanguagesAPI<Void, Plugin, ProxiedPlayer, BaseComponent> {

    @Override
    @NotNull IBungeeMessage getMessage(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces);

    @Override
    @NotNull IBungeeMessage getMessage(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces);

    @Override
    @NotNull IBungeeArrayMessage getMessageArray(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces);

    @Override
    @NotNull IBungeeArrayMessage getMessageArray(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces);

    @Override
    @Nullable IBungeeMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name);

    @NotNull IBungeeMessageStorage create(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[][]>> components);

    @Override
    @NotNull BaseComponent[] getText(@Nullable Locale locale, @NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces);

    default @NotNull BaseComponent[] getText(@NotNull ProxiedPlayer player, @NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return getText(player.getUniqueId(), messageStorage, id, replaces);
    }

}
