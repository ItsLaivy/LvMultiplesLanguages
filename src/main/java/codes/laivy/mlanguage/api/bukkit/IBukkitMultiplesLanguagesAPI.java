package codes.laivy.mlanguage.api.bukkit;

import codes.laivy.mlanguage.api.IMultiplesLanguagesAPI;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.lang.MessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface IBukkitMultiplesLanguagesAPI extends IMultiplesLanguagesAPI<ItemStack, Plugin, Player, BaseComponent> {

    @Override
    @Nullable IBukkitItemTranslator getItemTranslator();

    @Override
    @NotNull IBukkitMessage getMessage(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces);

    @Override
    @NotNull IBukkitMessage getMessage(@NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull List<@NotNull Object> prefixes, @NotNull List<@NotNull Object> suffixes, @NotNull Object... replaces);

    @Override
    @Nullable IBukkitMessageStorage getStorage(@NotNull Plugin plugin, @NotNull String name);

    @Override
    @NotNull IBukkitMessageStorage create(@NotNull Plugin plugin, @NotNull String name, @NotNull Locale defaultLocale, @NotNull Map<@NotNull String, Map<Locale, @NotNull BaseComponent[]>> components);

    default @NotNull BaseComponent[] getText(@NotNull OfflinePlayer player, @NotNull MessageStorage<BaseComponent> messageStorage, @NotNull String id, @NotNull Object... replaces) {
        return getText(player.getUniqueId(), messageStorage, id, replaces);
    }
}
