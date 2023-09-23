package io.github.hello09x.bedrock.page;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public record Page<T>(
        List<T> records,

        int page,

        int size,

        int pages,

        int total

) {

    public static <U> Page<U> empty() {
        return new Page<>(
                Collections.emptyList(),
                1,
                0,
                1,
                0
        );
    }

    public static <U> Page<U> of(List<U> data, int page, int size) {
        int total = data.size();
        if (total == 0) {
            return empty();
        }
        if (size < 1) {
            size = 10;
        }

        var pages = (int) Math.ceil((double) total / size);
        page = Math.max(Math.min(page, pages), 1);

        var from = Math.min((page - 1) * size, total - 1);
        var to = Math.min(from + size, total);
        var records = data.subList(from, to);

        return new Page<>(
                records,
                page,
                size,
                pages,
                total
        );
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public int size() {
        return records.size();
    }

    public boolean hasNext() {
        return this.page < pages;
    }

    public boolean hasPrevious() {
        return this.page > 1;
    }

    public @NotNull Component asComponent(
            @NotNull Component title,
            @NotNull Function<@UnknownNullability T, @NotNull Component> mapping,
            @NotNull Function<@NotNull Integer, @NotNull String> commandMapping
    ) {
        var builder = text();
        builder.append(title).append(newline());
        if (isEmpty()) {
            builder.append(text("\n... 没有更多数据了 ...\n\n", GRAY));
        } else {
            var itr = records.listIterator();
            while (itr.hasNext()) {
                var i = itr.nextIndex();
                var record = itr.next();
                var color = i % 2 == 0 ? WHITE : GRAY;
                var row = text("", color)
                        .append(mapping.apply(record))
                        .append(newline());
                builder.append(row);
            }
        }


        if (hasPrevious()) {
            builder.append(text("◀").clickEvent(runCommand(commandMapping.apply(page - 1))));
            builder.append(text(" "));
        } else {
            builder.append(text("◀ ", GRAY));
        }


        int left = page - 2;
        if (left <= 2) {
            for (int i = 1; i < page; i++) {
                builder.append(text(i).clickEvent(runCommand(commandMapping.apply(i))));
                builder.append(text(" "));
            }
        } else {
            builder.append(text("1").clickEvent(runCommand(commandMapping.apply(1))));
            builder.append(text(" "));
            builder.append(text("···").clickEvent(runCommand(commandMapping.apply(Math.max(page - 5, 2)))));
            builder.append(text(" "));
            for (int i = left; i < page; i++) {
                builder.append(text(i).clickEvent(runCommand(commandMapping.apply(i))));
                builder.append(text(" "));
            }
        }

        builder.append(text(page, AQUA));
        builder.append(text(" "));

        int next = page + 1;
        for (; next <= pages && next <= page + 2; next++) {
            builder.append(text(next).clickEvent(runCommand(commandMapping.apply(next))));
            builder.append(text(" "));
        }
        if (next < pages) {
            builder.append(text("···").clickEvent(runCommand(commandMapping.apply(Math.min(page + 5, pages - 1)))));
            builder.append(text(" "));
            builder.append(text(pages).clickEvent(runCommand(commandMapping.apply(pages))));
            builder.append(text(" "));
        } else if (next == pages) {
            builder.append(text(pages).clickEvent(runCommand(commandMapping.apply(pages))));
            builder.append(text(" "));
        }

        if (hasNext()) {
            builder.append(text("▶").clickEvent(runCommand(commandMapping.apply(page + 1))));
            builder.append(text(" "));
        } else {
            builder.append(text("▶ ", GRAY));
        }

        return builder.asComponent();
    }

}
