package codes.laivy.mlanguage.api.bukkit.reflection.classes.player;

import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory.Container;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class EntityPlayer extends ObjectExecutor {

    public static @NotNull EntityPlayer getEntityPlayer(@NotNull Player player) {
        return new CraftPlayer(player).getHandle();
    }

    public EntityPlayer(@Nullable Object value) {
        super(value);
    }

    public @NotNull PlayerConnection getConnection() {
        return new PlayerConnection(getDefApi().getVersion().getFieldExec("EntityPlayer:playerConnection").invokeInstance(this));
    }

    public @NotNull String getLocale() {
        return (String) Objects.requireNonNull(getDefApi().getVersion().getFieldExec("EntityPlayer:locale").invokeInstance(this));
    }

    public @NotNull Container getActiveContainer() {
        return new Container(getDefApi().getVersion().getFieldExec("EntityPlayer:activeContainer").invokeInstance(this));
    }
    public @NotNull Container getDefaultContainer() {
        return new Container(getDefApi().getVersion().getFieldExec("EntityPlayer:defaultContainer").invokeInstance(this));
    }

    @Override
    public @NotNull EntityPlayerClass getClassExecutor() {
        return (EntityPlayerClass) getDefApi().getVersion().getClassExec("EntityPlayer");
    }

    public static class EntityPlayerClass extends ClassExecutor {
        public EntityPlayerClass(@NotNull String className) {
            super(className);
        }
    }
}
