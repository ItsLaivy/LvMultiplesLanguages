package codes.laivy.mlanguage.data.plugin;

import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BungeePluginProperty implements PluginProperty {

    private final @NotNull Plugin plugin;

    public BungeePluginProperty(@NotNull Plugin plugin) {
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
