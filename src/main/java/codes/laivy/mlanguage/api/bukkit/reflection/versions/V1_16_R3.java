package codes.laivy.mlanguage.api.bukkit.reflection.versions;

import codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.*;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutWindowItems;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory.Slot;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.Executor;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.chat.IChatBaseComponent;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.CraftItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.CraftMetaItem;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.NBTBase;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.Packet;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.CraftPlayer;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.NetworkManager;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.PlayerConnection;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.inventory.Container;
import codes.laivy.mlanguage.api.bukkit.reflection.executors.FieldExecutor;
import codes.laivy.mlanguage.utils.ComponentUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

public class V1_16_R3 extends V1_16_R2 {

    public V1_16_R3(@NotNull BukkitMultiplesLanguagesAPI api) {
        super(api);
    }

    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_16_R2.class) {
            if (executor instanceof ClassExecutor) {
                return false;
            }
        } else if (version == V1_13_R1.class) {
            if (executor instanceof FieldExecutor) {
                if (key.equals("CraftMetaItem:displayName")) {
                    return false;
                }
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public void setCraftItemMetaDisplayName(@NotNull CraftMetaItem item, @NotNull BaseComponent[] name) {
        if (name != null) {
            getDefApi().getVersion().getFieldExec("CraftMetaItem:displayName").set(item, ComponentUtils.serialize(name));
        } else {
            getDefApi().getVersion().getFieldExec("CraftMetaItem:displayName").set(item, null);
        }
    }
    public void setCraftItemMetaLore(@NotNull CraftMetaItem item, @NotNull BaseComponent[] lore) {
        if (lore != null) {
            List<Object> objects = new LinkedList<>();
            for (BaseComponent component : lore) {
                objects.add(ComponentSerializer.toString(component));
            }

            getDefApi().getVersion().getFieldExec("CraftMetaItem:lore").set(item, objects);
        } else {
            getDefApi().getVersion().getFieldExec("CraftMetaItem:lore").set(item, null);
        }
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_16_R3.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.server.v1_16_R3.NBTBase"));

        load(V1_16_R3.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.server.v1_16_R3.NBTTagByte"));
        load(V1_16_R3.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.server.v1_16_R3.NBTTagByteArray"));
        load(V1_16_R3.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.server.v1_16_R3.NBTTagCompound"));
        load(V1_16_R3.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.server.v1_16_R3.NBTTagDouble"));
        load(V1_16_R3.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.server.v1_16_R3.NBTTagFloat"));
        load(V1_16_R3.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.server.v1_16_R3.NBTTagInt"));
        load(V1_16_R3.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.server.v1_16_R3.NBTTagIntArray"));
        load(V1_16_R3.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.server.v1_16_R3.NBTTagList"));
        load(V1_16_R3.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.server.v1_16_R3.NBTTagLong"));
        load(V1_16_R3.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.server.v1_16_R3.NBTTagShort"));
        load(V1_16_R3.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.server.v1_16_R3.NBTTagString"));
        // Items
        load(V1_16_R3.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.server.v1_16_R3.ItemStack"));
        load(V1_16_R3.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack"));
        load(V1_16_R3.class, "CraftMetaItem", new CraftMetaItem.CraftMetaItemClass("org.bukkit.craftbukkit.v1_16_R3.inventory.CraftMetaItem"));
        // Packets
        load(V1_16_R3.class, "Packet", new Packet.PacketClass("net.minecraft.server.v1_16_R3.Packet"));
        load(V1_16_R3.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot"));
        load(V1_16_R3.class, "PacketPlayOutWindowItems", new PacketPlayOutWindowItems.PacketPlayOutWindowItemsClass("net.minecraft.server.v1_16_R3.PacketPlayOutWindowItems"));
        // Player
        load(V1_16_R3.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.v1_16_R3.EntityPlayer"));
        load(V1_16_R3.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer"));
        load(V1_16_R3.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.v1_16_R3.PlayerConnection"));
        load(V1_16_R3.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.server.v1_16_R3.NetworkManager"));
        // Inventory
        load(V1_16_R3.class, "Container", new Container.ContainerClass("net.minecraft.server.v1_16_R3.Container"));
        load(V1_16_R3.class, "Slot", new Slot.SlotClass("net.minecraft.server.v1_16_R3.Slot"));
        // Chat
        load(V1_16_R3.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.server.v1_16_R3.IChatBaseComponent"));
        load(V1_16_R3.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.server.v1_16_R3.IChatBaseComponent$ChatSerializer"));
    }

    @Override
    public void loadMethods() {
        super.loadMethods();

        load(V1_16_R3.class, "CraftMetaItem:displayName", new FieldExecutor(getClassExec("CraftMetaItem"), ClassExecutor.STRING, "displayName", "Gets the component display name of a ItemMeta"));
    }
}
