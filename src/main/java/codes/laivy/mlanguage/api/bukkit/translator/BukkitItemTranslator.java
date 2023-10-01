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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
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
        } else {
            compound.set("NameTranslation", new NBTTagString("{}"));
        }


        if (lore != null) {
            compound.set("LoreTranslation", new NBTTagString(serialize(lore).toString()));
        } else {
            compound.set("LoreTranslation", new NBTTagString("{}"));
        }

        nmsItem.setTag(compound);

        ItemStack bukkitItem = nmsItem.getCraftItemStack().getItemStack();
        translate(bukkitItem, (Locale) null);

        return bukkitItem;
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
                String nameStr = new NBTTagString(tag.get("NameTranslation").getValue()).getData();
                if (nameStr == null) {
                    return null;
                }
                if (nameStr.equals("{}")) {
                    return null;
                }
                JsonObject loreObj = JsonParser.parseString(Objects.requireNonNull(nameStr)).getAsJsonObject();
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
                String loreStr = new NBTTagString(tag.get("LoreTranslation").getValue()).getData();
                if (loreStr == null) {
                    return null;
                }
                if (loreStr.equals("{}")) {
                    return null;
                }
                JsonObject loreObj = JsonParser.parseString(loreStr).getAsJsonObject();
                lore = deserialize(loreObj);
            } else {
                lore = null;
            }
            return lore;
        }
        return null;
    }

    public void translate(@NotNull ItemStack item, @Nullable Locale locale) {
        if (isTranslatable(item)) {
            @Nullable BukkitStoredMessage name = getName(item);
            @Nullable BukkitStoredMessage lore = getLore(item);

            if (name != null) {
                final Locale nameLocale = (locale != null ? locale : name.getStorage().getDefaultLocale());

                getPlugin().getVersion().setItemBukkitDisplayName(
                        item,
                        name.getMessage().getText(nameLocale, name.getReplacements().toArray(new Object[0]))
                );
            } else {
                getPlugin().getVersion().setItemBukkitDisplayName(item, null);
            }

            if (lore != null) {
                final Locale loreLocale = (locale != null ? locale : lore.getStorage().getDefaultLocale());

                getPlugin().getVersion().setItemBukkitLore(
                        item,
                        lore.getMessage().getArray(loreLocale, lore.getReplacements().toArray(new Object[0]))
                );
            } else {
                getPlugin().getVersion().setItemBukkitLore(item, null);
            }
        } else {
            throw new IllegalArgumentException("This item isn't translatable!");
        }
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
                        name.getMessage().getText(nameLocale, name.getReplacements().toArray(new Object[0]))
                );
            } else {
                getPlugin().getVersion().setItemBukkitDisplayName(item, null);
            }

            if (lore != null) {
                final Locale loreLocale = (locale != null ? locale : lore.getStorage().getDefaultLocale());

                getPlugin().getVersion().setItemBukkitLore(
                        item,
                        lore.getMessage().getArray(loreLocale, lore.getReplacements().toArray(new Object[0]))
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

        JsonArray replacements = new JsonArray();
        for (@NotNull Object replace : stored.getReplacements()) {
            replacements.add(multiplesLanguagesBukkit().getApi().getSerializer().serializeObject(replace));
        }

        JsonArray prefixes = new JsonArray();
        for (@NotNull Object prefix : stored.getPrefixes()) {
            prefixes.add(multiplesLanguagesBukkit().getApi().getSerializer().serializeObject(prefix));
        }

        JsonArray suffixes = new JsonArray();
        for (@NotNull Object suffix : stored.getSuffixes()) {
            suffixes.add(multiplesLanguagesBukkit().getApi().getSerializer().serializeObject(suffix));
        }

        object.addProperty("id", stored.getMessage().getId());

        if (!stored.getReplacements().isEmpty()) {
            object.add("replacements", replacements);
        }
        if (!stored.getPrefixes().isEmpty()) {
            object.add("prefixes", prefixes);
        }
        if (!stored.getSuffixes().isEmpty()) {
            object.add("suffixes", suffixes);
        }

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

        @Nullable BukkitMessageStorage storage = multiplesLanguagesBukkit().getApi().getStorage(storagePlugin, storageName);
        if (storage == null) {
            throw new NullPointerException("Couldn't find storage named '" + storageName + "' at plugin '" + storagePluginName + "'");
        }

        List<Object> replacements = new LinkedList<>();
        List<Object> prefixes = new LinkedList<>();
        List<Object> suffixes = new LinkedList<>();

        if (message.has("replacements")) {
            for (JsonElement element : message.getAsJsonArray("replacements")) {
                replacements.add(multiplesLanguagesBukkit().getApi().getSerializer().deserializeObject(element));
            }
        }
        if (message.has("prefixes")) {
            for (JsonElement element : message.getAsJsonArray("prefixes")) {
                prefixes.add(multiplesLanguagesBukkit().getApi().getSerializer().deserializeObject(element));
            }
        }
        if (message.has("suffixes")) {
            for (JsonElement element : message.getAsJsonArray("suffixes")) {
                suffixes.add(multiplesLanguagesBukkit().getApi().getSerializer().deserializeObject(element));
            }
        }

        BukkitStoredMessageProvider stored = new BukkitStoredMessageProvider(storage, storage.getMessage(id));

        stored.getReplacements().addAll(replacements);
        stored.getPrefixes().addAll(prefixes);
        stored.getSuffixes().addAll(suffixes);

        return stored;
    }

    @Override
    public void reset(@NotNull ItemStack item) {
        final @Nullable BukkitStoredMessage name = getName(item);
        final @Nullable BukkitStoredMessage lore = getLore(item);

        if (name != null) {
            getPlugin().getVersion().setItemBukkitDisplayName(item, name.getMessage().getText(name.getStorage().getDefaultLocale(), name.getReplacements().toArray(new Object[0])));
        } if (lore != null) {
            getPlugin().getVersion().setItemBukkitLore(item, lore.getMessage().getArray(lore.getStorage().getDefaultLocale(), lore.getReplacements().toArray(new Object[0])));
        }
    }
}
