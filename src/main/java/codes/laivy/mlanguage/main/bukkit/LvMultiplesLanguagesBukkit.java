package codes.laivy.mlanguage.main.bukkit;

import codes.laivy.data.DataAPI;
import codes.laivy.data.sql.SQLReceptor;
import codes.laivy.data.sql.SQLTable;
import codes.laivy.mlanguage.api.MenusAPI;
import codes.laivy.mlanguage.config.JsonConfigUtils;
import codes.laivy.mlanguage.lang.Language;
import codes.laivy.mlanguage.lang.LanguagePack;
import codes.laivy.mlanguage.lang.Locale;
import codes.laivy.mlanguage.main.bukkit.utils.GuiUtils;
import codes.laivy.mlanguage.main.bukkit.utils.PagedInventory;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static codes.laivy.mlanguage.LvMultiplesLanguages.getApi;
import static codes.laivy.mlanguage.LvMultiplesLanguages.setup;

public class LvMultiplesLanguagesBukkit extends JavaPlugin implements Listener {

    // ---/- Short the API use -/--- //
    public static @NotNull String getMessage(@NotNull Plugin plugin, @NotNull String key, @NotNull OfflinePlayer player) {
        return getApi().getLocaleAPI().getMessage(plugin.getName(), key, getApi().getLocaleAPI().getLocale(player.getUniqueId()));
    }
    public static @NotNull String getMessage(@NotNull Plugin plugin, @NotNull String key) {
        return getApi().getLocaleAPI().getMessage(plugin.getName(), key);
    }
    public static @NotNull String getMessage(@NotNull Plugin plugin, @NotNull String key, @NotNull Locale locale) {
        return getApi().getLocaleAPI().getMessage(plugin.getName(), key, locale);
    }
    public static @NotNull Locale getLocale(@NotNull OfflinePlayer player) {
        Locale locale = getApi().getLocaleAPI().getLocale(player.getUniqueId());
        if (locale == null) locale = Language.DEFAULT_LANGUAGE_CODE;
        return locale;
    }
    // ---/- Short the API use -/--- //

    public static @NotNull LvMultiplesLanguagesBukkit plugin() {
        return getPlugin(LvMultiplesLanguagesBukkit.class);
    }

    private final @NotNull JsonObject configuration;
    private final @NotNull SQLTable table;

    public LvMultiplesLanguagesBukkit() {
        //noinspection ResultOfMethodCallIgnored
        getDataFolder().mkdirs();
        configuration = JsonConfigUtils.safeGet("/config.json", getDataFolder().toPath()).getAsJsonObject();
        table = setup(configuration);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Language.createByResourceStream(this.getName(), getResource("languages/messages.json"));
        Language.createByResourceStream(this.getName(), getResource("languages/menu_messages.json"));

        getCommand("language").setExecutor(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            new SQLReceptor(getTable(), player.getName(), player.getUniqueId().toString()) {{
                load();
            }};
        }

        getApi().setMenusAPI(new MenusAPI() {
            @Override
            public @NotNull Object getLanguageInventory(@NotNull UUID playerUuid) {
                Player player = Objects.requireNonNull(Bukkit.getPlayer(playerUuid));
                Locale pLocale = getLocale(player);

                Inventory inventory = Bukkit.createInventory(null, 18, getApi().getLocaleAPI().getMessage(plugin().getName(), "menu_title", pLocale));

                ItemStack blackGlass = GuiUtils.createItem(GuiUtils.BLACK_STAINED_GLASS_PANE, "§c---/-/---");
                inventory.setItem(10, blackGlass);
                inventory.setItem(11, blackGlass);
                inventory.setItem(15, blackGlass);
                inventory.setItem(16, blackGlass);

                inventory.setItem(13, GuiUtils.createItem(Material.PAPER, getApi().getLocaleAPI().getMessage(plugin().getName(), "paper_name", pLocale), GuiUtils.getLoreFormattedMessage(getMessage(plugin(), "paper_lore", pLocale), 35)));

                // Items
                Map<Locale, Integer> messages = new TreeMap<>();
                for (Locale locale : Locale.values()) {
                    messages.put(locale, 0);
                    for (@NotNull Set<@NotNull Language> set : Language.LANGUAGES.get(locale).values()) {
                        for (Language language : set) {
                            for (LanguagePack pack : language.getPacks()) {
                                if (pack.getLocales().contains(locale)) {
                                    messages.put(locale, messages.get(locale) + pack.getMessages().size());
                                }
                            }
                        }
                    }
                    if (messages.get(locale) == 0) {
                        messages.remove(locale);
                    }
                }

                List<ItemStack> items = new LinkedList<>();
                for (Locale locale : messages.keySet()) {
                    if (messages.get(locale) == 0) {
                        continue;
                    }

                    Set<String> pluginsList = new LinkedHashSet<>();
                    for (Language language : getApi().getLocaleAPI().getLanguages()) {
                        if (language.getLocales().contains(locale)) {
                            pluginsList.add(language.getPlugin());
                        }
                    }
                    StringBuilder plugins = new StringBuilder();
                    for (String plugin : pluginsList) {
                        plugins.append("\n§8 - §f").append(plugin);
                    }

                    items.add(GuiUtils.createItem(Material.BOOK, (Language.DEFAULT_LANGUAGE_CODE.equals(locale) ? getApi().getLocaleAPI().getMessage(plugin().getName(), "default_badge", pLocale) + " " : "") + "§e" + locale.getName(),
                            getApi().getLocaleAPI().getMessage(plugin().getName(), "translations", pLocale) + messages.get(locale) + "\n" +
                                    getApi().getLocaleAPI().getMessage(plugin().getName(), "plugins_using", pLocale) + plugins + "\n" +
                                    "" + "\n" +
                                    (
                                            pLocale == locale ?
                                                    getApi().getLocaleAPI().getMessage(plugin().getName(), "current_language", pLocale) :
                                                    getApi().getLocaleAPI().getMessage(plugin().getName(), "select_language", pLocale)
                                    )
                    ));
                }
                //

                PagedInventory paged = new PagedInventory(inventory, items, 9, 17);
                paged.setBounds(9);
                paged.setClickAction(new PagedInventory.PagedInventoryClickAction() {
                    @Override
                    public void click(Player player, int clickedSlot, int page) {
                        Locale locale = new LinkedList<>(messages.keySet()).get(clickedSlot);

                        if (getApi().getLocaleAPI().getLocale(player.getUniqueId()) != locale) {
                            getApi().getLocaleAPI().setLocale(player.getUniqueId(), locale);
                            player.openInventory((Inventory) getLanguageInventory(playerUuid));
                            player.sendMessage(getApi().getLocaleAPI().getMessage(plugin().getName(), "language_changed", getApi().getLocaleAPI().getLocale(player.getUniqueId())).replace("%language%", locale.getName()));
                        }
                    }
                });

                return inventory;
            }
        });
    }

    @Override
    public void onDisable() {
        for (Language language : getApi().getLocaleAPI().getLanguages()) {
            language.saveFile();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            Objects.requireNonNull(DataAPI.getSQLReceptor(getTable(), player.getUniqueId().toString())).unload(true);
        }
    }

    public @NotNull JsonObject getConfiguration() {
        return configuration;
    }

    public @NotNull SQLTable getTable() {
        return table;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("language")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.openInventory((Inventory) getApi().getMenusAPI().getLanguageInventory(player.getUniqueId()));
            } else {
                sender.sendMessage(getApi().getLocaleAPI().getMessage(plugin().getName(), "only_players"));
            }
        }
        return true;
    }

    @EventHandler
    private void join(@NotNull PlayerJoinEvent e) {
        if (DataAPI.getSQLReceptor(getTable(), e.getPlayer().getUniqueId().toString()) == null) {
            new SQLReceptor(getTable(), e.getPlayer().getName(), e.getPlayer().getUniqueId().toString()) {{
                load();
            }};
        }

        if (getApi().getLocaleAPI().getLocale(e.getPlayer().getUniqueId()) == null) {
            e.getPlayer().sendMessage(getMessage(this, "change_language", e.getPlayer()));
        }
    }
    @EventHandler
    private void quit(@NotNull PlayerQuitEvent e) {
        Objects.requireNonNull(DataAPI.getSQLReceptor(getTable(), e.getPlayer().getUniqueId().toString())).unload(true);
    }

}
