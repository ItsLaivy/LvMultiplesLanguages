package codes.laivy.mlanguage.api;

import codes.laivy.mlanguage.api.bukkit.BukkitMessage;
import codes.laivy.mlanguage.lang.Message;
import codes.laivy.mlanguage.lang.MessageStorage;
import codes.laivy.mlanguage.utils.ComponentUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

/**
 * This class serializes/deserializes the message and message storages
 * @param <C> the component message
 * @param <M> the message
 * @param <S> the message storage
 */
public interface MessageSerializer<C, M extends Message<C>, S extends MessageStorage<C, M>> {

    @NotNull JsonElement serializeComponent(@NotNull C component);
    @NotNull C deserializeComponent(@NotNull JsonElement component);

    @NotNull JsonElement serializeMessage(@NotNull M message);
    @NotNull M deserializeMessage(@NotNull JsonElement message);

    @NotNull JsonElement serializeStorage(@NotNull S storage);
    @NotNull S deserializeStorage(@NotNull JsonElement storage);

    @NotNull JsonElement serializeObject(@NotNull Object replace);
    @NotNull Object deserializeObject(@NotNull JsonElement element);

}
