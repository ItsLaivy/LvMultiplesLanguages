package codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class NBTTagString extends NBTBase {
    public NBTTagString(@NotNull String string) {
        this(getDefApi().getVersion().nbtTag(Version.NBTTag.STRING, string).getValue());
    }

    /**
     * Construct a Multiples languages NBTTagString from an NMS NBTTagString
     * @param value a NMS NBTTagString
     */
    public NBTTagString(@Nullable Object value) {
        super(value);
    }

    public @Nullable String getData() {
        return (String) getDefApi().getVersion().getFieldExec("NBTTagString:getData").invokeInstance(this);
    }

    @Override
    public @NotNull NBTTagStringClass getClassExecutor() {
        return (NBTTagStringClass) getDefApi().getVersion().getClassExec("NBTBase:NBTTagString");
    }

    public static class NBTTagStringClass extends NBTBaseClass {
        public NBTTagStringClass(@NotNull String className) {
            super(className);
        }
    }
}
