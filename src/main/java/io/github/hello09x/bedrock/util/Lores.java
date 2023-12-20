package io.github.hello09x.bedrock.util;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Lores {

    public static void append(@NotNull ItemStack item, @NotNull List<? extends Component> append) {
        item.editMeta(meta -> {
            var lore = new ArrayList<>(Optional.ofNullable(meta.lore()).orElse(Collections.emptyList()));
            lore.addAll(append);
            meta.lore(lore);
        });

    }


}
