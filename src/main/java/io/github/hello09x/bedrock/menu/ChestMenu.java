package io.github.hello09x.bedrock.menu;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ChestMenu {

    @Getter
    private final Inventory inventory;

    @Getter
    private final ChestMenuHolder holder;

    @NotNull
    public static Consumer<InventoryClickEvent> DEFAULT_ON_BACK = x -> {
        x.getWhoClicked().closeInventory();
    };


    ChestMenu(int size, @NotNull Component title, @Nullable Consumer<InventoryClickEvent> onBack) {
        var holder = new ChestMenuHolder(onBack != null ? onBack : DEFAULT_ON_BACK);
        var inventory = Bukkit.createInventory(holder, size, title);
        holder.setInventory(inventory);

        this.inventory = inventory;
        this.holder = holder;
    }

    public void setButton(int slot, @NotNull ItemStack item) {
        this.setButton(slot, item, (Consumer<InventoryClickEvent>) null);
    }

    public void setButton(int slot, @NotNull Material material, @NotNull Component displayName) {
        this.setButton(slot, material, displayName, (Consumer<InventoryClickEvent>) null);
    }

    public void setButton(
            int slot,
            @NotNull ItemStack item,
            @NotNull Runnable onClick
    ) {
        this.setButton(slot, item, event -> onClick.run());
    }

    public void setButton(int slot, @NotNull Material material, @NotNull Component displayName, @NotNull Runnable onClick) {
        this.setButton(slot, material, displayName, event -> onClick.run());
    }

    public void setButton(
            int slot,
            @NotNull ItemStack item,
            @Nullable Consumer<InventoryClickEvent> onClick
    ) {
        this.inventory.setItem(slot, item);
        if (onClick != null) {
            this.holder.addButton(slot, onClick);
        }
    }

    public void setButton(int slot, @NotNull Material material, @NotNull Component displayName, @Nullable Consumer<InventoryClickEvent> callback) {
        var item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(displayName);
        });
        this.setButton(slot, item, callback);
    }

    public void removeButton(int slot) {
        this.inventory.setItem(slot, null);
        this.holder.removeButton(slot);
    }

}
