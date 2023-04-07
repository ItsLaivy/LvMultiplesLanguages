package codes.laivy.mlanguage.api.bukkit.reflection.classes.player;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NetworkManager extends ObjectExecutor {
    public NetworkManager(@Nullable Object value) {
        super(value);
    }

    public @NotNull Channel getChannel() {
        return (Channel) Objects.requireNonNull(getDefApi().getVersion().getFieldExec("NetworkManager:channel").invokeInstance(this));
    }

    @Override
    public @NotNull NetworkManagerClass getClassExecutor() {
        return (NetworkManagerClass) getDefApi().getVersion().getClassExec("NetworkManager");
    }

    public static class NetworkManagerClass extends ClassExecutor {
        public NetworkManagerClass(@NotNull String className) {
            super(className);
        }
    }
}
