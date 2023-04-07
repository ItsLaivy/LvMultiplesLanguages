package codes.laivy.mlanguage.api.bukkit.reflection.classes.player;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class PlayerConnection extends ObjectExecutor {
    public PlayerConnection(@Nullable Object value) {
        super(value);
    }

    public @NotNull NetworkManager getNetworkManager() {
        return new NetworkManager(multiplesLanguagesBukkit().getApi().getVersion().getFieldExec("PlayerConnection:networkManager").invokeInstance(this));
    }

    public void sendPacket(@NotNull Packet packet) {
        multiplesLanguagesBukkit().getApi().getVersion().getMethodExec("PlayerConnection:sendPacket").invokeInstance(this, packet);
    }

    @Override
    public @NotNull PlayerConnectionClass getClassExecutor() {
        return (PlayerConnectionClass) multiplesLanguagesBukkit().getApi().getVersion().getClassExec("PlayerConnection");
    }

    public static class PlayerConnectionClass extends ClassExecutor {
        public PlayerConnectionClass(@NotNull String className) {
            super(className);
        }
    }
}
