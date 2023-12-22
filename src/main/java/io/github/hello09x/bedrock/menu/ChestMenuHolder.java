package io.github.hello09x.bedrock.menu;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChestMenuHolder implements InventoryHolder {

    private Inventory inventory;

    @NotNull
    private final Map<Integer, Consumer<InventoryClickEvent>> buttons = new HashMap<>();

    @Getter
    @NotNull
    private final Consumer<InventoryClickEvent> onback;

    public ChestMenuHolder(@NotNull Consumer<InventoryClickEvent> onBack) {
        this.onback = onBack;
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

    public void addButton(int slot, Consumer<InventoryClickEvent> callback) {
        this.buttons.put(slot, callback);
    }

    public void removeButton(int slot) {
        this.buttons.remove(slot);
    }

    public @Nullable Consumer<InventoryClickEvent> getButton(@NotNull Integer slot) {
        return this.buttons.get(slot);
    }

}
