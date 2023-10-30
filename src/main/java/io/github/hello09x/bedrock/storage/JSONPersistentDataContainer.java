package io.github.hello09x.bedrock.storage;

import io.github.hello09x.bedrock.storage.value.AbstractValueWrapper;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JSONPersistentDataContainer implements PersistentDataContainer, Serializable {

    private final static JSONPersistentDataContainerSerializer SERIALIZER = new JSONPersistentDataContainerSerializer();

    private final Map<String, AbstractValueWrapper<?>> data = new HashMap<>();

    public static @NotNull JSONPersistentDataContainer deserializeFromJSON(@NotNull String json) {
        return SERIALIZER.getGson().fromJson(json, JSONPersistentDataContainer.class);
    }

    @Override
    public <T, Z> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
        data.put(key.toString(), AbstractValueWrapper.of(type, value));
    }

    @Override
    public <T, Z> boolean has(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type) {
        return this.get(key, type) != null;
    }

    @Override
    public <T, Z> @Nullable Z get(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type) {
        return Optional.ofNullable(data.get(key.toString()))
                .map(AbstractValueWrapper::getValue)
                .filter(value -> type.getComplexType().isAssignableFrom(value.getClass()))
                .map(value -> type.getComplexType().cast(value))
                .orElse(null);

    }

    @Override
    public <T, Z> @NotNull Z getOrDefault(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue) {
        return Optional.ofNullable(this.get(key, type)).orElse(defaultValue);
    }

    @Override
    public @NotNull Set<NamespacedKey> getKeys() {
        return this.data.keySet().stream().map(key -> {
            var parts = key.split(":");
            if (parts.length != 2) {
                return null;
            }
            return new NamespacedKey(parts[0], parts[1]);
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public void remove(@NotNull NamespacedKey key) {
        this.data.remove(key.toString());
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public @NotNull PersistentDataAdapterContext getAdapterContext() {
        throw new UnsupportedOperationException("getAdapterContext");
    }

    @Override
    public boolean has(@NotNull NamespacedKey key) {
        return this.data.containsKey(key.toString());
    }

    public @NotNull String serializeToJSON() throws IOException {
        return SERIALIZER.getGson().toJson(this);
    }

    public void readFromBytes(byte @NotNull [] bytes, boolean clear) throws java.io.IOException {
        if (clear) {
            this.data.clear();
        }
        this.data.putAll(SERIALIZER.getGson().fromJson(new String(bytes, StandardCharsets.UTF_8), JSONPersistentDataContainer.class).data);
    }

    public byte[] serializeToBytes() throws java.io.IOException {
        return this.serializeToJSON().getBytes(StandardCharsets.UTF_8);
    }

    public void clear() {
        this.data.clear();
    }

}
