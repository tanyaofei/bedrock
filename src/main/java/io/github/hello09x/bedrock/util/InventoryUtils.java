package io.github.hello09x.bedrock.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
            for (var unfit : result.values()) {
                player.getWorld().dropItem(player.getLocation(), unfit);
            }
        }
    }

    /**
     * 替换仓库
     *
     * @param replaceTo    要被替换的仓库
     * @param replacements 替换的物品
     * @param clear        是否清理仓库原物品
     */
    public static void replace(
            @NotNull Inventory replaceTo,
            @NotNull Map<Integer, ItemStack> replacements,
            boolean clear
    ) {
        for (int i = replaceTo.getSize() - 1; i >= 0; i--) {
            var replacement = replacements.get(i);
            if (replacement != null) {
                replaceTo.setItem(i, replacement);
            } else if (clear) {
                replaceTo.setItem(i, null);
            }
        }
    }

    /**
     * 将仓库转为 {@link Map}, 如果物品为 {@code null} 则不会出现在返回值里
     *
     * @param inventory 仓库
     * @return Java {@link Map}
     */
    public static @NotNull Map<Integer, ItemStack> toMap(@NotNull Inventory inventory) {
        var items = new HashMap<Integer, ItemStack>(inventory.getSize(), 1.0F);
        var itr = inventory.iterator();
        while (itr.hasNext()) {
            var i = itr.nextIndex();
            var item = itr.next();
            if (item == null) {
                continue;
            }
            items.put(i, item);
        }
        return items;
    }

}
