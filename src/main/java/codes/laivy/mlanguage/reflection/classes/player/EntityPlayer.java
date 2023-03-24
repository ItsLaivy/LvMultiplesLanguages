package codes.laivy.mlanguage.reflection.classes.player;

import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class EntityPlayer extends ObjectExecutor {

    public static @NotNull EntityPlayer getEntityPlayer(@NotNull Player player) {
        return new CraftPlayer(player).getHandle();
    }

    public EntityPlayer(@Nullable Object value) {
        super(value);
    }

    public @NotNull PlayerConnection getConnection() {
        return new PlayerConnection(multiplesLanguagesBukkit().getVersion().getFieldExec("EntityPlayer:playerConnection").invokeInstance(this));
    }

    public @NotNull String getLocale() {
        return (String) Objects.requireNonNull(multiplesLanguagesBukkit().getVersion().getFieldExec("EntityPlayer:locale").invokeInstance(this));
    }

    @Override
    public @NotNull EntityPlayerClass getClassExecutor() {
        return (EntityPlayerClass) multiplesLanguagesBukkit().getVersion().getClassExec("EntityPlayer");
    }

    public static class EntityPlayerClass extends ClassExecutor {
        public EntityPlayerClass(@NotNull String className) {
            super(className);
        }
    }
}
