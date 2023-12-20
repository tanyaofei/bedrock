package io.github.hello09x.bedrock.menu;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChestMenuHolder implements InventoryHolder {

    @Getter
    @NotNull
    private final Plugin plugin;

    private Inventory inventory;

    private final Map<Integer, Consumer<InventoryClickEvent>> buttons = new HashMap<>();

    public ChestMenuHolder(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(@NotNull Inventory inventory) {
        if (this.inventory != null) {
            throw new IllegalStateException("inventory can be set only once");
        }
        this.inventory = inventory;
    }

    public void addButton(@NotNull Integer slot, Consumer<InventoryClickEvent> callback) {
        this.buttons.put(slot, callback);
    }

    public @Nullable Consumer<InventoryClickEvent> getButton(@NotNull Integer slot) {
        return this.buttons.get(slot);
    }

}
