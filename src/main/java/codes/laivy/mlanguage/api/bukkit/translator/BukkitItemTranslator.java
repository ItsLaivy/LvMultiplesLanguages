package codes.laivy.mlanguage.api.bukkit.translator;

import codes.laivy.mlanguage.api.bukkit.BukkitMessageStorage;
import codes.laivy.mlanguage.api.bukkit.BukkitStoredMessage;
import codes.laivy.mlanguage.api.bukkit.IBukkitItemTranslator;
import codes.laivy.mlanguage.api.bukkit.events.ItemTranslateEvent;
import codes.laivy.mlanguage.api.bukkit.provider.BukkitStoredMessageProvider;
import codes.laivy.mlanguage.api.bukkit.reflection.Version;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagByte;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagCompound;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.nbt.tags.NBTTagString;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.main.BukkitMultiplesLanguages;
import codes.laivy.mlanguage.utils.ComponentUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack.getNMSItemStack;
import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

/**
 * The default Bukkit item translator of the LvMultiplesLanguages
 */
public final class BukkitItemTranslator implements IBukkitItemTranslator {

    private final @NotNull BukkitMultiplesLanguages plugin;
    
    public BukkitItemTranslator(@NotNull BukkitMultiplesLanguages plugin) {
        this.plugin = plugin;
    }

    public @NotNull BukkitMultiplesLanguages getPlugin() {
        return plugin;
    }

    @Override
    public boolean isTranslatable(@NotNull ItemStack item) {
        codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack nmsItem = getNMSItemStack(item);
        if (nmsItem.getValue() != null && nmsItem.getTag() != null) {
            return nmsItem.getTag().contains("Translatable");
        }
        return false;
    }

    @Override
    public @NotNull ItemStack setTranslatable(@NotNull ItemStack item, @Nullable BukkitStoredMessage name, @Nullable BukkitStoredMessage lore) {
        codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack nmsItem = codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack.getNMSItemStack(item);
        NBTTagCompound compound = nmsItem.getTag();

        if (compound == null) {
            compound = (NBTTagCompound) getPlugin().getVersion().nbtTag(Version.NBTTag.COMPOUND);
        }

        compound.set("Translatable", new NBTTagByte((byte) 1));
        if (name != null) {
            compound.set("NameTranslation", new NBTTagString(serialize(name).toString()));
        }
        if (lore != null) {
            compound.set("LoreTranslation", new NBTTagString(serialize(lore).toString()));
        }

        nmsItem.setTag(compound);

        return nmsItem.getCraftItemStack().getItemStack();
    }

    @Override
    public void translateInventory(@NotNull Player player) {
        getPlugin().getVersion().translateInventory(player);
    }

    /**
     * Gets the SetSlot packet
     * @param item the item
     * @param player the player
     * @param window the windowId
     * @param slot the slot
     * @param state the stateId (since 1.17.1, leave it -1 before this version)
     * @return the SetSlot packet
     */
    public @NotNull PacketPlayOutSetSlot translate(@NotNull ItemStack item, @NotNull Player player, int window, int slot, int state) {
        if (isTranslatable(item)) {
            translate(item, player);
            return getPlugin().getVersion().createSetSlotPacket(window, slot, state, getNMSItemStack(item));
        }
        throw new IllegalArgumentException("This item isn't translatable!");
    }

    @Override
    public @Nullable BukkitStoredMessage getName(@NotNull ItemStack item) {
        if (isTranslatable(item)) {
            @NotNull NBTTagCompound tag = Objects.requireNonNull(getNMSItemStack(item).getTag());
            final BukkitStoredMessage name;

            if (tag.contains("NameTranslation")) {
                JsonObject loreObj = JsonParser.parseString(Objects.requireNonNull(new NBTTagString(tag.get("LoreTranslation").getValue()).getData())).getAsJsonObject();
                name = deserialize(loreObj);
            } else {
                name = null;
            }
            return name;
        }
        return null;
    }

    @Override
    public @Nullable BukkitStoredMessage getLore(@NotNull ItemStack item) {
        if (isTranslatable(item)) {
            @NotNull NBTTagCompound tag = Objects.requireNonNull(getNMSItemStack(item).getTag());
            final BukkitStoredMessage lore;

            if (tag.contains("LoreTranslation")) {
                JsonObject loreObj = JsonParser.parseString(Objects.requireNonNull(new NBTTagString(tag.get("LoreTranslation").getValue()).getData())).getAsJsonObject();
                lore = deserialize(loreObj);
            } else {
                lore = null;
            }
            return lore;
        }
        return null;
    }

    @Override
    public void translate(@NotNull ItemStack item, @NotNull Player player) {
        if (isTranslatable(item)) {
            @Nullable Locale locale = multiplesLanguagesBukkit().getApi().getLocale(player.getUniqueId());

            @Nullable BukkitStoredMessage name = getName(item);
            @Nullable BukkitStoredMessage lore = getLore(item);

            // Event calling
            ItemTranslateEvent event = new ItemTranslateEvent(!Bukkit.isPrimaryThread(), item, player, locale, name, lore);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            name = event.getName();
            lore = event.getLore();
            locale = event.getLocale();
            //

            if (name != null) {
                final Locale nameLocale = (locale != null ? locale : name.getStorage().getDefaultLocale());

                getPlugin().getVersion().setItemBukkitDisplayName(
                        item,
                        ComponentUtils.merge(name.getMessage().getText(nameLocale))
                );
            } else {
                getPlugin().getVersion().setItemBukkitDisplayName(item, null);
            }

            if (lore != null) {
                final Locale loreLocale = (locale != null ? locale : lore.getStorage().getDefaultLocale());

                getPlugin().getVersion().setItemBukkitLore(
                        item,
                        lore.getMessage().getText(loreLocale)
                );
            } else {
                getPlugin().getVersion().setItemBukkitLore(item, null);
            }
        } else {
            throw new IllegalArgumentException("This item isn't translatable!");
        }
    }

    private @NotNull JsonObject serialize(@NotNull BukkitStoredMessage stored) {
        JsonObject object = new JsonObject();
        JsonObject storageObj = new JsonObject();

        storageObj.addProperty("name", stored.getStorage().getName());
        storageObj.addProperty("plugin", stored.getStorage().getPluginProperty().getName());

        object.addProperty("id", stored.getMessage().getId());
        object.add("storage", storageObj);
        return object;
    }
    private @NotNull BukkitStoredMessage deserialize(@NotNull JsonObject message) {
        JsonObject storageObj = message.get("storage").getAsJsonObject();

        @NotNull String id = message.get("id").getAsString();
        @NotNull String storageName = storageObj.get("name").getAsString();
        @NotNull String storagePluginName = storageObj.get("plugin").getAsString();
        @Nullable Plugin storagePlugin = Bukkit.getPluginManager().getPlugin(storagePluginName);
        if (storagePlugin == null) {
            throw new NullPointerException("Couldn't find plugin '" + storagePluginName + "'");
        }

        @Nullable BukkitMessageStorage storage = multiplesLanguagesBukkit().getApi().getStorage(plugin, storageName);
        if (storage == null) {
            throw new NullPointerException("Couldn't find storage named '" + storageName + "' at plugin '" + storagePluginName + "'");
        }

        return new BukkitStoredMessageProvider(storage, storage.getMessage(id));
    }

    @Override
    public void reset(@NotNull ItemStack item) {
        final @Nullable BukkitStoredMessage name = getName(item);
        final @Nullable BukkitStoredMessage lore = getLore(item);

        if (name != null) {
            getPlugin().getVersion().setItemBukkitDisplayName(item, ComponentUtils.merge(name.getMessage().getText(name.getStorage().getDefaultLocale())));
        } if (lore != null) {
            getPlugin().getVersion().setItemBukkitLore(item, lore.getMessage().getText(lore.getStorage().getDefaultLocale()));
        }
    }
}
