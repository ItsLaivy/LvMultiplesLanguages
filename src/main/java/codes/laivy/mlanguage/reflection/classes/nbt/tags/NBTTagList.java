package codes.laivy.mlanguage.reflection.classes.nbt.tags;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class NBTTagList extends NBTBase {
    public NBTTagList(List<NBTBase> value) {
        this(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.LIST, value).getValue());
    }
    public NBTTagList(NBTTagList value) {
        this(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.LIST).getValue());
        concatenate(value);
    }

    /**
     * Construct a Multiples languages NBTTagList from an NMS NBTTagList
     * @param value a NMS NBTTagList
     */
    public NBTTagList(@Nullable Object value) {
        super(value);
    }

    public @Nullable List<?> getList() {
        return (List<?>) multiplesLanguagesBukkit().getVersion().getFieldExec("NBTTagList:list").invokeInstance(this);
    }

    public void concatenate(@NotNull NBTTagList compound) {
        multiplesLanguagesBukkit().getVersion().nbtbaseConcatenate(this, compound.getValue());
    }

    @Override
    public @NotNull Object getValue() {
        if (super.getValue() == null) {
            setValue(multiplesLanguagesBukkit().getVersion().nbtTag(Version.NBTTag.LIST).getValue());
        }

        return super.getValue();
    }

    @Override
    public @NotNull NBTTagListClass getClassExecutor() {
        return (NBTTagListClass) multiplesLanguagesBukkit().getVersion().getClassExec("NBTBase:NBTTagList");
    }

    public static class NBTTagListClass extends NBTBaseClass {
        public NBTTagListClass(@NotNull String className) {
            super(className);
        }
    }
}
