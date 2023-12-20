package io.github.hello09x.bedrock.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ChestMenuRegistry {

    private final Plugin plugin;

    public ChestMenuRegistry(@NotNull Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new InventoryClickListener(), plugin);
    }

    public @NotNull Inventory createMenu(int size, @NotNull Component title) {
        var holder = new ChestMenuHolder(this.plugin);
        var menu = Bukkit.createInventory(holder, size, title);
        holder.setInventory(menu);
        return menu;
    }

    public void setButton(
            @NotNull Inventory menu,
            int slot,
            @NotNull ItemStack item,
            @Nullable Consumer<InventoryClickEvent> callback
    ) {
        var holder = this.getHolder(menu);
        if (holder == null) {
            throw new IllegalArgumentException("inventory must be a Menu Inventory");
        }

        menu.setItem(slot, item);
        if (callback != null) {
            holder.addButton(slot, callback);
        }
    }

    public @NotNull ItemStack setButton(
            @NotNull Inventory menu,
            int slot,
            @NotNull Material type,
            @NotNull Component displayName,
            @Nullable Consumer<InventoryClickEvent> callback
    ) {
        var item = new ItemStack(type);
        item.editMeta(meta -> meta.displayName(displayName));
        this.setButton(menu, slot, item, callback);
        return item;
    }

    public boolean isMenu(@NotNull Inventory inventory) {
        return inventory.getHolder() instanceof ChestMenuHolder holder && holder.getPlugin().equals(this.plugin);
    }

    public @Nullable ChestMenuHolder getHolder(@NotNull Inventory inventory) {
        if (!(inventory.getHolder() instanceof ChestMenuHolder holder) || holder.getPlugin() != this.plugin) {
            return null;
        }
        return holder;
    }

    public class InventoryClickListener implements Listener {

        @EventHandler
        private void onClick(@NotNull InventoryClickEvent event) {
            var top = event.getView().getTopInventory();
            var holder = getHolder(top);
            if (holder == null) {
                return;
            }

            event.setCancelled(true);

            if (event.getClickedInventory() != top) {
                return;
            }

            var slot = event.getSlot();
            var button = holder.getButton(slot);
            if (button == null) {
                return;
            }

            button.accept(event);
        }

    }

}
