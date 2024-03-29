package codes.laivy.mlanguage.plugin;

import org.jetbrains.annotations.NotNull;

public interface PluginProperty {

    /**
     * @return the plugin instance
     */
    @NotNull Object getPlugin();

    /**
     * @return the name of the plugin
     */
    @NotNull String getName();

}
