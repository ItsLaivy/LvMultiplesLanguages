package codes.laivy.mlanguage.reflection.versions;

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
import codes.laivy.mlanguage.reflection.executors.ClassExecutor;
import codes.laivy.mlanguage.reflection.executors.Executor;
import codes.laivy.mlanguage.reflection.executors.FieldExecutor;
import codes.laivy.mlanguage.reflection.executors.MethodExecutor;
import codes.laivy.mlanguage.utils.ClassUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class V1_13_R1 extends V1_12_R1 {
    
    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (version == V1_12_R1.class) {
            if (executor instanceof ClassExecutor) {
                return false;
            }
        } else if (version == V1_8_R1.class) {
            if (executor instanceof MethodExecutor) {
                if (key.equals("NBTTagCompound:keySet")) {
                    return false;
                }
            }
        }

        return super.onLoad(version, key, executor);
    }

    @Override
    public void setItemDisplayName(@NotNull ItemStack itemStack, @Nullable BaseComponent name) {
        NBTTagCompound tag = itemStack.getTag();

        if (tag == null) {
            itemStack.setTag((NBTTagCompound) nbtTag(NBTTag.COMPOUND));
            tag = itemStack.getTag();
        }

        if (tag == null) {
            throw new NullPointerException("Couldn't get the NBT Tag of this itemstack");
        }

        NBTTagCompound display = (NBTTagCompound) nbtTag(NBTTag.COMPOUND);
        if (tag.contains("display")) {
            display = new NBTTagCompound(tag.get("display").getValue());
        }

        if (name != null) {
            display.set("Name", new NBTTagString(ComponentSerializer.toString(name)));
        } else {
            display.remove("Name");
        }

        tag.set("display", display);
        itemStack.setTag(tag);
    }

    @Override
    public void setItemBukkitDisplayName(org.bukkit.inventory.@NotNull ItemStack itemStack, @Nullable BaseComponent name) {
        if (ClassUtils.isInstanceOf(getClassExec("CraftMetaItem").getReflectionClass(), itemStack.getItemMeta().getClass())) {
            if (itemStack.hasItemMeta()) {
                CraftMetaItem itemMeta = new CraftMetaItem(itemStack.getItemMeta());

                if (name != null) {
                    itemMeta.setDisplayName(new BaseComponent[] { name });
                } else {
                    itemMeta.setDisplayName(null);
                }

                itemStack.setItemMeta((ItemMeta) itemMeta.getValue());

                return;
            }
        }
        super.setItemBukkitDisplayName(itemStack, name);
    }

    public void setCraftItemMetaDisplayName(@NotNull CraftMetaItem item, @NotNull BaseComponent[] name) {
        if (name != null) {
            multiplesLanguagesBukkit().getVersion().getFieldExec("CraftMetaItem:displayName").set(item, IChatBaseComponent.convert(name).getValue());
        } else {
            multiplesLanguagesBukkit().getVersion().getFieldExec("CraftMetaItem:displayName").set(item, null);
        }
    }
    public void setCraftItemMetaLore(@NotNull CraftMetaItem item, @NotNull BaseComponent[] lore) {
        if (lore != null) {
            List<Object> objects = new LinkedList<>();
            for (BaseComponent component : lore) {
                objects.add(component.toLegacyText());
            }

            multiplesLanguagesBukkit().getVersion().getFieldExec("CraftMetaItem:lore").set(item, objects);
        } else {
            multiplesLanguagesBukkit().getVersion().getFieldExec("CraftMetaItem:lore").set(item, null);
        }
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_13_R1.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.server.v1_13_R1.NBTBase"));

        load(V1_13_R1.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.server.v1_13_R1.NBTTagByte"));
        load(V1_13_R1.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.server.v1_13_R1.NBTTagByteArray"));
        load(V1_13_R1.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.server.v1_13_R1.NBTTagCompound"));
        load(V1_13_R1.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.server.v1_13_R1.NBTTagDouble"));
        load(V1_13_R1.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.server.v1_13_R1.NBTTagFloat"));
        load(V1_13_R1.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.server.v1_13_R1.NBTTagInt"));
        load(V1_13_R1.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.server.v1_13_R1.NBTTagIntArray"));
        load(V1_13_R1.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.server.v1_13_R1.NBTTagList"));
        load(V1_13_R1.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.server.v1_13_R1.NBTTagLong"));
        load(V1_13_R1.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.server.v1_13_R1.NBTTagShort"));
        load(V1_13_R1.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.server.v1_13_R1.NBTTagString"));
        // Items
        load(V1_13_R1.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.server.v1_13_R1.ItemStack"));
        load(V1_13_R1.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack"));
        load(V1_13_R1.class, "CraftMetaItem", new CraftMetaItem.CraftMetaItemClass("org.bukkit.craftbukkit.v1_13_R1.inventory.CraftMetaItem"));
        // Packets
        load(V1_13_R1.class, "Packet", new Packet.PacketClass("net.minecraft.server.v1_13_R1.Packet"));
        load(V1_13_R1.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.server.v1_13_R1.PacketPlayOutSetSlot"));
        // Player
        load(V1_13_R1.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.v1_13_R1.EntityPlayer"));
        load(V1_13_R1.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer"));
        load(V1_13_R1.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.v1_13_R1.PlayerConnection"));
        load(V1_13_R1.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.server.v1_13_R1.NetworkManager"));
        // Inventory
        load(V1_13_R1.class, "Container", new Container.ContainerClass("net.minecraft.server.v1_13_R1.Container"));
        // Chat
        load(V1_13_R1.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.server.v1_13_R1.IChatBaseComponent"));
        load(V1_13_R1.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.server.v1_13_R1.IChatBaseComponent$ChatSerializer"));
    }

    @Override
    public void loadMethods() {
        super.loadMethods();

        // NBT
        load(V1_13_R1.class, "NBTTagCompound:keySet", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), new ClassExecutor(Set.class) {}, "getKeys", "Gets a NBTTagCompound's keys"));
        // Craft meta item
        load(V1_13_R1.class, "CraftMetaItem:lore", new FieldExecutor(getClassExec("CraftMetaItem"), new ClassExecutor(List.class), "lore", "Gets the component lore of a ItemMeta"));
    }

    @Override
    public void loadFields() {
        super.loadFields();

        load(V1_13_R1.class, "CraftMetaItem:displayName", new FieldExecutor(getClassExec("CraftMetaItem"), getClassExec("IChatBaseComponent"), "displayName", "Gets the component display name of a ItemMeta"));
    }
}
