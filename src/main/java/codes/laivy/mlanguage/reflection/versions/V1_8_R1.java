package codes.laivy.mlanguage.reflection.versions;

import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.lang.Locale;
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
import codes.laivy.mlanguage.reflection.executors.*;
import codes.laivy.mlanguage.reflection.objects.*;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import com.google.gson.JsonElement;
import io.netty.channel.Channel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.*;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.MODE;
import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class V1_8_R1 implements Version {

    private final @NotNull Map<@NotNull String, @NotNull ClassExecutor> classes = new HashMap<>();
    private final @NotNull Map<@NotNull String, @NotNull MethodExecutor> methods = new HashMap<>();
    private final @NotNull Map<@NotNull String, @NotNull FieldExecutor> fields = new HashMap<>();

    @Override
    public boolean onLoad(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        return true;
    }

    @Override
    public void load(@NotNull Class<? extends Version> version, @NotNull String key, @NotNull Executor executor) {
        if (onLoad(version, key, executor)) {
            executor.load();

            if (executor instanceof ClassExecutor) {
                classes.put(key, (ClassExecutor) executor);
            } else if (executor instanceof MethodExecutor) {
                methods.put(key, (MethodExecutor) executor);
            } else if (executor instanceof FieldExecutor) {
                fields.put(key, (FieldExecutor) executor);
            } else {
                throw new IllegalArgumentException("Couldn't find this executor's type");
            }
        }
    }

    @Override
    public boolean isCompatible(@NotNull String version) {
        return version.equals("V1_8_R1");
    }

    @Override
    public void loadClasses() {
        // NBT
        load(V1_8_R1.class, "NBTBase", new NBTBase.NBTBaseClass("net.minecraft.server.v1_8_R1.NBTBase"));

        load(V1_8_R1.class, "NBTBase:NBTTagByte", new NBTTagByte.NBTTagByteClass("net.minecraft.server.v1_8_R1.NBTTagByte"));
        load(V1_8_R1.class, "NBTBase:NBTTagByteArray", new NBTTagByteArray.NBTTagByteArrayClass("net.minecraft.server.v1_8_R1.NBTTagByteArray"));
        load(V1_8_R1.class, "NBTBase:NBTTagCompound", new NBTTagCompound.NBTTagCompoundClass("net.minecraft.server.v1_8_R1.NBTTagCompound"));
        load(V1_8_R1.class, "NBTBase:NBTTagDouble", new NBTTagDouble.NBTTagDoubleClass("net.minecraft.server.v1_8_R1.NBTTagDouble"));
        load(V1_8_R1.class, "NBTBase:NBTTagFloat", new NBTTagFloat.NBTTagFloatClass("net.minecraft.server.v1_8_R1.NBTTagFloat"));
        load(V1_8_R1.class, "NBTBase:NBTTagInt", new NBTTagInt.NBTTagIntClass("net.minecraft.server.v1_8_R1.NBTTagInt"));
        load(V1_8_R1.class, "NBTBase:NBTTagIntArray", new NBTTagIntArray.NBTTagIntArrayClass("net.minecraft.server.v1_8_R1.NBTTagIntArray"));
        load(V1_8_R1.class, "NBTBase:NBTTagList", new NBTTagList.NBTTagListClass("net.minecraft.server.v1_8_R1.NBTTagList"));
        load(V1_8_R1.class, "NBTBase:NBTTagLong", new NBTTagLong.NBTTagLongClass("net.minecraft.server.v1_8_R1.NBTTagLong"));
        load(V1_8_R1.class, "NBTBase:NBTTagShort", new NBTTagShort.NBTTagShortClass("net.minecraft.server.v1_8_R1.NBTTagShort"));
        load(V1_8_R1.class, "NBTBase:NBTTagString", new NBTTagString.NBTTagStringClass("net.minecraft.server.v1_8_R1.NBTTagString"));
        // Items
        load(V1_8_R1.class, "ItemStack", new ItemStack.ItemStackClass("net.minecraft.server.v1_8_R1.ItemStack"));
        load(V1_8_R1.class, "CraftItemStack", new CraftItemStack.CraftItemStackClass("org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack"));
        // Packets
        load(V1_8_R1.class, "Packet", new Packet.PacketClass("net.minecraft.server.v1_8_R1.Packet"));
        load(V1_8_R1.class, "PacketPlayOutSetSlot", new PacketPlayOutSetSlot.PacketPlayOutSetSlotClass("net.minecraft.server.v1_8_R1.PacketPlayOutSetSlot"));
        // Player
        load(V1_8_R1.class, "EntityPlayer", new EntityPlayer.EntityPlayerClass("net.minecraft.server.v1_8_R1.EntityPlayer"));
        load(V1_8_R1.class, "CraftPlayer", new CraftPlayer.CraftPlayerClass("org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer"));
        load(V1_8_R1.class, "PlayerConnection", new PlayerConnection.PlayerConnectionClass("net.minecraft.server.v1_8_R1.PlayerConnection"));
        load(V1_8_R1.class, "NetworkManager", new NetworkManager.NetworkManagerClass("net.minecraft.server.v1_8_R1.NetworkManager"));
        // Inventory
        load(V1_8_R1.class, "Container", new Container.ContainerClass("net.minecraft.server.v1_8_R1.Container"));
        // Chat
        load(V1_8_R1.class, "IChatBaseComponent", new IChatBaseComponent.IChatBaseComponentClass("net.minecraft.server.v1_8_R1.IChatBaseComponent"));
        load(V1_8_R1.class, "ChatSerializer", new IChatBaseComponent.ChatSerializerClass("net.minecraft.server.v1_8_R1.ChatSerializer"));
    }

    @Override
    public void loadMethods() {
        // NBT
        load(V1_8_R1.class, "NBTTagCompound:set", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.VOID, "set", "Sets a value inside a NBTTagCompound", ClassExecutor.STRING, getClassExec("NBTBase")));
        load(V1_8_R1.class, "NBTTagCompound:get", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), getClassExec("NBTBase"), "get", "Gets a value inside a NBTTagCompound", ClassExecutor.STRING));
        load(V1_8_R1.class, "NBTTagCompound:remove", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.VOID, "remove", "Removes a value from a NBTTagCompound", ClassExecutor.STRING));
        load(V1_8_R1.class, "NBTTagCompound:contains", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.BOOLEAN, "hasKey", "Check if a NBTTagCompound contains a key", ClassExecutor.STRING));
        load(V1_8_R1.class, "NBTTagCompound:isEmpty", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), ClassExecutor.BOOLEAN, "isEmpty", "Check if a NBTTagCompound is empty"));
        load(V1_8_R1.class, "NBTTagCompound:keySet", new MethodExecutor(getClassExec("NBTBase:NBTTagCompound"), new ClassExecutor(Set.class) {}, "c", "Gets a NBTTagCompound's keys"));
        // Item
        load(V1_8_R1.class, "CraftItemStack:asCraftMirror", new MethodExecutor(getClassExec("CraftItemStack"), getClassExec("CraftItemStack"), "asCraftMirror", "Gets the CraftItemStack from a NMS ItemStack", getClassExec("ItemStack")));
        load(V1_8_R1.class, "CraftItemStack:asNMSCopy", new MethodExecutor(getClassExec("CraftItemStack"), getClassExec("ItemStack"), "asNMSCopy", "Gets a NMS ItemStack from a Craft ItemStack", ClassExecutor.ITEMSTACK));
        // Player
        load(V1_8_R1.class, "CraftPlayer:getHandle", new MethodExecutor(getClassExec("CraftPlayer"), getClassExec("EntityPlayer"), "getHandle", "Gets the EntityPlayer of a CraftPlayer"));
        load(V1_8_R1.class, "PlayerConnection:sendPacket", new MethodExecutor(getClassExec("PlayerConnection"), ClassExecutor.VOID, "sendPacket", "Sends a packet to a PlayerConnection", getClassExec("Packet")));
        // Chat
        load(V1_8_R1.class, "ChatSerializer:convertToBase", new MethodExecutor(getClassExec("IChatBaseComponent"), ClassExecutor.STRING, "getText", "Converts a IChatBaseComponent to a string"));
        load(V1_8_R1.class, "ChatSerializer:convertToComponent", new MethodExecutor(getClassExec("ChatSerializer"), getClassExec("IChatBaseComponent"), "a", "Converts a string to a IChatBaseComponent", ClassExecutor.STRING));
    }

    @Override
    public void loadFields() {
        // NBT
        load(V1_8_R1.class, "NBTTagList:list", new FieldExecutor(getClassExec("NBTBase:NBTTagList"), new ClassExecutor(List.class) {}, "list", "Gets the list of a NBTTagList"));
        load(V1_8_R1.class, "NBTTagString:getData", new FieldExecutor(getClassExec("NBTBase:NBTTagString"), ClassExecutor.STRING, "data", "Gets a NBTTagString's data"));
        // Item
        load(V1_8_R1.class, "ItemStack:tag", new FieldExecutor(getClassExec("ItemStack"), getClassExec("NBTBase:NBTTagCompound"), "tag", "Gets the item NBT tag"));
        // Packets
        load(V1_8_R1.class, "PacketPlayOutSetSlot:windowId", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "a", "Gets the windowId of the PacketPlayOutSetSlot packet"));
        load(V1_8_R1.class, "PacketPlayOutSetSlot:slot", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), ClassExecutor.INT, "b", "Gets the slot of the PacketPlayOutSetSlot packet"));
        load(V1_8_R1.class, "PacketPlayOutSetSlot:item", new FieldExecutor(getClassExec("PacketPlayOutSetSlot"), getClassExec("ItemStack"), "c", "Gets the item of the PacketPlayOutSetSlot packet"));
        // Player
        load(V1_8_R1.class, "EntityPlayer:locale", new FieldExecutor(getClassExec("EntityPlayer"), ClassExecutor.STRING, "locale", "Gets the locale of an EntityPlayer"));
        load(V1_8_R1.class, "EntityPlayer:playerConnection", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("PlayerConnection"), "playerConnection", "Gets the PlayerConnection of an EntityPlayer"));
        load(V1_8_R1.class, "EntityPlayer:activeContainer", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("Container"), "activeContainer", "Gets the active container inventory of an EntityPlayer"));
        load(V1_8_R1.class, "EntityPlayer:defaultContainer", new FieldExecutor(getClassExec("EntityPlayer"), getClassExec("Container"), "defaultContainer", "Gets the default container inventory of an EntityPlayer"));
        load(V1_8_R1.class, "PlayerConnection:networkManager", new FieldExecutor(getClassExec("PlayerConnection"), getClassExec("NetworkManager"), "networkManager", "Gets the NetworkManager of a PlayerConnection"));
        load(V1_8_R1.class, "NetworkManager:channel", new FieldExecutor(getClassExec("NetworkManager"), new ClassExecutor(Channel.class), "i", "Gets the Channel of a NetworkManager"));
        // Container
        load(V1_8_R1.class, "Container:windowId", new FieldExecutor(getClassExec("Container"), ClassExecutor.INT, "windowId", "Gets the id of a Container"));
    }

    @Override
    public @NotNull PacketPlayOutSetSlot createSetSlotPacket(int windowId, int slot, int state, @NotNull ItemStack itemStack) {
        return new PacketPlayOutSetSlot(getClassExec("PacketPlayOutSetSlot").getConstructor(ClassExecutor.INT, ClassExecutor.INT, multiplesLanguagesBukkit().getVersion().getClassExec("ItemStack")).newInstance(new IntegerObjExec(windowId), new IntegerObjExec(slot), itemStack));
    }

    @Override
    public void translateInventory(@NotNull Player player) {
        BukkitItemTranslator translator = multiplesLanguagesBukkit().getApi().getItemTranslator();
        // translator.translate(ItemStack, Player, windowId, slot, state (ignore esse))

        try {
            EntityPlayer entityPlayer = EntityPlayer.getEntityPlayer(player);
            Set<PacketPlayOutSetSlot> packets = new LinkedHashSet<>();

            PlayerInventory playerInventory = player.getInventory();
            InventoryView view = player.getOpenInventory();
            // Armor translation
            int slot = view.getTopInventory().getSize();
            if (view.getTopInventory().getType() != InventoryType.CRAFTING) {
                slot += 5;
            }

            Bukkit.broadcastMessage("Bottom: '" + view.getBottomInventory().getSize() + "', Top: '" + view.getTopInventory().getSize() + "'");

            List<org.bukkit.inventory.ItemStack> armors = Arrays.asList(playerInventory.getArmorContents());
            Collections.reverse(armors);
            for (org.bukkit.inventory.ItemStack armor : armors) {
                if (armor != null && armor.getType() != Material.AIR) {
                    if (translator.isTranslatable(armor)) {
                        packets.add(translator.translate(armor, player, 0, slot, -1));
                        translator.reset(armor);
                    }
                }
                slot++;
            }
            // Inventory translation
            for (org.bukkit.inventory.ItemStack item : playerInventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    if (translator.isTranslatable(item)) {
                        final int dSlot;

                        if (slot == 49 && ReflectionUtils.isCompatible(V1_9_R1.class)) {
                            dSlot = 45;
                        } else if ((slot - 9) < ((view.getTopInventory().getSize() + 5) + 9)) {
                            dSlot = slot + 27 + (view.getTopInventory().getType() != InventoryType.CRAFTING ? -9 : 0);
                        } else {
                            dSlot = slot + 9 + (view.getTopInventory().getType() != InventoryType.CRAFTING ? -27 : -18);
                        }

                        if (item.getType() != Material.WOOL) {
                            boolean breaked = ((slot - 9) < ((view.getTopInventory().getSize() + 5) + 9));
                            Bukkit.broadcastMessage("Changed slot: '" + dSlot + "', original: '" + slot + "', item: '" + item.getAmount() + " " + item.getType().name().toLowerCase() + "', break: '" + ((breaked ? "§a" : "§c") + breaked) + "§f', break spot: '" + ((view.getTopInventory().getSize() + 5) + 9) + "', current: '" + (slot - 9) + "'");
                        }

                        packets.add(translator.translate(item.clone(), player, entityPlayer.getActiveContainer().getId(), dSlot, -1));
                        translator.reset(item);
                    }
                }
                slot++;
            }
            // Opened inventory translation
            for (int row = 0; row < view.getTopInventory().getSize(); row++) {
                org.bukkit.inventory.ItemStack item = view.getTopInventory().getItem(row);

                if (item != null && item.getType() != Material.AIR) {
                    if (translator.isTranslatable(item)) {
                        packets.add(translator.translate(item.clone(), player, entityPlayer.getActiveContainer().getId(), row, -1));
                        translator.reset(item);
                    }
                }
            }
            // Sending packets
            for (Packet packet : packets) {
                entityPlayer.getConnection().sendPacket(packet);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public @NotNull Map<String, ClassExecutor> getClasses() {
        return classes;
    }

    @Override
    public @NotNull Map<String, MethodExecutor> getMethods() {
        return methods;
    }

    @Override
    public @NotNull Map<String, FieldExecutor> getFields() {
        return fields;
    }

    @Override
    public void nbtbaseConcatenate(@NotNull NBTBase into, @NotNull Object from) {
        if (into instanceof NBTTagCompound && from instanceof NBTTagCompound) {
            NBTTagCompound i = (NBTTagCompound) into;
            NBTTagCompound f = (NBTTagCompound) from;

            for (String key : f.keySet()) {
                i.set(key, Objects.requireNonNull(nbtCompound(Version.NBTCompoundAction.GET, f, new StringObjExec(key), null)));
            }
        } else if (into instanceof NBTTagList && from instanceof List) {
            NBTTagList i = (NBTTagList) into;
            //noinspection unchecked
            List<NBTBase> f = (List<NBTBase>) from;

            List<Object> list = new ArrayList<>();
            for (NBTBase base : f) {
                list.add(base.getValue());
            }

            nbtList(NBTListAction.SET, i, list);
        } else {
            throw new IllegalArgumentException("Cannot concatenate NBT bases. Into: '" + into.getClassExecutor().getName() + "', From: '" + from.getClass().getName() + "'");
        }
    }

    @Override
    public @Nullable Object nbtList(@NotNull NBTListAction action, @NotNull NBTTagList object, @Nullable Object value) {
        if (value == null && action != NBTListAction.TRANSLATED_LIST && action != NBTListAction.CLEAR) {
            throw new NullPointerException("The value cannot be null if (action != TRANSLATED_LIST) or (action != CLEAR)");
        }

        if (action == NBTListAction.ADD) {
            if (value instanceof NBTBase) {
                FieldExecutor field = getFieldExec("NBTTagList:list");

                //noinspection unchecked
                List<Object> nbtBaseList = (List<Object>) field.invokeInstance(object);

                if (nbtBaseList != null) {
                    nbtBaseList.add(value);
                } else {
                    field.set(object, new ArrayList<Object>() {{add(value);}});
                }
            } else {
                throw new IllegalArgumentException("The value needs to be a '" + getClassExec("NBTBase:NBTTagList").getName() + "', and the passed is '" + value.getClass().getName() + "'");
            }
        } else if (action == NBTListAction.REMOVE) {
            if (value instanceof NBTBase) {
                FieldExecutor field = getFieldExec("NBTTagList:list");

                //noinspection unchecked
                List<Object> nbtBaseList = (List<Object>) field.invokeInstance(object);

                if (nbtBaseList != null) {
                    nbtBaseList.remove(value);
                }
            } else {
                throw new IllegalArgumentException("The value needs to be a '" + getClassExec("NBTBase:NBTTagList").getName() + "', and the passed is '" + value.getClass().getName() + "'");
            }
        } else if (action == NBTListAction.TRANSLATED_LIST) {
            FieldExecutor field = getFieldExec("NBTTagList:list");
            //noinspection unchecked
            List<Object> nbtBaseList = (List<Object>) field.invokeInstance(object);
            List<NBTBase> returnList = new ArrayList<>();

            if (nbtBaseList != null) {
                for (Object nbtbase : nbtBaseList) {
                    returnList.add(nbtTag(NBTBase.getTagType(nbtbase), nbtbase));
                }
                return returnList;
            } else {
                return new ArrayList<NBTBase>();
            }
        } else if (action == NBTListAction.CLEAR) {
            FieldExecutor field = getFieldExec("NBTTagList:list");
            field.set(object, new ArrayList<>());
        } else if (action == NBTListAction.SET) {
            if (value instanceof List) {
                FieldExecutor field = getFieldExec("NBTTagList:list");
                field.set(object, value);
            } else {
                throw new IllegalArgumentException("The parameter inserted is '" + value.getClass().getName() + "', and needs to be 'List'");
            }
        } else {
            throw new IllegalArgumentException("Cannot invoke nbtList() method due to an rare and unknown error");
        }

        return null;
    }

    @Override
    public @NotNull NBTBase nbtTag(@NotNull NBTTag tag, @NotNull Object... objects) {
        try {
            Object object;
            Constructor<?> constructor = null;

            if (objects.length > 0 && getClassExec("NBTBase").isReflectiveInstance(objects[0])) {
                object = objects[0];

                if (tag == NBTTag.BYTE) {
                    if (!getClassExec("NBTBase:NBTTagByte").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagByte.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.BYTE_ARRAY) {
                    if (!getClassExec("NBTBase:NBTTagByteArray").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagByteArray.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.COMPOUND) {
                    if (!getClassExec("NBTBase:NBTTagCompound").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagCompound.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.DOUBLE) {
                    if (!getClassExec("NBTBase:NBTTagDouble").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagDouble.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.FLOAT) {
                    if (!getClassExec("NBTBase:NBTTagFloat").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagFloat.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.INT) {
                    if (!getClassExec("NBTBase:NBTTagInt").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagInt.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.INT_ARRAY) {
                    if (!getClassExec("NBTBase:NBTTagIntArray").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagIntArray.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.LIST) {
                    if (!getClassExec("NBTBase:NBTTagList").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagList.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.LONG) {
                    if (!getClassExec("NBTBase:NBTTagLong").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagLong.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.SHORT) {
                    if (!getClassExec("NBTBase:NBTTagShort").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagShort.class.getDeclaredConstructor(Object.class);
                    }
                } else if (tag == NBTTag.STRING) {
                    if (!getClassExec("NBTBase:NBTTagString").isReflectiveInstance(objects[0])) {
                        constructor = NBTTagString.class.getDeclaredConstructor(Object.class);
                    }
                } else {
                    throw new IllegalArgumentException("Cannot execute nbtTag²() due to an unknown and rare error");
                }

                if (constructor == null) {
                    throw new IllegalArgumentException("Wrong parameter type! To create a '" + tag.name() + "' base, the parameter needs to be '" + (tag.getCheckClass() != null ? tag.getCheckClass().getName() : tag.name()) + "', and you passed '" + objects[0].getClass().getName() + "'");
                }
            } else {
                if (objects.length == 0 && tag != NBTTag.LIST && tag != NBTTag.COMPOUND) {
                    throw new NullPointerException("Wrong number of arguments! To create a the '" + tag.name() + "' base you need atleast 1 base parameter!");
                } if (tag == NBTTag.COMPOUND && objects.length > 0 && !(getClassExec("NBTBase:NBTTagCompound").isReflectiveInstance(objects[0]))) {
                    throw new IllegalArgumentException("Wrong parameter type! To create a 'NBTTagCompound' base, the parameter needs to be 'NBTTagCompound', and you passed '" + objects[0].getClass().getName() + "'");
                } if (tag == NBTTag.LIST && objects.length > 0 && !(objects[0] instanceof List)) {
                    throw new IllegalArgumentException("Wrong parameter type! To create a 'NBTTagList' base, the parameter needs to be 'List', and you passed '" + objects[0].getClass().getName() + "'");
                }

                if (tag == NBTTag.BYTE) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagByte").getConstructor(ClassExecutor.BYTE);
                    object = c.newInstance(new ByteObjExec((byte) objects[0]));
                    constructor = NBTTagByte.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.BYTE_ARRAY) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagByteArray").getConstructor(ClassExecutor.BYTE_ARRAY);
                    object = c.newInstance(new ByteArrayObjExec((byte[]) objects[0]));
                    constructor = NBTTagByteArray.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.COMPOUND) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagCompound").getConstructor();
                    object = c.newInstance();
                    constructor = NBTTagCompound.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.DOUBLE) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagDouble").getConstructor(ClassExecutor.DOUBLE);
                    object = c.newInstance(new DoubleObjExec((double) objects[0]));
                    constructor = NBTTagDouble.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.FLOAT) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagFloat").getConstructor(ClassExecutor.FLOAT);
                    object = c.newInstance(new FloatObjExec((float) objects[0]));
                    constructor = NBTTagFloat.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.INT) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagInt").getConstructor(ClassExecutor.INT);
                    object = c.newInstance(new IntegerObjExec((int) objects[0]));
                    constructor = NBTTagInt.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.INT_ARRAY) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagIntArray").getConstructor(ClassExecutor.INT_ARRAY);
                    object = c.newInstance(new IntegerArrayObjExec((int[]) objects[0]));
                    constructor = NBTTagIntArray.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.LIST) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagList").getConstructor();
                    object = c.newInstance();
                    constructor = NBTTagList.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.LONG) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagLong").getConstructor(ClassExecutor.LONG);
                    object = c.newInstance(new LongObjExec((long) objects[0]));
                    constructor = NBTTagLong.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.SHORT) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagShort").getConstructor(ClassExecutor.SHORT);
                    object = c.newInstance(new ShortObjExec((short) objects[0]));
                    constructor = NBTTagShort.class.getDeclaredConstructor(Object.class);
                } else if (tag == NBTTag.STRING) {
                    ClassConstructor c = getClassExec("NBTBase:NBTTagString").getConstructor(ClassExecutor.STRING);
                    object = c.newInstance(new StringObjExec((String) objects[0]));
                    constructor = NBTTagString.class.getDeclaredConstructor(Object.class);
                } else {
                    throw new IllegalStateException("Cannot invoke nbtTag() method due to an invalid tag parameter error");
                }
            }

            constructor.setAccessible(true);
            Object base = constructor.newInstance(object);

            if (base instanceof NBTTagCompound) {
                if (objects.length > 0) {
                    NBTTagCompound compound = (NBTTagCompound) base;
                    nbtbaseConcatenate(compound, objects[0]);
                }
            } else if (base instanceof NBTTagList) {
                if (objects.length > 0) {
                    NBTTagList list = (NBTTagList) base;
                    nbtbaseConcatenate(list, objects[0]);
                }
            }

            return (NBTBase) base;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable Object nbtCompound(@NotNull NBTCompoundAction action, @NotNull NBTTagCompound object, @Nullable StringObjExec key, @Nullable NBTBase value) {
        if (action != NBTCompoundAction.IS_EMPTY && action != NBTCompoundAction.KEY_SET && key == null) {
            throw new NullPointerException("The key cannot be null if action != IS_EMPTY or action != KEY_SET!");
        } if (action != NBTCompoundAction.SET && value != null) {
            throw new NullPointerException("The value needs to be null if action != SET");
        } if (action == NBTCompoundAction.SET && value == null) {
            throw new NullPointerException("The NBTBase cannot be null in SET actions!");
        }

        if (action == NBTCompoundAction.SET) {
            getMethodExec("NBTTagCompound:set").invokeInstance(object, key, value);
        } else if (action == NBTCompoundAction.REMOVE) {
            getMethodExec("NBTTagCompound:remove").invokeInstance(object, key);
        } else if (action == NBTCompoundAction.CONTAINS) {
            return getMethodExec("NBTTagCompound:contains").invokeInstance(object, key);
        } else if (action == NBTCompoundAction.IS_EMPTY) {
            return getMethodExec("NBTTagCompound:isEmpty").invokeInstance(object);
        } else if (action == NBTCompoundAction.GET) {
            return getMethodExec("NBTTagCompound:get").invokeInstance(object, key);
        } else if (action == NBTCompoundAction.KEY_SET) {
            return getMethodExec("NBTTagCompound:keySet").invokeInstance(object);
        } else {
            throw new IllegalStateException("Cannot invoke nbtCompound() method due to an rare and unknown error");
        }

        return null;
    }

    @Override
    public @NotNull String getName() {
        return "V1_8_R1";
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
            display.set("Name", new NBTTagString(name.toLegacyText()));
        } else {
            display.remove("Name");
        }

        tag.set("display", display);
        itemStack.setTag(tag);
    }

    @Override
    public void setItemLore(@NotNull ItemStack itemStack, @NotNull BaseComponent[] lore) {
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

        if (lore != null) {
            List<NBTBase> loreBase = new LinkedList<>();
            for (BaseComponent line : lore) {
                loreBase.add(multiplesLanguagesBukkit().getVersion().nbtTag(NBTTag.STRING, line.toLegacyText()));
            }
            display.set("Lore", new NBTTagList(loreBase));
        } else {
            display.remove("Lore");
        }

        tag.set("display", display);
        itemStack.setTag(tag);
    }

    @Override
    public @NotNull IChatBaseComponent baseComponentToIChatComponent(@NotNull BaseComponent... components) {
        return new IChatBaseComponent(getMethodExec("ChatSerializer:convertToComponent").invokeStatic(new StringObjExec(ComponentSerializer.toString(components))));
    }

    @Override
    public @NotNull BaseComponent[] iChatComponentToBaseComponent(@NotNull IChatBaseComponent iChatBaseComponent) {
        JsonElement element = (JsonElement) Objects.requireNonNull(getMethodExec("ChatSerializer:convertToBase").invokeInstance(iChatBaseComponent));
        List<BaseComponent> components = Arrays.asList(ComponentSerializer.parse(element.toString()));
        return components.toArray(new BaseComponent[0]);
    }

    @Override
    public void setItemBukkitDisplayName(org.bukkit.inventory.@NotNull ItemStack itemStack, @Nullable BaseComponent name) {
        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();

            if (name != null) {
                meta.setDisplayName(name.toLegacyText());
            } else {
                meta.setDisplayName(null);
            }

            itemStack.setItemMeta(meta);
        }
    }

    @Override
    public void setItemBukkitLore(org.bukkit.inventory.@NotNull ItemStack itemStack, @NotNull BaseComponent[] lore) {
        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();

            if (meta != null) {
                if (lore != null) {
                    List<String> loreStr = new LinkedList<>();
                    for (BaseComponent component : lore) {
                        loreStr.add(component.toLegacyText());
                    }

                    meta.setLore(loreStr);
                } else {
                    meta.setLore(null);
                }

                itemStack.setItemMeta(meta);
            }
        }
    }

    @Override
    public @NotNull Locale getPlayerMinecraftLocale(@NotNull Player player) {
        return Locale.getByCode(player.spigot().getLocale());
    }
}
