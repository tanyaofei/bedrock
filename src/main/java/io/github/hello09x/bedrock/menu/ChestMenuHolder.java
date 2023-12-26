package io.github.hello09x.bedrock.menu;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChestMenuHolder implements InventoryHolder {

    @NotNull
    private final Map<Integer, Consumer<InventoryClickEvent>> buttons = new HashMap<>();

    @Getter
    @NotNull
    private final Consumer<InventoryClickEvent> onClickOutside;

    @Getter
    @NotNull
    private final Consumer<InventoryClickEvent> onClickBottom;

    @Getter
    @NotNull
    private final Consumer<InventoryCloseEvent> onClose;

    private Inventory inventory;

    public ChestMenuHolder(
            @NotNull Consumer<InventoryClickEvent> onClickBottom,
            @NotNull Consumer<InventoryClickEvent> onClickOutside,
            @NotNull Consumer<InventoryCloseEvent> onClose
    ) {
        this.onClickBottom = onClickBottom;
        this.onClickOutside = onClickOutside;
        this.onClose = onClose;
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
