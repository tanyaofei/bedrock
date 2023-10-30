package io.github.hello09x.bedrock.storage.type;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface JSONPersistentDataType {

    PersistentDataType<String[], String[]> STRING_ARRAY = new JSONComplexPersistentDataType<>(String[].class);

    class JSONComplexPersistentDataType<Z> implements PersistentDataType<Z, Z> {

        private final Class<Z> type;

        public JSONComplexPersistentDataType(Class<Z> type) {
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        private JSONComplexPersistentDataType(Z represent) {
            this.type = (Class<Z>) represent.getClass();
        }

        @Override
        public @NotNull Class<Z> getPrimitiveType() {
            return this.type;
        }

        @Override
        public @NotNull Class<Z> getComplexType() {
            return this.type;
        }

        @Override
        public @NotNull Z toPrimitive(@NotNull Z complex, @NotNull PersistentDataAdapterContext context) {
            return complex;
        }

        @Override
        public @NotNull Z fromPrimitive(@NotNull Z primitive, @NotNull PersistentDataAdapterContext context) {
            return primitive;
        }
    }


}
