package io.github.hello09x.bedrock.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class CachedSupplier<T> {

    private final Supplier<T> supplier;
    private volatile T value;

    public CachedSupplier(@NotNull Supplier<@NotNull T> supplier) {
        this.supplier = supplier;
    }

    public @NotNull T get() {
        if (this.value == null) {
            this.value = Objects.requireNonNull(supplier.get());
        }
        return this.value;
    }


    public void remove() {
        this.value = null;
    }

}
