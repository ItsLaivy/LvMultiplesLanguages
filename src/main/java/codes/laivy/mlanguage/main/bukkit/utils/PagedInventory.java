package codes.laivy.mlanguage.main.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static codes.laivy.mlanguage.main.bukkit.LvMultiplesLanguagesBukkit.plugin;

public class PagedInventory {

    public interface PagedInventoryClickAction {
        default void click(InventoryClickEvent e, Player player) {
        }
        default void click(Player player, int clickedSlot, int page) {
        }
    }

    public static final PagedInventoryClickAction DEFAULT_PAGED_CLICK_ACTION = new PagedInventoryClickAction() {
        @Override
        public void click(InventoryClickEvent e, Player player) {
        }
        @Override
        public void click(Player player, int clickedSlot, int page) {
        }
    };

    public static int getClickSlot(ItemStack pageItem, int clickedSlot, int boundMax) {
        return getClickSlot(GuiUtils.getPageOfItem(pageItem), clickedSlot, boundMax);
    }
    public static int getClickSlot(int page, int clickedSlot, int boundMax) {
        return (page * boundMax) + clickedSlot;
    }

    public static int getBoundMaxFrom(PagedInventory pagedInventory) {
        int top = 0;
        for (Integer b : pagedInventory.bounds) {
            if (b > top) {
                top = b;
            }
        }
        return top + 1;
    }

    private static final Map<Inventory, PagedInventory> PAGED_INVENTORIES = new HashMap<>();

    @SuppressWarnings("unused")
    private static final class Events implements Listener {
        @EventHandler(ignoreCancelled = true)
        private void clickEvent(InventoryClickEvent e) {
            if (PAGED_INVENTORIES.containsKey(e.getView().getTopInventory())) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();

                PagedInventory i = PAGED_INVENTORIES.get(e.getView().getTopInventory());
                int page = i.page;

                if (e.getSlot() == i.getPreviousSlot()) {
                    if (page > 0) {
                        i.setPage(page - 1);
                    } else {
                        e.getWhoClicked().sendMessage("§cThis inventory doesn't have a previous page");
                    }
                } else if (e.getSlot() == i.getNextSlot()) {
                    if ((i.page + 1) * i.bounds.size() < i.list.size()) {
                        i.setPage(page + 1);
                    } else {
                        e.getWhoClicked().sendMessage("§cThis inventory doesn't have a next page");
                    }
                } else if (e.getCurrentItem() != null && i.getClickAction() != null && i.bounds.contains(e.getSlot())) {
                    i.getClickAction().click((Player) e.getWhoClicked(), getClickSlot(page, e.getSlot(), getBoundMaxFrom(i)), page);
                }

                i.getClickAction().click(e, player);
            }
        }
    }

    static {
        Bukkit.getPluginManager().registerEvents(new Events(), plugin());

        Bukkit.getScheduler().runTaskTimer(plugin(), () -> {
            for (Map.Entry<Inventory, PagedInventory> m : new HashSet<>(PAGED_INVENTORIES.entrySet())) {
                if (m.getKey().getViewers().size() == 0) {
                    m.getValue().remove();
                }
            }
        }, 100, 100);
    }

    private PagedInventoryClickAction clickAction;

    private final Inventory inventory;
    private final List<ItemStack> list;
    private int page;

    private final List<Integer> bounds = new ArrayList<>();

    private final int previousSlot;
    private final int nextSlot;

    private Integer blankListItemSlot;
    private ItemStack blankListItem;

    public PagedInventory(Inventory inventory, List<ItemStack> list, int previous, int next) {
        this(inventory, list, previous, next, null);
    }
    public PagedInventory(Inventory inventory, List<ItemStack> list, int previous, int next, Integer blankListItemSlot) {
        this.blankListItemSlot = blankListItemSlot;
        this.blankListItem = GuiUtils.createItem(GuiUtils.COBWEB, "§cBlank Page", "§7There is nothing here to explore :(");

        this.inventory = inventory;
        this.list = list;

        this.previousSlot = previous;
        this.nextSlot = next;

        this.clickAction = DEFAULT_PAGED_CLICK_ACTION;

        PAGED_INVENTORIES.put(inventory, this);
    }

    public PagedInventoryClickAction getClickAction() {
        return clickAction;
    }

    public void setClickAction(PagedInventoryClickAction clickAction) {
        if (clickAction == null) clickAction = DEFAULT_PAGED_CLICK_ACTION;
        this.clickAction = clickAction;
    }

    public void setBounds(int zeroTo) {
        for (int row = 0; row < zeroTo; row++) {
            bounds.add(row);
        }

        setPage(0);
    }
    public void setBounds(int... onlyAt) {
        for (Integer n : onlyAt) {
            bounds.add(n);
        }

        setPage(0);
    }

    public PagedInventory setPage(int newPage) {
        if (bounds.isEmpty()) {
            throw new NullPointerException("You need to set the bounds!");
        }

        this.page = newPage;

        int row = 0;
        for (Integer slot : bounds) {
            this.inventory.setItem(slot, new ItemStack(Material.AIR));

            int index = row + (page * bounds.size());
            if (list.size() > index) {
                this.inventory.setItem(slot, list.get(index));
            }

            row++;
        }

        inventory.setItem(nextSlot, GuiUtils.getNextPageItem(page));
        inventory.setItem(previousSlot, GuiUtils.getPreviousPageItem(page));

        if (blankListItem != null && blankListItemSlot != null) {
            if (list.size() == 0) {
                inventory.setItem(blankListItemSlot, blankListItem);
            }
        }

        return this;
    }

    public void openFor(Player... players) {
        for (Player player : players) {
            player.openInventory(inventory);
        }
    }

    public List<Integer> getBounds() {
        return bounds;
    }

    public int getPreviousSlot() {
        return previousSlot;
    }

    public int getNextSlot() {
        return nextSlot;
    }

    public void remove() {
        PAGED_INVENTORIES.remove(inventory);
    }

}