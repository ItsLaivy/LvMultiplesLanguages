package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagInt extends NBTBase {
    public NBTTagInt(int value) {
        this(multiplesLanguagesBukkit().getApi().getVersion().nbtTag(Version.NBTTag.INT, value).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagInt from an NMS NBTTagInt
     * @param value a NMS NBTTagInt
     */
    protected NBTTagInt(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagIntClass getClassExecutor() {
        return (NBTTagIntClass) multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagInt");
    }

    public static class NBTTagIntClass extends NBTBaseClass {
        public NBTTagIntClass(@NotNull String className) {
            super(className);
        }
    }
}
