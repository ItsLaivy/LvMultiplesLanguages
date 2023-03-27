package codes.laivy.mlanguage.reflection.versions;

import codes.laivy.mlanguage.reflection.Version;
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
import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.Executor;
import codes.laivy.mlanguage.reflection.executors.FieldExecutor;
import io.netty.channel.Channel;
import org.jetbrains.annotations.NotNull;

public class V1_8_R3 extends V1_8_R2 {
    
    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_8_R2.class) {
            if (executor instanceof ClassExecutor) {
                return false;
            } else if (executor instanceof FieldExecutor) {
                return !key.equals("NetworkManager:channel");
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_8_R3.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.server.v1_8_R3.NBTBase"));

        load(V1_8_R3.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.server.v1_8_R3.NBTTagByte"));
        load(V1_8_R3.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.server.v1_8_R3.NBTTagByteArray"));
        load(V1_8_R3.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.server.v1_8_R3.NBTTagCompound"));
        load(V1_8_R3.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.server.v1_8_R3.NBTTagDouble"));
        load(V1_8_R3.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.server.v1_8_R3.NBTTagFloat"));
        load(V1_8_R3.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.server.v1_8_R3.NBTTagInt"));
        load(V1_8_R3.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.server.v1_8_R3.NBTTagIntArray"));
        load(V1_8_R3.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.server.v1_8_R3.NBTTagList"));
        load(V1_8_R3.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.server.v1_8_R3.NBTTagLong"));
        load(V1_8_R3.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.server.v1_8_R3.NBTTagShort"));
        load(V1_8_R3.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.server.v1_8_R3.NBTTagString"));
        // Items
        load(V1_8_R3.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.server.v1_8_R3.ItemStack"));
        load(V1_8_R3.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack"));
        // Packets
        load(V1_8_R3.class, "Packet", new Packet.PacketClass("net.minecraft.server.v1_8_R3.Packet"));
        load(V1_8_R3.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot"));
        // Player
        load(V1_8_R3.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.v1_8_R3.EntityPlayer"));
        load(V1_8_R3.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer"));
        load(V1_8_R3.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.v1_8_R3.PlayerConnection"));
        load(V1_8_R3.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.server.v1_8_R3.NetworkManager"));
        // Inventory
        load(V1_8_R3.class, "Container", new Container.ContainerClass("net.minecraft.server.v1_8_R3.Container"));
    }

    @Override
    public void loadFields() {
        super.loadFields();

        load(V1_8_R3.class, "NetworkManager:channel", new FieldExecutor(getClassExec("NetworkManager"), new ClassExecutor(Channel.class), "channel", "Gets the Channel of a NetworkManager"));
    }
}