package codes.laivy.mlanguage.reflection;

import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.reflection.classes.chat.IChatBaseComponent;
import codes.laivy.mlanguage.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.NBTTagList;
import codes.laivy.mlanguage.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.Executor;
import codes.laivy.mlanguage.reflection.executors.FieldExecutor;
import codes.laivy.mlanguage.reflection.executors.MethodExecutor;
import codes.laivy.mlanguage.reflection.objects.StringObjExec;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface Version {

    boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor);

    void load(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor);

    boolean isCompatible(@NotNull String version);

    void loadClasses();
    void loadMethods();
    void loadFields();

    @NotNull Map<String, ClassExecutor> getClasses();
    @NotNull Map<String, MethodExecutor> getMethods();
    @NotNull Map<String, FieldExecutor> getFields();

    @NotNull
    default ClassExecutor getClassExec(@NotNull String name) {
        if (getClasses().containsKey(name)) {
            return getClasses().get(name);
        } else {
            throw new NullPointerException("Cannot find a ClassExecutor named '" + name + "' at the version loader '" + getClass().getName() + "'");
        }
    }
    @NotNull
    default ClassExecutor getClassExec(@NotNull String name, boolean array) {
        ClassExecutor e = getClassExec(name);

        return new ClassExecutor(e.getReflectionClass(), array) {{
            load();
        }};
    }

    @NotNull
    default MethodExecutor getMethodExec(@NotNull String name) {
        if (getMethods().containsKey(name)) {
            return getMethods().get(name);
        } else {
            throw new NullPointerException("Cannot find a MethodExecutor named '" + name + "' at the version loader '" + getClass().getName() + "'");
        }
    }

    @NotNull
    default FieldExecutor getFieldExec(@NotNull String name) {
        if (getFields().containsKey(name)) {
            return getFields().get(name);
        } else {
            throw new NullPointerException("Cannot find a FieldExecutor named '" + name + "' at the version loader '" + getClass().getName() + "'");
        }
    }

    /**
     * Concatenate two NBTBases into one
     *
     * @param into The NBTBase that will receive the values
     * @param from The NBTBase that will give the values
     */
    void nbtbaseConcatenate(@NotNull NBTBase into, @NotNull Object from);

    @Nullable Object nbtList(@NotNull NBTListAction action, @NotNull NBTTagList object, @Nullable Object value);

    /**
     * Returns a new instance of an NBTBase
     *
     * @param tag the tag type (you can get the NBTTag from an NMS NBTBase using {@link NBTBase#getTagType(Object)}
     * @param objects the constructor's parameters
     * @return a NBTBase
     */
    @NotNull
    NBTBase nbtTag(@NotNull NBTTag tag, @NotNull Object... objects);

    @Nullable Object nbtCompound(@NotNull NBTCompoundAction action, @NotNull NBTTagCompound object, @Nullable StringObjExec key, @Nullable NBTBase value);

    @NotNull String getName();

    enum NBTTag {
        BYTE(byte.class),
        BYTE_ARRAY(byte[].class),
        COMPOUND(null),
        DOUBLE(double.class),
        FLOAT(float.class),
        INT(int.class),
        INT_ARRAY(int[].class),
        LIST(List.class),
        LONG(long.class),
        SHORT(short.class),
        STRING(String.class),
        ;

        private final Class<?> checkClass;

        NBTTag(@Nullable Class<?> checkClass) {
            this.checkClass = checkClass;
        }

        /**
         * Some cases, that check class can be null
         * Like: COMPOUND
         *
         * @return the check class, you can use this to check if the values are at the correct instance
         */
        @Nullable
        public Class<?> getCheckClass() {
            return checkClass;
        }
    }
    enum NBTListAction {
        ADD,
        REMOVE,
        TRANSLATED_LIST,
        SET,
        CLEAR,
        ;
    }
    enum NBTCompoundAction {
        SET,
        REMOVE,
        CONTAINS,
        IS_EMPTY,
        GET,
        KEY_SET,
        ;
    }

    // ItemStack
    void setItemDisplayName(@NotNull ItemStack itemStack, @Nullable BaseComponent name);

    void setItemLore(@NotNull ItemStack itemStack, @NotNull BaseComponent[] lore);

    // Bukkit ItemStack
    void setItemBukkitDisplayName(@NotNull org.bukkit.inventory.ItemStack itemStack, @Nullable BaseComponent name);

    void setItemBukkitLore(@NotNull org.bukkit.inventory.ItemStack itemStack, @NotNull BaseComponent[] lore);
    // ItemStack

    // Player
    @NotNull Locale getPlayerMinecraftLocale(@NotNull Player player);
    // Player

    // Chat
    @NotNull IChatBaseComponent baseComponentToIChatComponent(@NotNull BaseComponent... components);
    @NotNull BaseComponent[] iChatComponentToBaseComponent(@NotNull IChatBaseComponent iChatBaseComponent);
    // Chat

    // Packet
    @NotNull PacketPlayOutSetSlot createSetSlotPacket(int windowId, int slot, int state, @NotNull ItemStack itemStack);
    // Packet

    void translateInventory(@NotNull Player player);

}
