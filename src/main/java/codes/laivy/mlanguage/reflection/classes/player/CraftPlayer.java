package codes.laivy.mlanguage.reflection.classes.player;

import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.ObjectExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class CraftPlayer extends ObjectExecutor {
    public CraftPlayer(@Nullable Object value) {
        super(value);
    }

    public @NotNull EntityPlayer getHandle() {
        return new EntityPlayer(multiplesLanguagesBukkit().getVersion().getMethodExec("CraftPlayer:getHandle").invokeInstance(this));
    }

    @Override
    public @NotNull CraftPlayerClass getClassExecutor() {
        return (CraftPlayerClass) multiplesLanguagesBukkit().getVersion().getClassExec("CraftPlayer");
    }

    public static class CraftPlayerClass extends ClassExecutor {
        public CraftPlayerClass(@NotNull String className) {
            super(className);
        }
    }
}
