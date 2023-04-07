package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagLong extends NBTBase {
    public NBTTagLong(long value) {
        this(multiplesLanguagesBukkit().getApi().getVersion().nbtTag(Version.NBTTag.LONG, value).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagLong from an NMS NBTTagLong
     * @param value a NMS NBTTagLong
     */
    protected NBTTagLong(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagLongClass getClassExecutor() {
        return (NBTTagLongClass) multiplesLanguagesBukkit().getApi().getVersion().getClassExec("NBTBase:NBTTagLong");
    }

    public static class NBTTagLongClass extends NBTBaseClass {
        public NBTTagLongClass(@NotNull String className) {
            super(className);
        }
    }
}
