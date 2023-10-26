package io.github.hello09x.bedrock.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class ComponentUtils {

    public static Component join(@NotNull Iterable<? extends Component> components, @NotNull Component sep) {
        var itr = components.iterator();
        var builder = Component.empty().toBuilder();
        while (itr.hasNext()) {
            builder.append(itr.next());
            if (itr.hasNext()) {
                builder.append(sep);
            }
        }
        return builder.asComponent();
    }

    public static Component join(
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

    public static @NotNull TextComponent asBanner(@NotNull String title) {
        return textOfChildren(text("[ ", YELLOW), text(title, LIGHT_PURPLE), text(" ]", YELLOW));
    }

}
