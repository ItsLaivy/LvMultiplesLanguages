package codes.laivy.mlanguage.reflection.versions;

import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.chat.IChatBaseComponent;
import codes.laivy.mlanguage.reflection.classes.item.CraftItemStack;
import codes.laivy.mlanguage.reflection.classes.item.CraftMetaItem;
import codes.laivy.mlanguage.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.reflection.classes.nbt.NBTBase;
import codes.laivy.mlanguage.reflection.classes.nbt.tags.*;
import codes.laivy.mlanguage.reflection.classes.packets.Packet;
import codes.laivy.mlanguage.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.reflection.classes.player.CraftPlayer;
import codes.laivy.mlanguage.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.reflection.classes.player.NetworkManager;
import codes.laivy.mlanguage.reflection.classes.player.PlayerConnection;
import codes.laivy.mlanguage.reflection.classes.player.inventory.Container;
import codes.laivy.mlanguage.reflection.classes.player.inventory.Slot;
import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.Executor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class V1_15_R1 extends V1_14_R1 {
    
    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_14_R1.class) {
            if (executor instanceof ClassExecutor) {
                return false;
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public @NotNull Locale getPlayerMinecraftLocale(@NotNull Player player) {
        try {
            Method method = player.getClass().getDeclaredMethod("getLocale");
            return Locale.getByCode((String) method.invoke(player));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_15_R1.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.server.v1_15_R1.NBTBase"));

        load(V1_15_R1.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.server.v1_15_R1.NBTTagByte"));
        load(V1_15_R1.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.server.v1_15_R1.NBTTagByteArray"));
        load(V1_15_R1.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.server.v1_15_R1.NBTTagCompound"));
        load(V1_15_R1.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.server.v1_15_R1.NBTTagDouble"));
        load(V1_15_R1.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.server.v1_15_R1.NBTTagFloat"));
        load(V1_15_R1.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.server.v1_15_R1.NBTTagInt"));
        load(V1_15_R1.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.server.v1_15_R1.NBTTagIntArray"));
        load(V1_15_R1.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.server.v1_15_R1.NBTTagList"));
        load(V1_15_R1.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.server.v1_15_R1.NBTTagLong"));
        load(V1_15_R1.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.server.v1_15_R1.NBTTagShort"));
        load(V1_15_R1.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.server.v1_15_R1.NBTTagString"));
        // Items
        load(V1_15_R1.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.server.v1_15_R1.ItemStack"));
        load(V1_15_R1.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack"));
        load(V1_15_R1.class, "CraftMetaItem", new CraftMetaItem.CraftMetaItemClass("org.bukkit.craftbukkit.v1_15_R1.inventory.CraftMetaItem"));
        // Packets
        load(V1_15_R1.class, "Packet", new Packet.PacketClass("net.minecraft.server.v1_15_R1.Packet"));
        load(V1_15_R1.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.server.v1_15_R1.PacketPlayOutSetSlot"));
        // Player
        load(V1_15_R1.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.v1_15_R1.EntityPlayer"));
        load(V1_15_R1.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer"));
        load(V1_15_R1.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.v1_15_R1.PlayerConnection"));
        load(V1_15_R1.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.server.v1_15_R1.NetworkManager"));
        // Inventory
        load(V1_15_R1.class, "Container", new Container.ContainerClass("net.minecraft.server.v1_15_R1.Container"));
        load(V1_15_R1.class, "Slot", new Slot.SlotClass("net.minecraft.server.v1_15_R1.Slot"));
        // Chat
        load(V1_15_R1.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.server.v1_15_R1.IChatBaseComponent"));
        load(V1_15_R1.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.server.v1_15_R1.IChatBaseComponent$ChatSerializer"));
    }

}
