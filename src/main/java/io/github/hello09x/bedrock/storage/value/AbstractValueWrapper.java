package io.github.hello09x.bedrock.storage.value;

import io.github.hello09x.bedrock.storage.JSONPersistentDataContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public abstract class AbstractValueWrapper<T> {

    @Getter
    @Nullable
    protected final T value;

    public static <Z> @NotNull AbstractValueWrapper<?> of(@NotNull PersistentDataType<?, Z> type, @Nullable Z value) {
        var complexType = type.getComplexType();
        if (complexType == boolean.class || complexType == Boolean.class) {
            return new BooleanValueWrapper((Boolean) value);
        } else if (complexType == byte.class || complexType == Byte.class) {
            return new ByteValueWrapper((Byte) value);
        } else if (complexType == int.class || complexType == Integer.class) {
            return new IntValueWrapper((Integer) value);
        } else if (complexType == short.class || complexType == Short.class) {
            return new ShortValueWrapper((Short) value);
        } else if (complexType == long.class || complexType == Long.class) {
            return new LongValueWrapper((Long) value);
        } else if (complexType == float.class || complexType == Float.class) {
            return new FloatValueWrapper((Float) value);
        } else if (complexType == double.class || complexType == Double.class) {
            return new DoubleValueWrapper((Double) value);
        } else if (complexType == String.class) {
            return new StringValueWrapper((String) value);
        } else if (complexType == String[].class) {
            return new StringArrayValueWrapper((String[]) value);
        } else if (complexType == byte[].class) {
            return new ByteArrayValueWrapper((byte[]) value);
        } else if (complexType == int[].class) {
            return new IntArrayValueWrapper((int[]) value);
        } else if (complexType == long[].class) {
            return new LongArrayValueWrapper((long[]) value);
        } else if (complexType == TagContainerValueWrapper.class) {
            return new TagContainerValueWrapper((JSONPersistentDataContainer) value);
        } else if (complexType == JSONPersistentDataContainer[].class) {
            return new TagContainerArrayValueWrapper((JSONPersistentDataContainer[]) value);
        } else {
            throw new IllegalArgumentException("unsupported type: " + type.getPrimitiveType());
        }
    }


}
