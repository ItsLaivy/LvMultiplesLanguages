package codes.laivy.mlanguage.api.bukkit.reflection.classes.packets;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class Packet extends ObjectExecutor {
    public Packet(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull PacketClass getClassExecutor() {
        return (PacketClass) getDefApi().getVersion().getClassExec("Packet");
    }

    public static class PacketClass extends ClassExecutor {
        public PacketClass(@NotNull String className) {
            super(className);
        }
    }
}
