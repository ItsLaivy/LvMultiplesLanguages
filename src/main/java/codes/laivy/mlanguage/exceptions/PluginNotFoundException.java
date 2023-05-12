package codes.laivy.mlanguage.exceptions;

import org.jetbrains.annotations.NotNull;

public class PluginNotFoundException extends RuntimeException {

    private final @NotNull String plugin;

    public PluginNotFoundException(@NotNull String plugin) {
        super("Plugin not found '" + plugin + "'");
        this.plugin = plugin;
    }

    public PluginNotFoundException(@NotNull String plugin, @NotNull Throwable cause) {
        super("Plugin not found '" + plugin + "'", cause);
        this.plugin = plugin;
    }

    public @NotNull String getPlugin() {
        return plugin;
    }
}
