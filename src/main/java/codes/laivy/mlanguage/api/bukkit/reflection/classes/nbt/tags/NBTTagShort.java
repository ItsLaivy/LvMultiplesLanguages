package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NBTTagShort extends NBTBase {
    public NBTTagShort(short value) {
        this(getDefApi().getVersion().nbtTag(Version.NBTTag.SHORT, value).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagShort from an NMS NBTTagShort
     * @param value a NMS NBTTagShort
     */
    protected NBTTagShort(@Nullable Object value) {
        super(value);
    }

    @Override
    public @NotNull NBTTagShortClass getClassExecutor() {
        return (NBTTagShortClass) getDefApi().getVersion().getClassExec("NBTBase:NBTTagShort");
    }

    public static class NBTTagShortClass extends NBTBaseClass {
        public NBTTagShortClass(@NotNull String className) {
            super(className);
        }
    }
}
