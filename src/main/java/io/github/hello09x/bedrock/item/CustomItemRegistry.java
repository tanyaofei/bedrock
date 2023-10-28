package io.github.hello09x.bedrock.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CustomItemRegistry {

    @NotNull
    private final NamespacedKey TYPE_KEY;

    public CustomItemRegistry(@NotNull Plugin plugin) {
        this.TYPE_KEY = new NamespacedKey(plugin, "item-type");
    }

    public void register(@NotNull CustomItem item) {
        item.item().editMeta(meta -> {
            meta.getPersistentDataContainer().set(TYPE_KEY, PersistentDataType.STRING, item.key());
        });
    }

    public boolean isSimilar(@NotNull ItemStack item, @NotNull CustomItem type) {
        return Optional.ofNullable(item.getItemMeta())
                .map(meta -> meta.getPersistentDataContainer().get(TYPE_KEY, PersistentDataType.STRING))
                .filter(t -> t.equals(type.key()))
                .isPresent();
    }

}
