package codes.laivy.mlanguage.api.bukkit.reflection.classes.player;

import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class CraftPlayer extends ObjectExecutor {
    public CraftPlayer(@Nullable Object value) {
        super(value);
    }

    public @NotNull EntityPlayer getHandle() {
        return new EntityPlayer(getDefApi().getVersion().getMethodExec("CraftPlayer:getHandle").invokeInstance(this));
    }

    @Override
    public @NotNull CraftPlayerClass getClassExecutor() {
        return (CraftPlayerClass) getDefApi().getVersion().getClassExec("CraftPlayer");
    }

    public static class CraftPlayerClass extends ClassExecutor {
        public CraftPlayerClass(@NotNull String className) {
            super(className);
        }
    }
}
