package io.github.hello09x.bedrock.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.WeakHashMap;
import java.util.function.Consumer;

public class ChestMenuRegistry {

    private final WeakHashMap<Inventory, String> menus = new WeakHashMap<>();

    public ChestMenuRegistry(@NotNull Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), plugin);
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

    public @NotNull ChestMenu createMenu(int size, @NotNull Component title, @NotNull Consumer<InventoryClickEvent> onBack) {
        var menu = new ChestMenu(size, title, onBack);
        this.menus.put(menu.getInventory(), LegacyComponentSerializer.legacySection().serialize(title));
        return menu;
    }

    public static class InventoryClickListener implements Listener {

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
                // 点击下方的物品栏不处理
                return;
            }

            var slot = event.getSlot();
            if ((event.getClick() == ClickType.RIGHT && event.getCurrentItem() == null) || event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                // 右键空白地方返回
                holder.getOnback().accept(event);
                return;
            }
            var button = holder.getButton(slot);
            if (button == null) {
                return;
            }
            button.accept(event);
        }

        private @Nullable ChestMenuHolder getHolder(@NotNull Inventory inventory) {
            if (!(inventory.getHolder() instanceof ChestMenuHolder holder)) {
                return null;
            }
            return holder;
        }
    }
}
