package codes.laivy.mlanguage.api.bukkit.reflection.classes.player;

import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory.Container;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
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
        return new PlayerConnection(multiplesLanguagesBukkit().getApi().getVersion().getFieldExec("EntityPlayer:playerConnection").invokeInstance(this));
    }

    public @NotNull String getLocale() {
        return (String) Objects.requireNonNull(multiplesLanguagesBukkit().getApi().getVersion().getFieldExec("EntityPlayer:locale").invokeInstance(this));
    }

    public @NotNull Container getActiveContainer() {
        return new Container(multiplesLanguagesBukkit().getApi().getVersion().getFieldExec("EntityPlayer:activeContainer").invokeInstance(this));
    }
    public @NotNull Container getDefaultContainer() {
        return new Container(multiplesLanguagesBukkit().getApi().getVersion().getFieldExec("EntityPlayer:defaultContainer").invokeInstance(this));
    }

    @Override
    public @NotNull EntityPlayerClass getClassExecutor() {
        return (EntityPlayerClass) multiplesLanguagesBukkit().getApi().getVersion().getClassExec("EntityPlayer");
    }

    public static class EntityPlayerClass extends ClassExecutor {
        public EntityPlayerClass(@NotNull String className) {
            super(className);
        }
    }
}
