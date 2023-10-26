package io.github.hello09x.bedrock.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryUtils {

    /**
     * 移除物品
     *
     * @param inventory 玩家背包
     * @param item      物品
     * @param max       最大移除数
     * @return 成功移除数
     */
    public static int removeItem(@NotNull Inventory inventory, @NotNull ItemStack item, int max) {
        var removed = 0;

        for (var each : inventory) {
            if (removed == max) {
                return removed;
            }
            if (each == null) {
                continue;
            }
            if (!each.asOne().equals(item)) {
                continue;
            }

            var delta = Math.min(each.getAmount(), max - removed);
            each.subtract(delta);
            removed += delta;
        }
        return removed;
    }

    public static void addItem(@NotNull Player player, @NotNull ItemStack item) {
        var result = player.getInventory().addItem(item);
        if (!result.isEmpty()) {
            for(var unfit: result.values()) {
                player.getWorld().dropItem(player.getLocation(), unfit);
            }
        }
    }


}
