package io.github.hello09x.bedrock.menu;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChestMenuBuilder {

    public final static Consumer<?> IGNORE = event -> {
    };

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> ignore() {
        return (Consumer<T>) IGNORE;
    }

    public final static Consumer<InventoryClickEvent> CLOSE = event -> {
        event.getWhoClicked().closeInventory();
    };

    @NotNull
    private Component title = Component.text("Menu");

    @NotNull
    private Consumer<InventoryClickEvent> onClickOutside = CLOSE;

    @NotNull
    private Consumer<InventoryClickEvent> onClickBottom = ignore();

    @NotNull
    private Consumer<InventoryCloseEvent> onClose = ignore();

    @NotNull
    private final Map<Integer, Pair<ItemStack, Consumer<InventoryClickEvent>>> buttons = new HashMap<>();

    private int size = 54;

    public @NotNull ChestMenuBuilder title(@NotNull Component title) {
        this.title = title;
        return this;
    }

    public @NotNull ChestMenuBuilder onClickOutside(@NotNull Consumer<InventoryClickEvent> onClickOutside) {
        this.onClickOutside = onClickOutside;
        return this;
    }

    public @NotNull ChestMenuBuilder onClickBottom(@NotNull Consumer<InventoryClickEvent> onClickBottom) {
        this.onClickBottom = onClickBottom;
        return this;
    }

    public @NotNull ChestMenuBuilder onClose(@NotNull Consumer<InventoryCloseEvent> onClose) {
        this.onClose = onClose;
        return this;
    }

    public @NotNull ChestMenuBuilder size(int size) {
        this.size = size;
        return this;
    }

    public @NotNull ChestMenuBuilder onClickButton(int slot, @NotNull ItemStack item, @NotNull Consumer<InventoryClickEvent> onClickButton) {
        this.buttons.put(slot, Pair.of(item, onClickButton));
        return this;
    }

    public @NotNull ChestMenuBuilder onClickButton(int slot, @NotNull Material type, @NotNull Component text, @NotNull Consumer<InventoryClickEvent> onClickButton) {
        var item = new ItemStack(type);
        item.editMeta(meta -> {
            meta.displayName(text);
        });
        return this.onClickButton(slot, item, onClickButton);
    }

    public @NotNull Inventory build() {
        var holder = new ChestMenuHolder(this.onClickBottom, this.onClickOutside, this.onClose);
        var inventory = Bukkit.createInventory(holder, this.size, this.title);
        for (var button : this.buttons.entrySet()) {
            var slot = button.getKey();
            holder.addButton(slot, button.getValue().value());
            inventory.setItem(slot, button.getValue().key());
        }
        holder.setInventory(inventory);
        return inventory;
    }

}
