package io.github.hello09x.bedrock.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LazyInit<V> {

    private final static Object INITIALIZED = new Object();

    private V value;

    public V computeIfAbsent(@NotNull Supplier<V> supplier, boolean once) {
        if (this.value != null) {
            if (this.value == INITIALIZED) {
                return null;
            }
            return this.value;
        }

        this.value = supplier.get();
        if (once && this.value == null) {
            this.value = (V) INITIALIZED;
        }
        return this.value;
    }

}
