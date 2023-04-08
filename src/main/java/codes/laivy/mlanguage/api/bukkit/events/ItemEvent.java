package codes.laivy.mlanguage.api.bukkit.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemEvent extends Event {

    private static final @NotNull HandlerList handlers = new HandlerList();

    private final @NotNull ItemStack item;

    public ItemEvent(boolean async, @NotNull ItemStack item) {
        super(async);
        this.item = item;
    }

    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
