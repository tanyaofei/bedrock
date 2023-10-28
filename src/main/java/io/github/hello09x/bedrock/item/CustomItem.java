package io.github.hello09x.bedrock.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CustomItem {

    /**
     * 返回物品
     *
     * @return 物品
     */
    @NotNull ItemStack item();

    /**
     * 返回物品 key
     *
     * @return key
     */
    @NotNull String key();


}
