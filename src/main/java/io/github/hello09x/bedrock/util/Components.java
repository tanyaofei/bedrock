package io.github.hello09x.bedrock.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public interface Components {

    public static @NotNull Component join(
            @NotNull Iterable<? extends Component> components,
            @NotNull Component sep
    ) {
        return Component.join(JoinConfiguration.separator(sep), components);
    }

    public static @NotNull Component join(
            @NotNull Iterable<? extends Component> components,
            @NotNull Component sep,
            @Nullable Function<Integer, Component> prefixSupplier
    ) {
        var itr = components.iterator();
        var builder = Component.empty().toBuilder();

        int i = 0;
        while (itr.hasNext()) {
            builder.append(prefixSupplier == null ? itr.next() : textOfChildren(prefixSupplier.apply(i), itr.next()));
            if (itr.hasNext()) {
                builder.append(sep);
            }
            i++;
        }
        return builder.asComponent();
    }

    public static @NotNull TextComponent banner(@NotNull String title) {
        return textOfChildren(text("[ ", YELLOW), text(title, LIGHT_PURPLE), text(" ]", YELLOW));
    }

    public static @NotNull Component noItalic(@NotNull String raw) {
        return text(raw).decoration(ITALIC, false);
    }

    public static @NotNull Component noItalic(@NotNull String raw, @NotNull TextColor color) {
        return text(raw, color).decoration(ITALIC, false);
    }

}
