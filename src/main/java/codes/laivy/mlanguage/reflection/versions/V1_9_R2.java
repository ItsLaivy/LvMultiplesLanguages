package codes.laivy.mlanguage.reflection.versions;

import codes.laivy.mlanguage.reflection.Version;
import codes.laivy.mlanguage.reflection.classes.chat.IChatBaseComponent;
import codes.laivy.mlanguage.reflection.classes.item.CraftItemStack;
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
import org.jetbrains.annotations.NotNull;

public class V1_9_R2 extends V1_9_R1 {
    
    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_9_R1.class) {
            if (executor instanceof ClassExecutor) {
                return false;
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_9_R2.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.server.v1_9_R2.NBTBase"));

        load(V1_9_R2.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.server.v1_9_R2.NBTTagByte"));
        load(V1_9_R2.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.server.v1_9_R2.NBTTagByteArray"));
        load(V1_9_R2.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.server.v1_9_R2.NBTTagCompound"));
        load(V1_9_R2.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.server.v1_9_R2.NBTTagDouble"));
        load(V1_9_R2.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.server.v1_9_R2.NBTTagFloat"));
        load(V1_9_R2.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.server.v1_9_R2.NBTTagInt"));
        load(V1_9_R2.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.server.v1_9_R2.NBTTagIntArray"));
        load(V1_9_R2.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.server.v1_9_R2.NBTTagList"));
        load(V1_9_R2.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.server.v1_9_R2.NBTTagLong"));
        load(V1_9_R2.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.server.v1_9_R2.NBTTagShort"));
        load(V1_9_R2.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.server.v1_9_R2.NBTTagString"));
        // Items
        load(V1_9_R2.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.server.v1_9_R2.ItemStack"));
        load(V1_9_R2.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack"));
        // Packets
        load(V1_9_R2.class, "Packet", new Packet.PacketClass("net.minecraft.server.v1_9_R2.Packet"));
        load(V1_9_R2.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.server.v1_9_R2.PacketPlayOutSetSlot"));
        // Player
        load(V1_9_R2.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.v1_9_R2.EntityPlayer"));
        load(V1_9_R2.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer"));
        load(V1_9_R2.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.v1_9_R2.PlayerConnection"));
        load(V1_9_R2.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.server.v1_9_R2.NetworkManager"));
        // Inventory
        load(V1_9_R2.class, "Container", new Container.ContainerClass("net.minecraft.server.v1_9_R2.Container"));
        load(V1_9_R2.class, "Slot", new Slot.SlotClass("net.minecraft.server.v1_9_R2.Slot"));
        // Chat
        load(V1_9_R2.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.server.v1_9_R2.IChatBaseComponent"));
        load(V1_9_R2.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.server.v1_9_R2.IChatBaseComponent$ChatSerializer"));
    }

}
