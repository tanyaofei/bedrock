package io.github.hello09x.bedrock.menu;

import io.github.hello09x.bedrock.util.Folia;
import io.github.hello09x.bedrock.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.WeakHashMap;

public class ChestMenuRegistry {

    private final WeakHashMap<Inventory, String> menus = new WeakHashMap<>();

    public ChestMenuRegistry(@NotNull Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new ChestMenuListener(), plugin);
        if (!Folia.isFolia()) {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                boolean showTips = (Bukkit.getServer().getCurrentTick() & 1) == 1;
                for (var entry : menus.entrySet()) {
                    var inv = entry.getKey();
                    for (var viewer : inv.getViewers()) {
                        var view = viewer.getOpenInventory();
                        if (view.getTopInventory() != inv) {
                            return;
                        }

                        if (showTips) {
                            view.setTitle("点击空白位置返回上一页");
                        } else {
                            view.setTitle(entry.getValue());
                        }
                    }
                }
            }, 0, 41);
        }
    }

    public @NotNull ChestMenuBuilder builder() {
        return new ChestMenuBuilder();
    }

    public static class ChestMenuListener implements Listener {

        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        public void onClick(@NotNull InventoryClickEvent event) {
            var top = event.getView().getTopInventory();
            var holder = this.getHolder(top);
            if (holder == null) {
                return;
            }

            event.setCancelled(true);

            var clicked = event.getClickedInventory();
            if (clicked != null && clicked != top) {
                holder.getOnClickBottom().accept(event);
                return;
            }

            var slot = event.getSlot();
            if ((event.getClick() == ClickType.RIGHT && event.getCurrentItem() == null) || event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                // 右键空白地方返回
                holder.getOnClickOutside().accept(event);
                return;
            }

            var button = holder.getButton(slot);
            if (button == null) {
                return;
            }

            button.accept(event);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
        public void onClose(@NotNull InventoryCloseEvent event) {
            var top = event.getView().getTopInventory();
            var holder = this.getHolder(top);
            if (holder == null) {
                return;
            }

            holder.getOnClose().accept(event);
        }

        private @Nullable ChestMenuHolder getHolder(@NotNull Inventory inventory) {
            if (!(inventory.getHolder() instanceof ChestMenuHolder holder)) {
                return null;
            }
            return holder;
        }
    }

}
