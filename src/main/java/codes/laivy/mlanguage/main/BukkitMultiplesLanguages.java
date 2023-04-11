package codes.laivy.mlanguage.main;

import codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.IBukkitMultiplesLanguagesAPI;
import codes.laivy.mlanguage.api.bukkit.natives.InjectionManager;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.utils.ComponentUtils;
import codes.laivy.mlanguage.utils.Platform;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BukkitMultiplesLanguages extends JavaPlugin implements Platform<ItemStack, Plugin, Player, BaseComponent>, Listener {

    public static @NotNull BukkitMultiplesLanguages multiplesLanguagesBukkit() {
        return JavaPlugin.getPlugin(BukkitMultiplesLanguages.class);
    }

    private @NotNull IBukkitMultiplesLanguagesAPI api;
    private boolean serverLoaded = false;

    public BukkitMultiplesLanguages() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();

        BukkitItemTranslator translator = new BukkitItemTranslator();
        this.api = new BukkitMultiplesLanguagesAPI(this, translator, new InjectionManager(translator));
    }

    @Override
    public @NotNull IBukkitMultiplesLanguagesAPI getApi() {
        return api;
    }

    public void setApi(@NotNull IBukkitMultiplesLanguagesAPI api) {
        if (getApi().isLoaded()) getApi().unload();
        this.api = api;
        if (isServerLoaded()) api.load();
    }

    // TODO: 08/04/2023 Debug only
//    @EventHandler
//    private void chat(@NotNull AsyncPlayerChatEvent e) {
//        Bukkit.getScheduler().runTask(this, () -> {
//            try {
//                int slot = Integer.parseInt(e.getMessage());
//                EntityPlayer.getEntityPlayer(e.getPlayer()).getConnection().sendPacket(getDefApi().getVersion().createSetSlotPacket(0, slot, -1, codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack.getNMSItemStack(new ItemStack(Material.DIAMOND))));
//            } catch (NumberFormatException ignore) {
//                if (e.getMessage().equals("a")) {
//                    NBTTagCompound compound = codes.laivy.mlanguage.api.bukkit.reflection.classes.item.ItemStack.getNMSItemStack(e.getPlayer().getItemInHand()).getTag();
//                    Bukkit.broadcastMessage(compound.getValue().toString());
//                } else if (e.getMessage().equals("get")) {
//                    BukkitMessageStorage storage = (BukkitMessageStorage) Objects.requireNonNull(getApi().getStorage(this, "Nome teste"));
//
//                    if (ReflectionUtils.isCompatible(V1_9_R1.class)) {
//                        e.getPlayer().getInventory().setItem(45, new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.DIAMOND_PICKAXE), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
//                    }
//                    Set<ItemStack> items = new LinkedHashSet<>();
//                    for (org.bukkit.inventory.ItemStack ignore2 : e.getPlayer().getInventory()) {
//                        int random = new Random().nextInt(5);
//                        Material material;
//                        if (random == 0) {
//                            material = Material.DIAMOND;
//                        } else if (random == 1) {
//                            material = Material.REDSTONE;
//                        } else if (random == 2) {
//                            material = Material.COAL;
//                        } else if (random == 3) {
//                            material = Material.GOLD_INGOT;
//                        } else {
//                            material = Material.IRON_INGOT;
//                        }
//
//                        items.add(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(material, new Random().nextInt(64) + 1), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
//                    }
//                    e.getPlayer().getInventory().setContents(items.toArray(new org.bukkit.inventory.ItemStack[0]));
//
//                    e.getPlayer().getInventory().setHelmet(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
//                    e.getPlayer().getInventory().setChestplate(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_CHESTPLATE), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
//                    e.getPlayer().getInventory().setLeggings(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
//                    e.getPlayer().getInventory().setBoots(new TranslatableBukkitItem(new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS), new BukkitMessage(storage, "Teste1"), new BukkitMessage(storage, "Teste2")).getItem());
//                }
//            }
//        });
//    }

    @Override
    public void log(@NotNull BaseComponent component) {
        getServer().getConsoleSender().sendMessage("§8[§6" + getDescription().getName() + "§8]§7" + " " + ComponentUtils.getText(component));
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // TODO: 07/04/2023 Improve this, reload not supported!
        // On server loads, load the API too
        Bukkit.getScheduler().runTaskLater(this, () -> {
            serverLoaded = true;
            getApi().load();
        }, 1);
    }

    @Override
    public void onDisable() {
        getApi().unload();
    }

    private boolean isServerLoaded() {
        return serverLoaded;
    }

    @Override
    @Contract(pure = true)
    public final @NotNull Type getType() {
        return Type.BUKKIT;
    }
}
