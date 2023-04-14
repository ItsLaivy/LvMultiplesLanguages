package codes.laivy.mlanguage.data.plugin;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BukkitPluginProperty implements PluginProperty {

    private final @NotNull Plugin plugin;

    public BukkitPluginProperty(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull String getName() {
        return getPlugin().getDescription().getName();
    }
}
