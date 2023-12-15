package io.github.hello09x.bedrock.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CommandItemRegistry {

    private final NamespacedKey key;

    public CommandItemRegistry(@NotNull Plugin plugin) {
        this.key = new NamespacedKey(plugin, "bedrock.command");
    }

    public boolean isCommandItem(@Nullable ItemStack item) {
        return item != null && Optional.ofNullable(item.getItemMeta())
                .map(meta -> meta.getPersistentDataContainer().get(key, PersistentDataType.STRING))
                .isPresent();
    }

    public void perform(@NotNull Player player, @NotNull ItemStack commandItem) {
        var commands = Optional
                .ofNullable(commandItem.getItemMeta())
                .map(meta -> meta.getPersistentDataContainer().get(key, PersistentDataType.STRING))
                .orElse(null);

        if (commands == null) {
            return;
        }

        for (var command : commands.split("; ")) {
            if (command.isBlank()) {
                return;
            }
            player.performCommand(command);
        }
    }

    public @NotNull ItemStack create(
            @NotNull Material material,
            @NotNull Component name,
            String @NotNull ... commands
    ) {
        var item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(name);
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.join("; ", commands));
        });
        return item;
    }

}
